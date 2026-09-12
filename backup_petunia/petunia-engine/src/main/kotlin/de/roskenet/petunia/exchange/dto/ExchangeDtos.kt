package de.roskenet.petunia.exchange.dto

import java.time.OffsetDateTime
import java.util.UUID

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
    val price: Long,
    val side: OrderSide,
    val type: OrderType,
    val createdAt: OffsetDateTime?
) {
    constructor() : this(UUID.randomUUID(), UUID.randomUUID(), "", 0, 0, OrderSide.BUY, OrderType.LIMIT, null)
}
