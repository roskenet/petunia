package com.petunia.clearing.service

import com.petunia.clearing.domain.Asset
import com.petunia.clearing.domain.PlayerAccount
import com.petunia.clearing.repository.AssetRepository
import com.petunia.clearing.repository.PlayerAccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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
    fun createAccount(playerName: String, initialBalance: Long): PlayerAccount {
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

    @Transactional
    fun clearTrade(
        buyerName: String,
        sellerName: String,
        symbol: String,
        quantity: Long,
        price: Long
    ): TradeSettlement {
        val buyer = playerAccountRepository.findByPlayerName(buyerName)
            ?: throw IllegalArgumentException("Buyer not found")
        val seller = playerAccountRepository.findByPlayerName(sellerName)
            ?: throw IllegalArgumentException("Seller not found")

        val sellerAsset = assetRepository.findByAccountIdAndSymbol(seller.id, symbol)
            ?: throw IllegalArgumentException("Seller asset not found")
        if (sellerAsset.quantity < quantity) {
            throw IllegalArgumentException("Insufficient asset quantity")
        }

        val total = price * quantity
        if (buyer.balance < total) {
            throw IllegalArgumentException("Insufficient balance")
        }

        val updatedSellerAsset = sellerAsset.copy(quantity = sellerAsset.quantity - quantity)
        assetRepository.save(updatedSellerAsset)

        val buyerAsset = assetRepository.findByAccountIdAndSymbol(buyer.id, symbol)
        val updatedBuyerAsset = if (buyerAsset != null) {
            buyerAsset.copy(quantity = buyerAsset.quantity + quantity)
        } else {
            Asset(account = buyer, symbol = symbol, quantity = quantity)
        }
        assetRepository.save(updatedBuyerAsset)

        val updatedBuyer = buyer.copy(balance = buyer.balance - total)
        val updatedSeller = seller.copy(balance = seller.balance + total)
        playerAccountRepository.save(updatedBuyer)
        playerAccountRepository.save(updatedSeller)

        return TradeSettlement(
            buyerAccountId = buyer.id,
            sellerAccountId = seller.id,
            symbol = symbol,
            quantity = quantity,
            price = price,
            total = total
        )
    }
}

data class TradeSettlement(
    val buyerAccountId: UUID,
    val sellerAccountId: UUID,
    val symbol: String,
    val quantity: Long,
    val price: Long,
    val total: Long
)
