package de.roskenet.petunia.bank.api

import de.roskenet.petunia.dto.ClearTradeRequest
import de.roskenet.petunia.dto.ClearTradeResponse
import de.roskenet.petunia.bank.service.ClearingService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/trades")
class TradeController(
    private val clearingService: ClearingService
) {
    @PostMapping("/clear")
    fun clearTrade(@RequestBody request: ClearTradeRequest): ClearTradeResponse {
        val settlement = clearingService.clearTrade(
            buyerId = request.buyerId,
            sellerId = request.sellerId,
            symbol = request.symbol,
            quantity = request.quantity,
            price = request.price
        )
        return ClearTradeResponse(
            buyerAccountId = settlement.buyerAccountId,
            sellerAccountId = settlement.sellerAccountId,
            symbol = settlement.symbol,
            quantity = settlement.quantity,
            price = settlement.price,
            total = settlement.total
        )
    }
}
