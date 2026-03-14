package de.roskenet.petunia.exchange.service

import de.roskenet.petunia.bank.service.ClearingService
import de.roskenet.petunia.exchange.domain.Order
import de.roskenet.petunia.exchange.domain.Trade
import de.roskenet.petunia.exchange.dto.OrderSide
import de.roskenet.petunia.exchange.dto.OrderType
import de.roskenet.petunia.exchange.repository.OrderRepository
import de.roskenet.petunia.exchange.repository.TradeRepository
import de.roskenet.petunia.security.service.SecurityService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class OrderBookService(
    private val orderRepository: OrderRepository,
    private val tradeRepository: TradeRepository,
    private val clearingService: ClearingService,
    private val securityService: SecurityService
) {
    @Transactional
    fun placeOrder(
        playerId: UUID,
        symbol: String,
        quantity: Long,
        price: Long,
        side: OrderSide,
        type: OrderType = OrderType.LIMIT
    ): Order {
        val security = securityService.requireBySymbol(symbol)
        val order = Order(
            playerId = playerId,
            security = security,
            quantity = quantity,
            price = price,
            side = side,
            type = type
        )

        val savedOrder = orderRepository.save(order)

        matchOrders(savedOrder)

        return savedOrder
    }

    private fun matchOrders(newOrder: Order) {
        val oppositeSide = if (newOrder.side == OrderSide.BUY) OrderSide.SELL else OrderSide.BUY

        while (newOrder.quantity > 0) {
            val matchingOrders = orderRepository.findBySecuritySymbol(newOrder.symbol)
                .filter { it.id != newOrder.id }
                .filter { it.side == oppositeSide }
                .filter {
                    if (newOrder.type == OrderType.MARKET) {
                        true
                    } else if (newOrder.side == OrderSide.BUY) {
                        it.price <= newOrder.price
                    } else {
                        it.price >= newOrder.price
                    }
                }
                .sortedWith(
                    if (newOrder.side == OrderSide.BUY) {
                        compareBy<Order> { it.price }.thenBy { it.createdAt }
                    } else {
                        compareByDescending<Order> { it.price }.thenBy { it.createdAt }
                    }
                )

            if (matchingOrders.isEmpty()) break

            val match = matchingOrders.first()
            val tradeQuantity = Math.min(newOrder.quantity, match.quantity)
            val tradePrice = match.price

            val buyer = if (newOrder.side == OrderSide.BUY) newOrder else match
            val seller = if (newOrder.side == OrderSide.BUY) match else newOrder

            clearingService.clearTrade(
                buyerId = buyer.playerId,
                sellerId = seller.playerId,
                symbol = newOrder.symbol,
                quantity = tradeQuantity,
                price = tradePrice
            )

            tradeRepository.save(
                Trade(
                    buyerId = buyer.playerId,
                    sellerId = seller.playerId,
                    security = newOrder.security,
                    quantity = tradeQuantity,
                    price = tradePrice
                )
            )

            newOrder.quantity -= tradeQuantity
            match.quantity -= tradeQuantity

            if (match.quantity == 0L) {
                orderRepository.delete(match)
            } else {
                orderRepository.save(match)
            }

            if (newOrder.quantity == 0L) {
                orderRepository.delete(newOrder)
            } else {
                orderRepository.save(newOrder)
            }
        }

        if (newOrder.type == OrderType.MARKET && newOrder.quantity > 0) {
            orderRepository.delete(newOrder)
            newOrder.quantity = 0
        }
    }

    fun getOpenOrders(): List<Order> {
        return orderRepository.findAll()
    }

    fun getOpenOrdersBySymbol(symbol: String): List<Order> {
        return orderRepository.findBySecuritySymbol(symbol)
    }
}
