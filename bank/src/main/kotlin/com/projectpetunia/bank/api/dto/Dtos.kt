package com.projectpetunia.bank.api.dto

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
