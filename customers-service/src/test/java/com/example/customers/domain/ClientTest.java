package com.example.customers.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
    @Test
    void createClientAndCheckFields() {
        Client c = new Client();
        c.setName("Juan");
        c.setGenre(Genre.MALE);
        c.setAge(30);
        c.setIdentification("123");
        c.setAddress("Calle 1");
        c.setPhone("555-1234");
        c.setState(true);

        assertEquals("Juan", c.getName());
        assertTrue(c.getState());
        assertEquals(Genre.MALE, c.getGenre());
    }
}
