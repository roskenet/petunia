package de.roskenet.petunia.exchange.domain

import de.roskenet.petunia.security.domain.Security
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "symbol", referencedColumnName = "symbol", nullable = false)
    val security: Security,

    @Column(name = "quantity", nullable = false)
    val quantity: Long,

    @Column(name = "price", nullable = false)
    val price: Long,

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    val createdAt: OffsetDateTime? = null
) {
    val symbol: String
        get() = security.symbol
}
