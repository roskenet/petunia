package com.petunia.clearing.api

import com.petunia.clearing.api.dto.AssetDto
import com.petunia.clearing.api.dto.CreateAssetRequest
import com.petunia.clearing.service.ClearingService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/accounts/{accountId}/assets")
class AssetController(
    private val clearingService: ClearingService
) {

    @GetMapping
    fun getAssetsByAccountId(@PathVariable accountId: UUID): List<AssetDto> =
        clearingService.getAssetsByAccountId(accountId).map {
            AssetDto(it.id, it.account.id, it.symbol, it.quantity)
        }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addAsset(
        @PathVariable accountId: UUID,
        @RequestBody request: CreateAssetRequest
    ): AssetDto {
        val asset = clearingService.addAsset(accountId, request.symbol, request.quantity)
        return AssetDto(asset.id, asset.account.id, asset.symbol, asset.quantity)
    }

    @DeleteMapping("/{symbol}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeAsset(
        @PathVariable accountId: UUID,
        @PathVariable symbol: String,
        @RequestParam quantity: Long
    ) {
        clearingService.removeAsset(accountId, symbol, quantity)
    }
}
