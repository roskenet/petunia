package de.roskenet.petunia.bank.api

import de.roskenet.petunia.dto.CreatePlayerAccountRequest
import de.roskenet.petunia.dto.PlayerAccountDto
import de.roskenet.petunia.dto.UpdatePlayerAccountRequest
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
        val account = clearingService.createAccount(playerSubject = request.playerSubject, request.playerName)
        return PlayerAccountDto(account.id, account.playerName, account.balance)
    }

    @PutMapping("/{playerName}")
    fun updateAccount(
        @PathVariable playerName: String,
        @RequestBody request: UpdatePlayerAccountRequest
    ): PlayerAccountDto {
        val account = clearingService.updateAccount(playerName, request.playerName, request.balance)
        return PlayerAccountDto(account.id, account.playerName, account.balance)
    }

    @DeleteMapping("/{playerName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAccount(@PathVariable playerName: String) {
        clearingService.deleteAccountByPlayerName(playerName)
    }
}
