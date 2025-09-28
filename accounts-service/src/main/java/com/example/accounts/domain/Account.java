package com.example.accounts.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "cuentas", indexes = {
        @Index(name = "idx_cuenta_numero", columnList = "numero_cuenta", unique = true),
        @Index(name = "idx_cuenta_cliente", columnList = "cliente_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "numero_cuenta", nullable = false, unique = true)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cuenta", nullable = false)
    private AccountType accountType;

    @Column(name = "saldo_inicial", nullable = false)
    private BigDecimal initialBalance;

    @Column(name = "estado", nullable = false)
    private Boolean state;

    @NotNull
    @Column(name = "cliente_id", nullable = false)
    private UUID clientId;
}
