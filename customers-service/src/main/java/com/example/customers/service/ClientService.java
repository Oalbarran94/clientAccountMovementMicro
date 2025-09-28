package com.example.customers.service;

import com.example.customers.api.dto.ClientDto;
import com.example.customers.domain.Client;
import com.example.customers.exceptions.ClientCreationException;
import com.example.customers.exceptions.ClientNotFoundException;
import com.example.customers.exceptions.ClientValidationException;
import com.example.customers.mapper.ClientMapper;
import com.example.customers.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository repository;
    private final ClientMapper mapper;

    @Transactional
    public Client create(ClientDto dto) {
        log.info("Creating client: {}", dto);
        try {
            validateDto(dto);
            Client entity = mapper.toEntity(dto);
            entity.setClientId(dto.clientId() == null ? UUID.randomUUID() : dto.clientId());
            return repository.save(entity);
        } catch (DataIntegrityViolationException e) {
            log.error("Error creating client: {}", e.getMessage());
            throw new ClientCreationException("Error creating client: " + e.getMessage());
        }
    }

    @Transactional
    public Client update(UUID id, ClientDto dto) {
        log.info("Updating client with id: {}", id);
        validateDto(dto);
        Client existing = repository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));
        mapper.updateEntityFromDto(existing, dto);
        return repository.save(existing);
    }

    @Transactional(readOnly = true)
    public ClientDto get(UUID id) {
        log.info("Retrieving client with id: {}", id);
        Client client = repository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
        return mapper.toDto(client);
    }


    @Transactional(readOnly = true)
    public List<ClientDto> list() {
        log.info("Retrieving all clients");
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(UUID id) {
        log.info("Deleting client with id: {}", id);
        if (!repository.existsById(id)) {
            throw new ClientNotFoundException(id);
        }
        repository.deleteById(id);
    }

    private void validateDto(ClientDto dto) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {
            throw new ClientValidationException("Client DTO cannot be empty");
        }

        if (dto.name() == null || dto.name().trim().isEmpty()) {
            errors.add("Name required");
        } else if (dto.name().trim().length() < 2 || dto.name().trim().length() > 50) {
            errors.add("Name must be between  2 and 100 characters");
        }

        if (dto.genre() == null) {
            errors.add("Genre required");
        }

        if (dto.age() == null) {
            errors.add("Age required");
        } else if (dto.age() < 18 || dto.age() > 65) {
            errors.add("Invalid age");
        }

        if (dto.identification() == null || dto.identification().trim().isEmpty()) {
            errors.add("Id required");
        }

        if (dto.address() == null || dto.address().trim().isEmpty()) {
            errors.add("Address required");
        }

        if (dto.phone() == null || dto.phone().trim().isEmpty()) {
            errors.add("Phone required");
        }

        if (dto.state() == null) {
            errors.add("State required");
        }

        if (!errors.isEmpty()) {
            throw new ClientValidationException("Validation errors: " + String.join("; ", errors));
        }
    }
}
