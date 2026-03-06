package de.roskenet.petunia.bank.api

import de.roskenet.petunia.dto.CreatePlayerAccountRequest
import de.roskenet.petunia.dto.PlayerAccountDto
import de.roskenet.petunia.bank.service.ClearingService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/accounts")
class PlayerAccountController(
    private val clearingService: ClearingService
) {
    @GetMapping
    fun getAllAccounts(): List<PlayerAccountDto> =
        clearingService.getAllAccounts().map {
            PlayerAccountDto(it.id, it.playerName, it.balance)
        }

    @GetMapping("/{playerName}")
    fun getAccountByPlayerName(@PathVariable playerName: String): PlayerAccountDto? =
        clearingService.getAccountByPlayerName(playerName)?.let {
            PlayerAccountDto(it.id, it.playerName, it.balance)
        }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAccount(@RequestBody request: CreatePlayerAccountRequest): PlayerAccountDto {
        val account = clearingService.createAccount(request.playerName, request.initialBalance)
        return PlayerAccountDto(account.id, account.playerName, account.balance)
    }

    @DeleteMapping("/{playerName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAccount(@PathVariable playerName: String) {
        clearingService.deleteAccountByPlayerName(playerName)
    }
}
