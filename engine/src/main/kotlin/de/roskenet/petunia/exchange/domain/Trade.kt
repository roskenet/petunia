package de.roskenet.petunia.exchange.domain

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "trades")
class Trade(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(name = "buyer_name", nullable = false)
    val buyerName: String,

    @Column(name = "seller_name", nullable = false)
    val sellerName: String,

    @Column(name = "symbol", nullable = false)
    val symbol: String,

    @Column(name = "quantity", nullable = false)
    val quantity: Long,

    @Column(name = "price", nullable = false)
    val price: Long,

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    val createdAt: OffsetDateTime? = null
)
