package de.roskenet.petunia.security.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.OffsetDateTime

@Entity
@Table(name = "securities")
data class Security(
    @Id
    @Column(nullable = false, length = 10)
    val symbol: String,
    @Column(nullable = false)
    val name: String,
    @Column(name = "created_at", insertable = false, updatable = false)
    val createdAt: OffsetDateTime? = null,
    @Column(name = "updated_at", insertable = false, updatable = false)
    val updatedAt: OffsetDateTime? = null
)

