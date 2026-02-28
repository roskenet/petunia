package de.roskenet.petunia.exchange.api

import de.roskenet.petunia.exchange.api.dto.OrderResponse
import de.roskenet.petunia.exchange.api.dto.PlaceOrderRequest
import de.roskenet.petunia.exchange.domain.Order
import de.roskenet.petunia.exchange.service.OrderBookService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderBookService: OrderBookService
) {
    @PostMapping
    fun placeOrder(@RequestBody request: PlaceOrderRequest): OrderResponse {
        val order = orderBookService.placeOrder(
            playerName = request.playerName,
            symbol = request.symbol,
            quantity = request.quantity,
            price = request.price,
            side = request.side
        )
        return order.toResponse()
    }

    @GetMapping
    fun getOpenOrders(@RequestParam(required = false) symbol: String?): List<OrderResponse> {
        val orders = if (symbol != null) {
            orderBookService.getOpenOrdersBySymbol(symbol)
        } else {
            orderBookService.getOpenOrders()
        }
        return orders.map { it.toResponse() }
    }

    private fun Order.toResponse() = OrderResponse(
        id = this.id,
        playerName = this.playerName,
        symbol = this.symbol,
        quantity = this.quantity,
        remainingQuantity = this.remainingQuantity,
        price = this.price,
        side = this.side,
        status = this.status,
        createdAt = this.createdAt
    )
}
