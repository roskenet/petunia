package de.roskenet.petunia.exchange.domain

import de.roskenet.petunia.exchange.dto.OrderSide
import de.roskenet.petunia.exchange.dto.OrderType
import de.roskenet.petunia.security.domain.Security
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "orders")
class Order(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(name = "player_id", nullable = false)
    val playerId: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "symbol", referencedColumnName = "symbol", nullable = false)
    val security: Security,

    @Column(name = "quantity", nullable = false)
    var quantity: Long,

    @Column(name = "price", nullable = false)
    val price: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "side", nullable = false)
    val side: OrderSide,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    val type: OrderType,

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    val createdAt: OffsetDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at")
    val updatedAt: OffsetDateTime? = null
) {
    val symbol: String
        get() = security.symbol
}
