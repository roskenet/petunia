package de.roskenet.petunia.dto

import de.roskenet.petunia.enums.OrderSide
import de.roskenet.petunia.enums.OrderType
import java.time.OffsetDateTime
import java.util.*

data class PlaceOrderRequest(
    val playerId: UUID,
    val symbol: String,
    val quantity: Long,
    val price: Long,
    val side: OrderSide,
    val type: OrderType = OrderType.LIMIT
) {
    constructor() : this(UUID.randomUUID(), "", 0, 0, OrderSide.BUY, OrderType.LIMIT)
}

data class OrderResponse(
    val id: UUID,
    val playerId: UUID,
    val symbol: String,
    val quantity: Long,
    val remainingQuantity: Long,
    val price: Long,
    val side: OrderSide,
    val type: OrderType,
    val createdAt: OffsetDateTime?
) {
    constructor() : this(UUID.randomUUID(), UUID.randomUUID(), "", 0, 0, 0, OrderSide.BUY, OrderType.LIMIT, null)
}
