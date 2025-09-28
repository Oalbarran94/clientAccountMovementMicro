package com.example.accounts.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "movimientos", indexes = {
        @Index(name = "idx_mov_cuenta", columnList = "cuenta_id"),
        @Index(name = "idx_mov_fecha", columnList = "fecha")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movement {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Account account;

    @Column(name = "fecha", nullable = false)
    private OffsetDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false)
    private MovementType movementType;

    @Column(name = "valor", nullable = false)
    private BigDecimal value;

    @Column(name = "saldo", nullable = false)
    private BigDecimal balance;
}
