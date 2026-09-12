package de.roskenet.petunia.security.api

import de.roskenet.petunia.dto.CreateSecurityRequest
import de.roskenet.petunia.dto.SecurityDto
import de.roskenet.petunia.dto.UpdateSecurityRequest
import de.roskenet.petunia.security.domain.Security
import de.roskenet.petunia.security.service.SecurityService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/securities")
class SecurityController(
    private val securityService: SecurityService
) {
    @GetMapping
    fun getAll(): List<SecurityDto> = securityService.getAll().map { it.toDto() }

    @GetMapping("/{symbol}")
    fun getBySymbol(@PathVariable symbol: String): SecurityDto? = securityService.getBySymbol(symbol)?.toDto()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: CreateSecurityRequest): SecurityDto =
        securityService.create(request.symbol, request.name).toDto()

    @PutMapping("/{symbol}")
    fun update(
        @PathVariable symbol: String,
        @RequestBody request: UpdateSecurityRequest
    ): SecurityDto = securityService.update(symbol, request.name).toDto()

    @DeleteMapping("/{symbol}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable symbol: String) {
        securityService.delete(symbol)
    }

    private fun Security.toDto() = SecurityDto(symbol = this.symbol, name = this.name)
}

