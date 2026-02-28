package de.roskenet.petunia.exchange.api.dto

import de.roskenet.petunia.exchange.domain.OrderSide
import de.roskenet.petunia.exchange.domain.OrderStatus
import java.time.OffsetDateTime
import java.util.*

data class PlaceOrderRequest(
    val playerName: String,
    val symbol: String,
    val quantity: Long,
    val price: Long,
    val side: OrderSide
)

data class OrderResponse(
    val id: UUID,
    val playerName: String,
    val symbol: String,
    val quantity: Long,
    val remainingQuantity: Long,
    val price: Long,
    val side: OrderSide,
    val status: OrderStatus,
    val createdAt: OffsetDateTime?
)
