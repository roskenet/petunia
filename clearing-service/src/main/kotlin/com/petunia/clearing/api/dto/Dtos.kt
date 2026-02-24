package com.petunia.clearing.api.dto

import java.math.BigDecimal
import java.util.UUID

data class PlayerAccountDto(
    val id: UUID,
    val playerName: String,
    val balance: BigDecimal
)

data class CreatePlayerAccountRequest(
    val playerName: String,
    val initialBalance: BigDecimal = BigDecimal.ZERO
)

data class AssetDto(
    val id: UUID,
    val accountId: UUID,
    val symbol: String,
    val quantity: Long
)

data class CreateAssetRequest(
    val symbol: String,
    val quantity: Long
)
