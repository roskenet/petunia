package com.petunia.clearing.cucumber

import com.petunia.clearing.api.dto.ClearTradeRequest
import com.petunia.clearing.domain.Asset
import com.petunia.clearing.domain.PlayerAccount
import com.petunia.clearing.repository.AssetRepository
import com.petunia.clearing.repository.PlayerAccountRepository
import io.cucumber.datatable.DataTable
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import java.util.UUID

class TradeStepDefinitions(
    private val playerAccountRepository: PlayerAccountRepository,
    private val assetRepository: AssetRepository,
    private val restTemplate: TestRestTemplate
) {

    private val initialBalances = mutableMapOf<String, Long>()
    private val initialShares = mutableMapOf<String, Long>()
    private val accountIds = mutableMapOf<String, UUID>()

    private lateinit var trade: TradeInput

    @Before
    fun resetState() {
        assetRepository.deleteAll()
        playerAccountRepository.deleteAll()
        initialBalances.clear()
        initialShares.clear()
        accountIds.clear()
    }

    @Given("The following players exist:")
    fun theFollowingPlayersExist(dataTable: DataTable) {
        val initialBalance = 100000L // 1000.00 euros as 100000 cents
        dataTable.asMaps().forEach { row ->
            val name = row["Name"]?.trim().orEmpty()
            val shares = row["Shares (BAY)"]?.trim().orEmpty().toLong()

            val account = playerAccountRepository.save(
                PlayerAccount(playerName = name, balance = initialBalance)
            )
            accountIds[name] = account.id
            initialBalances[name] = account.balance
            initialShares[name] = shares

            if (shares > 0) {
                assetRepository.save(Asset(account = account, symbol = "BAY", quantity = shares))
            }
        }
    }

    @Given("{word} has bought {long} shares of {word} from {word} at a price of {long}")
    fun playerHasBoughtSharesFromPlayerAtPrice(
        buyerName: String,
        quantity: Long,
        symbol: String,
        sellerName: String,
        price: Long
    ) {
        trade = TradeInput(
            buyerName = buyerName,
            sellerName = sellerName,
            symbol = symbol,
            quantity = quantity,
            price = price
        )
    }

    @When("the clearing webservice is called to clear the trade")
    fun theClearingWebserviceIsCalledToClearTheTrade() {
        val response = restTemplate.postForEntity(
            "/api/trades/clear",
            ClearTradeRequest(
                buyerName = trade.buyerName,
                sellerName = trade.sellerName,
                symbol = trade.symbol,
                quantity = trade.quantity,
                price = trade.price
            ),
            String::class.java
        )
        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Then("{long} shares of {word} should be transferred from {word} to {word}")
    fun sharesShouldBeTransferredFromSellerToBuyer(
        quantity: Long,
        symbol: String,
        sellerName: String,
        buyerName: String
    ) {
        val sellerId = accountIds[sellerName] ?: error("Seller not found")
        val buyerId = accountIds[buyerName] ?: error("Buyer not found")

        val sellerAsset = assetRepository.findByAccountIdAndSymbol(sellerId, symbol)
        val buyerAsset = assetRepository.findByAccountIdAndSymbol(buyerId, symbol)

        val sellerInitial = initialShares[sellerName] ?: 0L
        val buyerInitial = initialShares[buyerName] ?: 0L

        assertEquals(sellerInitial - quantity, sellerAsset?.quantity ?: 0L)
        assertEquals(buyerInitial + quantity, buyerAsset?.quantity ?: 0L)
    }

    @Then("{long} should be transferred from {word} to {word}")
    fun amountShouldBeTransferredFromBuyerToSeller(
        amount: Long,
        buyerName: String,
        sellerName: String
    ) {
        val sellerId = accountIds[sellerName] ?: error("Seller not found")
        val buyerId = accountIds[buyerName] ?: error("Buyer not found")

        val buyer = playerAccountRepository.findById(buyerId).orElseThrow()
        val seller = playerAccountRepository.findById(sellerId).orElseThrow()

        val buyerInitial = initialBalances[buyerName] ?: 0L
        val sellerInitial = initialBalances[sellerName] ?: 0L

        val buyerDelta = buyerInitial - buyer.balance
        val sellerDelta = seller.balance - sellerInitial

        assertEquals(amount, buyerDelta)
        assertEquals(amount, sellerDelta)
    }

    private data class TradeInput(
        val buyerName: String,
        val sellerName: String,
        val symbol: String,
        val quantity: Long,
        val price: Long
    )
}
