package de.roskenet.petunia.bank.domain

import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "player_account")
data class PlayerAccount(
    @Id
    val id: UUID = UUID.randomUUID(),
    @Column(name = "player_name", nullable = false)
    val playerName: String,
    @Column(nullable = false)
    val balance: Long = 0L,
    @Column(name = "created_at", insertable = false, updatable = false)
    val createdAt: OffsetDateTime? = null,
    @Column(name = "updated_at", insertable = false, updatable = false)
    val updatedAt: OffsetDateTime? = null
)
