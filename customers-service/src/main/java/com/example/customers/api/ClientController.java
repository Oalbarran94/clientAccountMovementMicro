package com.example.customers.api;

import com.example.customers.api.dto.ClientDto;
import com.example.customers.domain.Client;
import com.example.customers.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClientController {
    private final ClientService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Client create(@Valid @RequestBody ClientDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public Client update(@PathVariable("id") UUID id, @Valid @RequestBody ClientDto dto) {
        return service.update(id, dto);
    }

    @GetMapping("/{id}")
    public ClientDto get(@PathVariable("id") UUID id) {
        log.info("Retrieving client with id: {}", id);
        return service.get(id);
    }

    @GetMapping
    public List<ClientDto> list() {
        return service.list();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }
}
