package com.projectpetunia.bank.repository

import com.projectpetunia.bank.domain.PlayerAccount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PlayerAccountRepository : JpaRepository<PlayerAccount, UUID> {
    fun findByPlayerName(playerName: String): PlayerAccount?
}
