package de.roskenet.petunia.security.service

import de.roskenet.petunia.security.domain.Security
import de.roskenet.petunia.security.repository.SecurityRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SecurityService(
    private val securityRepository: SecurityRepository
) {
    @Transactional(readOnly = true)
    fun getAll(): List<Security> = securityRepository.findAll()

    @Transactional(readOnly = true)
    fun getBySymbol(symbol: String): Security? = securityRepository.findById(symbol).orElse(null)

    @Transactional(readOnly = true)
    fun requireBySymbol(symbol: String): Security =
        getBySymbol(symbol) ?: throw IllegalArgumentException("Security not found: $symbol")

    @Transactional
    fun create(symbol: String, name: String): Security {
        if (securityRepository.existsById(symbol)) {
            throw IllegalArgumentException("Security already exists: $symbol")
        }
        return securityRepository.save(Security(symbol = symbol, name = name))
    }

    @Transactional
    fun update(symbol: String, name: String): Security {
        val security = requireBySymbol(symbol)
        return securityRepository.save(security.copy(name = name))
    }

    @Transactional
    fun delete(symbol: String) {
        if (!securityRepository.existsById(symbol)) {
            throw IllegalArgumentException("Security not found: $symbol")
        }
        securityRepository.deleteById(symbol)
    }
}

