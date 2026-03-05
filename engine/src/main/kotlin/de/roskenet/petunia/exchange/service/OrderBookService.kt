package de.roskenet.petunia.exchange.service

import de.roskenet.petunia.bank.service.ClearingService
import de.roskenet.petunia.exchange.domain.Order
import de.roskenet.petunia.exchange.domain.Trade
import de.roskenet.petunia.enums.OrderSide
import de.roskenet.petunia.enums.OrderStatus
import de.roskenet.petunia.enums.OrderType
import de.roskenet.petunia.exchange.repository.OrderRepository
import de.roskenet.petunia.exchange.repository.TradeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderBookService(
    private val orderRepository: OrderRepository,
    private val tradeRepository: TradeRepository,
    private val clearingService: ClearingService
) {
    @Transactional
    fun placeOrder(
        playerName: String,
        symbol: String,
        quantity: Long,
        price: Long,
        side: OrderSide,
        type: OrderType = OrderType.LIMIT
    ): Order {
        val order = Order(
            playerName = playerName,
            symbol = symbol,
            quantity = quantity,
            remainingQuantity = quantity,
            price = price,
            side = side,
            type = type,
            status = OrderStatus.OPEN
        )
        
        val savedOrder = orderRepository.save(order)
        
        matchOrders(savedOrder)
        
        return savedOrder
    }

    private fun matchOrders(newOrder: Order) {
        val oppositeSide = if (newOrder.side == OrderSide.BUY) OrderSide.SELL else OrderSide.BUY
        
        while (newOrder.remainingQuantity > 0) {
            val matchingOrders = orderRepository.findBySymbolAndStatus(newOrder.symbol, OrderStatus.OPEN)
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
                        // For a BUY order, we want the lowest SELL price first (maker's best offer)
                        compareBy<Order> { it.price }.thenBy { it.createdAt }
                    } else {
                        // For a SELL order, we want the highest BUY price first (maker's best bid)
                        compareByDescending<Order> { it.price }.thenBy { it.createdAt }
                    }
                )

            if (matchingOrders.isEmpty()) break
            
            val match = matchingOrders.first()
            val tradeQuantity = Math.min(newOrder.remainingQuantity, match.remainingQuantity)
            val tradePrice = match.price // Price-time priority: Match at maker's price
            
            val buyer = if (newOrder.side == OrderSide.BUY) newOrder else match
            val seller = if (newOrder.side == OrderSide.BUY) match else newOrder
            
            clearingService.clearTrade(
                buyerName = buyer.playerName,
                sellerName = seller.playerName,
                symbol = newOrder.symbol,
                quantity = tradeQuantity,
                price = tradePrice
            )

            tradeRepository.save(
                Trade(
                    buyerName = buyer.playerName,
                    sellerName = seller.playerName,
                    symbol = newOrder.symbol,
                    quantity = tradeQuantity,
                    price = tradePrice
                )
            )
            
            newOrder.remainingQuantity -= tradeQuantity
            match.remainingQuantity -= tradeQuantity
            
            if (newOrder.remainingQuantity == 0L) {
                newOrder.status = OrderStatus.COMPLETED
            } else if (newOrder.type == OrderType.MARKET) {
                // Market orders are "Fill or Kill" / "Immediate or Cancel" in this implementation
                // If they can't be fully filled, the remaining part is cancelled.
                newOrder.status = OrderStatus.CANCELLED
            }
            
            if (match.remainingQuantity == 0L) {
                match.status = OrderStatus.COMPLETED
            }
            
            orderRepository.save(newOrder)
            orderRepository.save(match)
        }
        
        // If a market order was not matched at all, it should be cancelled
        if (newOrder.type == OrderType.MARKET && newOrder.remainingQuantity > 0) {
            newOrder.status = OrderStatus.CANCELLED
            orderRepository.save(newOrder)
        }
    }

    fun getOpenOrders(): List<Order> {
        return orderRepository.findByStatus(OrderStatus.OPEN)
    }

    fun getOpenOrdersBySymbol(symbol: String): List<Order> {
        return orderRepository.findBySymbolAndStatus(symbol, OrderStatus.OPEN)
    }
}
