package de.roskenet.petunia.bank.service

import de.roskenet.petunia.bank.domain.Asset
import de.roskenet.petunia.bank.domain.PlayerAccount
import de.roskenet.petunia.bank.config.GameProperties
import de.roskenet.petunia.bank.repository.AssetRepository
import de.roskenet.petunia.bank.repository.PlayerAccountRepository
import de.roskenet.petunia.security.service.SecurityService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ClearingService(
    private val playerAccountRepository: PlayerAccountRepository,
    private val assetRepository: AssetRepository,
    private val securityService: SecurityService,
    private val gameProperties: GameProperties
) {
    @Transactional(readOnly = true)
    fun getAllAccounts(): List<PlayerAccount> = playerAccountRepository.findAll()

    @Transactional(readOnly = true)
    fun getAccountByPlayerName(playerName: String): PlayerAccount? =
        playerAccountRepository.findByPlayerName(playerName)

    @Transactional(readOnly = true)
    fun getAccountById(id: UUID): PlayerAccount? =
        playerAccountRepository.findById(id).orElse(null)

    @Transactional
    fun createAccount(playerSubject: UUID, playerName: String): PlayerAccount {
        val initialBalance = gameProperties.account.initialBalance
        val account = PlayerAccount(id=playerSubject, playerName = playerName, balance = initialBalance)
        return playerAccountRepository.save(account)
    }

    @Transactional
    fun updateAccount(id: UUID, newPlayerName: String, balance: Long): PlayerAccount {
        val existingAccount = playerAccountRepository.findById(id).orElseThrow { IllegalArgumentException("Account not found") }

        val updatedAccount = existingAccount.copy(playerName = newPlayerName, balance = balance)
        return playerAccountRepository.save(updatedAccount)
    }

    @Transactional
    fun deleteAccountById(id: UUID) {
        playerAccountRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    fun getAssetsByAccountId(accountId: UUID): List<Asset> = assetRepository.findByAccountId(accountId)

    @Transactional
    fun addAsset(accountId: UUID, symbol: String, quantity: Long): Asset {
        val account = playerAccountRepository.findById(accountId)
            .orElseThrow { IllegalArgumentException("Account not found") }
        val security = securityService.requireBySymbol(symbol)

        val existingAsset = assetRepository.findByAccountIdAndSecuritySymbol(accountId, symbol)
        val asset = if (existingAsset != null) {
            existingAsset.copy(quantity = existingAsset.quantity + quantity)
        } else {
            Asset(account = account, security = security, quantity = quantity)
        }
        return assetRepository.save(asset)
    }

    @Transactional
    fun removeAsset(accountId: UUID, symbol: String, quantity: Long): Asset {
        val asset = assetRepository.findByAccountIdAndSecuritySymbol(accountId, symbol)
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
        val security = securityService.requireBySymbol(symbol)
        val buyer = playerAccountRepository.findByPlayerName(buyerName)
            ?: throw IllegalArgumentException("Buyer not found")
        val seller = playerAccountRepository.findByPlayerName(sellerName)
            ?: throw IllegalArgumentException("Seller not found")
        val sellerAsset = assetRepository.findByAccountIdAndSecuritySymbol(seller.id, symbol)
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
        val buyerAsset = assetRepository.findByAccountIdAndSecuritySymbol(buyer.id, symbol)
        val updatedBuyerAsset = if (buyerAsset != null) {
            buyerAsset.copy(quantity = buyerAsset.quantity + quantity)
        } else {
            Asset(account = buyer, security = security, quantity = quantity)
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
