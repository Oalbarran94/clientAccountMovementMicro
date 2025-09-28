package com.example.accounts.api;


import com.example.accounts.api.dto.account.AccountDetailDto;
import com.example.accounts.api.dto.movement.AccountStatementDto;
import com.example.accounts.domain.dto.ClientResponseDto;
import com.example.accounts.service.AccountService;
import com.example.accounts.service.MovementService;
import com.example.accounts.service.ReportService;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MovementControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private MovementService movementService;

    private WireMockServer wireMockServer;

    @MockBean
    private ReportService reportService;

    private final UUID clientId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private final OffsetDateTime startDate = OffsetDateTime.now().minusDays(30);
    private final OffsetDateTime endDate = OffsetDateTime.now();

    @BeforeEach
    void setup() {

        ClientResponseDto client = new ClientResponseDto(
                clientId,
                "John Doe",
                "123456789",
                true
        );

        AccountDetailDto account = new AccountDetailDto(
                "123456789",
                "SAVINGS",
                new BigDecimal("1500.00"),
                true,
                List.of(),
                new BigDecimal("1500.00")
        );

        AccountStatementDto statement = new AccountStatementDto(
                OffsetDateTime.now(),
                client,
                List.of(account)
        );

        when(reportService.generateAccountStatement(
                Mockito.any(UUID.class),
                Mockito.any(OffsetDateTime.class),
                Mockito.any(OffsetDateTime.class)))
                .thenReturn(CompletableFuture.completedFuture(statement));

        wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
    }

    @AfterEach
    void teardown() {
        wireMockServer.stop();
    }

    @Test
    void shouldGenerateAccountStatement() throws Exception {
        // Given
        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/clientes/" + clientId))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "clientId": "123e4567-e89b-12d3-a456-426614174000",
                                    "name": "John Doe",
                                    "identification": "123456789",
                                    "state": true
                                }
                                """)));

        // When & Then
        MvcResult mvcResult = mockMvc.perform(get("/movimientos/reportes")
                        .param("clientId", clientId.toString())
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.client.name").value("John Doe"))
                .andExpect(jsonPath("$.client.identification").value("123456789"))
                .andExpect(jsonPath("$.accounts[0].accountNumber").value("123456789"))
                .andExpect(jsonPath("$.accounts[0].type").value("SAVINGS"))
                .andExpect(jsonPath("$.accounts[0].initialBalance").value("1500.0"));
    }

    @Test
    void shouldReturnErrorWhenClientNotFound() throws Exception {
        // Given
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/clientes/" + clientId))
                .willReturn(WireMock.aResponse()
                        .withStatus(404)));

        // When & Then
        mockMvc.perform(get("/reportes")
                        .param("clientId", clientId.toString())
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}