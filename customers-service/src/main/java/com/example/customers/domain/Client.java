package com.example.customers.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Client extends Person {

    @Id
    @UuidGenerator
    @Column(name = "cliente_id", nullable = false, updatable = false)
    private UUID clientId;

    @Column(nullable = false)
    private Boolean state;
}
