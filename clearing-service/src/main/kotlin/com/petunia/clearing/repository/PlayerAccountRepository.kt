package com.petunia.clearing.repository

import com.petunia.clearing.domain.PlayerAccount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PlayerAccountRepository : JpaRepository<PlayerAccount, UUID>
