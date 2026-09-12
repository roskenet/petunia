package de.roskenet.petunia.exchange.repository

import de.roskenet.petunia.exchange.domain.Trade
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TradeRepository : JpaRepository<Trade, UUID> {
    fun findBySecuritySymbol(symbol: String): List<Trade>
}
