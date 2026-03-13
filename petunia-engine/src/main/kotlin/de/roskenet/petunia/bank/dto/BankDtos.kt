package de.roskenet.petunia.dto

import java.util.UUID

data class PlayerAccountDto(
    val id: UUID,
    val playerName: String,
    val balance: Long
) {
    constructor() : this(UUID.randomUUID(), "", 0)
}

data class CreatePlayerAccountRequest(
    val playerSubject: UUID = UUID.randomUUID(),
    val playerName: String
) {
    constructor() : this(UUID.randomUUID(), "")
}

data class UpdatePlayerAccountRequest(
    val playerName: String,
    val balance: Long
) {
    constructor() : this("", 0)
}

data class AssetDto(
    val id: UUID,
    val accountId: UUID,
    val symbol: String,
    val quantity: Long
) {
    constructor() : this(UUID.randomUUID(), UUID.randomUUID(), "", 0)
}

data class CreateAssetRequest(
    val symbol: String,
    val quantity: Long
) {
    constructor() : this("", 0)
}

data class ClearTradeRequest(
    val buyerId: UUID,
    val sellerId: UUID,
    val symbol: String,
    val quantity: Long,
    val price: Long
) {
    constructor() : this(UUID.randomUUID(), UUID.randomUUID(), "", 0, 0)
}

data class ClearTradeResponse(
    val buyerAccountId: UUID,
    val sellerAccountId: UUID,
    val symbol: String,
    val quantity: Long,
    val price: Long,
    val total: Long
) {
    constructor() : this(UUID.randomUUID(), UUID.randomUUID(), "", 0, 0, 0)
}

data class SecurityDto(
    val symbol: String,
    val name: String
) {
    constructor() : this("", "")
}

data class CreateSecurityRequest(
    val symbol: String,
    val name: String
) {
    constructor() : this("", "")
}

data class UpdateSecurityRequest(
    val name: String
) {
    constructor() : this("")
}
