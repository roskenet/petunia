package de.roskenet.petunia.bank.domain

import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "asset")
data class Asset(
    @Id
    val id: UUID = UUID.randomUUID(),
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    val account: PlayerAccount,
    @Column(nullable = false, length = 10)
    val symbol: String,
    @Column(nullable = false)
    val quantity: Long = 0,
    @Column(name = "created_at", insertable = false, updatable = false)
    val createdAt: OffsetDateTime? = null,
    @Column(name = "updated_at", insertable = false, updatable = false)
    val updatedAt: OffsetDateTime? = null
)
