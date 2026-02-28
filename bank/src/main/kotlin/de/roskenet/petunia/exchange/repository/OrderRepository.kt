package de.roskenet.petunia.exchange.repository

import de.roskenet.petunia.exchange.domain.Order
import de.roskenet.petunia.exchange.domain.OrderStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OrderRepository : JpaRepository<Order, UUID> {
    fun findByStatus(status: OrderStatus): List<Order>
    fun findBySymbolAndStatus(symbol: String, status: OrderStatus): List<Order>
}
