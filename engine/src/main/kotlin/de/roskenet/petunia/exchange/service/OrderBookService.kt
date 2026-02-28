package de.roskenet.petunia.exchange.service

import de.roskenet.petunia.exchange.domain.Order
import de.roskenet.petunia.exchange.domain.OrderSide
import de.roskenet.petunia.exchange.domain.OrderStatus
import de.roskenet.petunia.exchange.repository.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderBookService(
    private val orderRepository: OrderRepository
) {
    @Transactional
    fun placeOrder(
        playerName: String,
        symbol: String,
        quantity: Long,
        price: Long,
        side: OrderSide
    ): Order {
        val order = Order(
            playerName = playerName,
            symbol = symbol,
            quantity = quantity,
            remainingQuantity = quantity,
            price = price,
            side = side,
            status = OrderStatus.OPEN
        )
        
        // In the future, matching logic would be called here.
        
        return orderRepository.save(order)
    }

    fun getOpenOrders(): List<Order> {
        return orderRepository.findByStatus(OrderStatus.OPEN)
    }

    fun getOpenOrdersBySymbol(symbol: String): List<Order> {
        return orderRepository.findBySymbolAndStatus(symbol, OrderStatus.OPEN)
    }
}
