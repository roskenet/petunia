package de.roskenet.petunia.exchange.domain

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

    @Column(name = "player_name", nullable = false)
    val playerName: String,

    @Column(name = "symbol", nullable = false)
    val symbol: String,

    @Column(name = "quantity", nullable = false)
    val quantity: Long,

    @Column(name = "remaining_quantity", nullable = false)
    var remainingQuantity: Long,

    @Column(name = "price", nullable = false)
    val price: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "side", nullable = false)
    val side: OrderSide,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: OrderStatus = OrderStatus.OPEN,

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    val createdAt: OffsetDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at")
    val updatedAt: OffsetDateTime? = null
)
