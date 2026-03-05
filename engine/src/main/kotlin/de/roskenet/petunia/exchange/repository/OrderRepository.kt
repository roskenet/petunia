package de.roskenet.petunia.exchange.repository

import de.roskenet.petunia.exchange.domain.Order
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OrderRepository : JpaRepository<Order, UUID> {
    fun findBySymbol(symbol: String): List<Order>
}
