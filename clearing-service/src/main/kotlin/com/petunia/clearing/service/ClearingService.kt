package com.petunia.clearing.service

import com.petunia.clearing.domain.Asset
import com.petunia.clearing.domain.PlayerAccount
import com.petunia.clearing.repository.AssetRepository
import com.petunia.clearing.repository.PlayerAccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.UUID

@Service
class ClearingService(
    private val playerAccountRepository: PlayerAccountRepository,
    private val assetRepository: AssetRepository
) {

    @Transactional(readOnly = true)
    fun getAllAccounts(): List<PlayerAccount> = playerAccountRepository.findAll()

    @Transactional(readOnly = true)
    fun getAccountById(id: UUID): PlayerAccount? = playerAccountRepository.findById(id).orElse(null)

    @Transactional
    fun createAccount(playerName: String, initialBalance: BigDecimal): PlayerAccount {
        val account = PlayerAccount(playerName = playerName, balance = initialBalance)
        return playerAccountRepository.save(account)
    }

    @Transactional
    fun deleteAccount(id: UUID) {
        playerAccountRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    fun getAssetsByAccountId(accountId: UUID): List<Asset> = assetRepository.findByAccountId(accountId)

    @Transactional
    fun addAsset(accountId: UUID, symbol: String, quantity: Long): Asset {
        val account = playerAccountRepository.findById(accountId)
            .orElseThrow { IllegalArgumentException("Account not found") }
        
        val existingAsset = assetRepository.findByAccountIdAndSymbol(accountId, symbol)
        val asset = if (existingAsset != null) {
            existingAsset.copy(quantity = existingAsset.quantity + quantity)
        } else {
            Asset(account = account, symbol = symbol, quantity = quantity)
        }
        return assetRepository.save(asset)
    }

    @Transactional
    fun removeAsset(accountId: UUID, symbol: String, quantity: Long): Asset {
        val asset = assetRepository.findByAccountIdAndSymbol(accountId, symbol)
            ?: throw IllegalArgumentException("Asset not found")
        
        if (asset.quantity < quantity) {
            throw IllegalArgumentException("Insufficient quantity")
        }
        
        val updatedAsset = asset.copy(quantity = asset.quantity - quantity)
        return assetRepository.save(updatedAsset)
    }
}
