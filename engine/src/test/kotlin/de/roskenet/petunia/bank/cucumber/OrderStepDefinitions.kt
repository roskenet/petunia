package de.roskenet.petunia.bank.cucumber

import de.roskenet.petunia.bank.domain.Asset
import de.roskenet.petunia.bank.domain.PlayerAccount
import de.roskenet.petunia.bank.repository.AssetRepository
import de.roskenet.petunia.bank.repository.PlayerAccountRepository
import de.roskenet.petunia.dto.PlaceOrderRequest
import de.roskenet.petunia.enums.OrderSide
import de.roskenet.petunia.enums.OrderStatus
import de.roskenet.petunia.exchange.repository.OrderRepository
import de.roskenet.petunia.exchange.repository.TradeRepository
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.resttestclient.TestRestTemplate
import org.springframework.http.HttpStatus

class OrderStepDefinitions(
    private val playerAccountRepository: PlayerAccountRepository,
    private val assetRepository: AssetRepository,
    private val orderRepository: OrderRepository,
    private val tradeRepository: TradeRepository,
    private val restTemplate: TestRestTemplate
) {

    @Given("The following accounts exist:")
    fun theFollowingAccountsExist(dataTable: DataTable) {
        orderRepository.deleteAll()
        tradeRepository.deleteAll()
        assetRepository.deleteAll()
        playerAccountRepository.deleteAll()

        dataTable.asMaps().forEach { row ->
            val name = row["Name"]!!
            val balance = row["Account Balance"]!!.toLong()

            val account = playerAccountRepository.save(
                PlayerAccount(playerName = name, balance = balance)
            )

            row.keys.filter { it.startsWith("Shares (") }.forEach { key ->
                val symbol = key.substringAfter("(").substringBefore(")")
                val shares = row[key]!!.toLong()
                if (shares > 0) {
                    assetRepository.save(Asset(account = account, symbol = symbol, quantity = shares))
                }
            }
        }
    }

    @Given("{word} has placed an order to buy {long} shares of {word} at a maximum price of {long} with time priority {int}")
    fun playerHasPlacedBuyOrderWithPriority(playerName: String, quantity: Long, symbol: String, price: Long, priority: Int) {
        // We ignore priority for now as it is handled by createdAt in the real system, 
        // but for testing we might need to simulate it if we want exact control.
        // For now, let's just place the order.
        playerHasPlacedBuyOrder(playerName, quantity, symbol, price)
    }

    @Given("{word} has placed an order to buy {long} shares of {word} at a maximum price of {long}")
    fun playerHasPlacedBuyOrder(playerName: String, quantity: Long, symbol: String, price: Long) {
        ensurePlayerExists(playerName)
        val request = PlaceOrderRequest(
            playerName = playerName,
            symbol = symbol,
            quantity = quantity,
            price = price,
            side = OrderSide.BUY
        )
        val response = restTemplate.postForEntity("/api/orders", request, String::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Given("{word} has placed an order to sell {long} shares of {word} at a minimum price of {long} with time priority {int}")
    fun playerHasPlacedSellOrderWithPriority(playerName: String, quantity: Long, symbol: String, price: Long, priority: Int) {
        playerHasPlacedSellOrder(playerName, quantity, symbol, price)
    }

    @Given("{word} has placed an order to sell {long} shares of {word} at a minimum price of {long}")
    fun playerHasPlacedSellOrder(playerName: String, quantity: Long, symbol: String, price: Long) {
        ensurePlayerExists(playerName)
        // Ensure seller has the assets to sell
        val account = playerAccountRepository.findByPlayerName(playerName)!!
        val existingAsset = assetRepository.findByAccountIdAndSymbol(account.id, symbol)
        if (existingAsset == null) {
            assetRepository.save(Asset(account = account, symbol = symbol, quantity = quantity))
        } else if (existingAsset.quantity < quantity) {
            assetRepository.save(existingAsset.copy(quantity = quantity))
        }

        val request = PlaceOrderRequest(
            playerName = playerName,
            symbol = symbol,
            quantity = quantity,
            price = price,
            side = OrderSide.SELL
        )
        val response = restTemplate.postForEntity("/api/orders", request, String::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Then("This order should not be cleared")
    fun orderShouldNotBeCleared() {
        val orders = orderRepository.findAll()
        assertTrue(orders.all { it.status == OrderStatus.OPEN }, "Some orders were cleared but should not be")
    }

    @Then("The order book is empty")
    fun orderBookIsEmpty() {
        val orders = orderRepository.findByStatus(OrderStatus.OPEN)
        assertTrue(orders.isEmpty(), "Order book is not empty: $orders")
    }

    @Then("The order book contains")
    fun orderBookContains(dataTable: DataTable) {
        val expectedRows = dataTable.asMaps()
        val actualOrders = orderRepository.findByStatus(OrderStatus.OPEN)

        assertEquals(expectedRows.size, actualOrders.size, "Order book size mismatch")

        expectedRows.forEach { expected ->
            val playerName = expected["Player"]!!
            val side = OrderSide.valueOf(expected["Side"]!!)
            val symbol = expected["Symbol"]!!
            val price = expected["Price"]!!.toLong()
            val quantity = expected["Quantity"]!!.toLong()

            val actual = actualOrders.find {
                it.playerName == playerName &&
                        it.side == side &&
                        it.symbol == symbol &&
                        it.price == price &&
                        it.remainingQuantity == quantity
            } ?: fail("Order not found: $playerName $side $symbol $price $quantity")
        }
    }

    @Then("The trade book is empty")
    fun tradeBookIsEmpty() {
        val trades = tradeRepository.findAll()
        assertTrue(trades.isEmpty(), "Trade book is not empty")
    }

    @Then("The trade book contains")
    fun tradeBookContains(dataTable: DataTable) {
        val expectedRows = dataTable.asMaps()
        val actualTrades = tradeRepository.findAll()

        assertEquals(expectedRows.size, actualTrades.size, "Trade book size mismatch")

        expectedRows.forEach { expected ->
            val buyer = expected["Buyer"]!!
            val seller = expected["Seller"]!!
            val symbol = expected["Symbol"]!!
            val quantity = expected["Quantity"]!!.toLong()
            val price = expected["Price/Share"]!!.toLong()

            val actual = actualTrades.find {
                it.buyerName == buyer &&
                        it.sellerName == seller &&
                        it.symbol == symbol &&
                        it.quantity == quantity &&
                        it.price == price
            } ?: fail("Trade not found: $buyer $seller $symbol $quantity $price")
        }
    }

    @Then("The account balances and holdings are:")
    fun playerAccountsMatch(dataTable: DataTable) {
        dataTable.asMaps().forEach { row ->
            val name = row["Name"]!!
            val expectedBalance = row["Account Balance"]!!.toLong()

            val account = playerAccountRepository.findByPlayerName(name)
                ?: fail("Account for $name not found")
            
            assertEquals(expectedBalance, account.balance, "Balance mismatch for $name")

            row.keys.filter { it.startsWith("Shares (") }.forEach { key ->
                val symbol = key.substringAfter("(").substringBefore(")")
                val expectedShares = row[key]!!.toLong()
                val asset = assetRepository.findByAccountIdAndSymbol(account.id, symbol)
                assertEquals(expectedShares, asset?.quantity ?: 0L, "Shares mismatch for $symbol of $name")
            }
        }
    }

    private fun ensurePlayerExists(playerName: String) {
        val accounts = playerAccountRepository.findAll()
        if (accounts.none { it.playerName == playerName }) {
            playerAccountRepository.save(PlayerAccount(playerName = playerName, balance = 1000000L))
        }
    }
}
