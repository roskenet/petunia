package de.roskenet.petunia.bank.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "game")
data class GameProperties(
    val account: Account = Account()
) {
    data class Account(
        val initialBalance: Long = 0L
    )
}
