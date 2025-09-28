package com.example.customers.mapper;

import com.example.customers.api.dto.ClientDto;
import com.example.customers.domain.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {
    public Client toEntity(ClientDto dto) {
        return Client.builder()
                .name(dto.name())
                .genre(dto.genre())
                .age(dto.age())
                .identification(dto.identification())
                .address(dto.address())
                .phone(dto.phone())
                .state(dto.state())
                .build();
    }

    public ClientDto toDto(Client entity) {
        return new ClientDto(
                entity.getClientId(),
                entity.getName(),
                entity.getGenre(),
                entity.getAge(),
                entity.getIdentification(),
                entity.getAddress(),
                entity.getPhone(),
                entity.getState()
        );
    }

    public void updateEntityFromDto(Client entity, ClientDto dto) {
        entity.setName(dto.name());
        entity.setGenre(dto.genre());
        entity.setAge(dto.age());
        entity.setIdentification(dto.identification());
        entity.setAddress(dto.address());
        entity.setPhone(dto.phone());
        entity.setState(dto.state());
    }
}
