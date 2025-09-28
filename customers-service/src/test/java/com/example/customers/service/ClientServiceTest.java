package com.example.customers.service;

import com.example.customers.api.dto.ClientDto;
import com.example.customers.domain.Client;
import com.example.customers.domain.Genre;
import com.example.customers.exceptions.ClientCreationException;
import com.example.customers.exceptions.ClientNotFoundException;
import com.example.customers.exceptions.ClientValidationException;
import com.example.customers.mapper.ClientMapper;
import com.example.customers.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class ClientServiceTest {

    @Mock
    private ClientRepository repository;

    @Mock
    private ClientMapper mapper;

    @InjectMocks
    private ClientService service;

    private ClientDto validDto;
    private Client validClient;
    private UUID validId;

    @BeforeEach
    void setUp() {
        validId = UUID.randomUUID();
        validDto = new ClientDto(
                validId,
                "John Doe",
                Genre.MALE,
                25,
                "12345678",
                "123 Main St",
                "+1234567890",
                true
        );
        validClient = Client.builder()
                .clientId(validId)
                .name("John Doe")
                .genre(Genre.MALE)
                .age(25)
                .identification("12345678")
                .address("123 Main St")
                .phone("+1234567890")
                .state(true)
                .build();
    }


    @Test
    @DisplayName("Should create client successfully")
    void shouldCreateClientSuccessfully() {
        when(mapper.toEntity(validDto)).thenReturn(validClient);
        when(repository.save(any(Client.class))).thenReturn(validClient);

        Client result = service.create(validDto);

        assertNotNull(result);
        assertEquals(validDto.name(), result.getName());
        verify(repository).save(any(Client.class));
    }

    @Test
    @DisplayName("Should throw exception when creating client with invalid data")
    void shouldThrowExceptionWhenCreatingInvalidClient() {
        ClientDto invalidDto = new ClientDto(
                null, "", null, null,
                "", "", "", null
        );

        assertThrows(ClientValidationException.class,
                () -> service.create(invalidDto));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when database error occurs")
    void shouldThrowExceptionWhenDatabaseError() {
        when(mapper.toEntity(validDto)).thenReturn(validClient);
        when(repository.save(any(Client.class)))
                .thenThrow(new DataIntegrityViolationException("Database error"));

        assertThrows(ClientCreationException.class,
                () -> service.create(validDto));
    }

    @Test
    @DisplayName("Should update client successfully")
    void shouldUpdateClientSuccessfully() {
        when(repository.findById(validId)).thenReturn(Optional.of(validClient));
        when(repository.save(any(Client.class))).thenReturn(validClient);

        Client result = service.update(validId, validDto);

        assertNotNull(result);
        verify(mapper).updateEntityFromDto(any(), eq(validDto));
        verify(repository).save(any(Client.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent client")
    void shouldThrowExceptionWhenUpdatingNonExistentClient() {
        when(repository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class,
                () -> service.update(UUID.randomUUID(), validDto));
    }

    @Test
    @DisplayName("Should get client by id successfully")
    void shouldGetClientByIdSuccessfully() {
        when(repository.findById(validId)).thenReturn(Optional.of(validClient));
        when(mapper.toDto(validClient)).thenReturn(validDto);

        ClientDto result = service.get(validId);

        assertNotNull(result);
        assertEquals(validDto.name(), result.name());
    }

    @Test
    @DisplayName("Should throw exception when getting non-existent client")
    void shouldThrowExceptionWhenGettingNonExistentClient() {
        when(repository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class,
                () -> service.get(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Should list all clients successfully")
    void shouldListAllClientsSuccessfully() {
        List<Client> clients = Arrays.asList(validClient, validClient);
        when(repository.findAll()).thenReturn(clients);
        when(mapper.toDto(any(Client.class))).thenReturn(validDto);

        List<ClientDto> results = service.list();

        assertNotNull(results);
        assertEquals(2, results.size());
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should delete client successfully")
    void shouldDeleteClientSuccessfully() {
        when(repository.existsById(validId)).thenReturn(true);

        service.delete(validId);

        verify(repository).deleteById(validId);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent client")
    void shouldThrowExceptionWhenDeletingNonExistentClient() {
        when(repository.existsById(any())).thenReturn(false);

        assertThrows(ClientNotFoundException.class,
                () -> service.delete(UUID.randomUUID()));
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should validate all required fields")
    void shouldValidateAllRequiredFields() {
        ClientDto invalidDto = new ClientDto(
                null, null, null, null,
                null, null, null, null
        );

        ClientValidationException exception = assertThrows(
                ClientValidationException.class,
                () -> service.create(invalidDto)
        );

        String errorMessage = exception.getMessage();
        assertThat(errorMessage).contains("Name required");
        assertThat(errorMessage).contains("Genre required");
        assertThat(errorMessage).contains("Age required");
        assertThat(errorMessage).contains("Id required");
        assertThat(errorMessage).contains("Address required");
        assertThat(errorMessage).contains("Phone required");
        assertThat(errorMessage).contains("State required");
    }



}