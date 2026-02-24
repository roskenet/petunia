package com.petunia.clearing.repository

import com.petunia.clearing.domain.Asset
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AssetRepository : JpaRepository<Asset, UUID> {
    fun findByAccountId(accountId: UUID): List<Asset>
    fun findByAccountIdAndSymbol(accountId: UUID, symbol: String): Asset?
}
