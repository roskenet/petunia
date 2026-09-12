package de.roskenet.petunia.security.repository

import de.roskenet.petunia.security.domain.Security
import org.springframework.data.jpa.repository.JpaRepository

interface SecurityRepository : JpaRepository<Security, String>

