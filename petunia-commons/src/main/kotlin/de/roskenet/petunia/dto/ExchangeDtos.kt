package de.roskenet.petunia.dto

import de.roskenet.petunia.enums.OrderSide
import de.roskenet.petunia.enums.OrderStatus
import de.roskenet.petunia.enums.OrderType
import java.time.OffsetDateTime
import java.util.*

data class PlaceOrderRequest(
    val playerName: String,
    val symbol: String,
    val quantity: Long,
    val price: Long,
    val side: OrderSide,
    val type: OrderType = OrderType.LIMIT
)

data class OrderResponse(
    val id: UUID,
    val playerName: String,
    val symbol: String,
    val quantity: Long,
    val remainingQuantity: Long,
    val price: Long,
    val side: OrderSide,
    val type: OrderType,
    val status: OrderStatus,
    val createdAt: OffsetDateTime?
)
