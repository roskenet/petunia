package de.roskenet.petunia.dto

import java.util.UUID

data class PlayerAccountDto(
    val id: UUID,
    val playerName: String,
    val balance: Long
)

data class CreatePlayerAccountRequest(
    val playerName: String,
    val initialBalance: Long = 0L
)

data class UpdatePlayerAccountRequest(
    val playerName: String,
    val balance: Long
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

data class ClearTradeRequest(
    val buyerName: String,
    val sellerName: String,
    val symbol: String,
    val quantity: Long,
    val price: Long
)

data class ClearTradeResponse(
    val buyerAccountId: UUID,
    val sellerAccountId: UUID,
    val symbol: String,
    val quantity: Long,
    val price: Long,
    val total: Long
)

data class SecurityDto(
    val symbol: String,
    val name: String
)

data class CreateSecurityRequest(
    val symbol: String,
    val name: String
)

data class UpdateSecurityRequest(
    val name: String
)
