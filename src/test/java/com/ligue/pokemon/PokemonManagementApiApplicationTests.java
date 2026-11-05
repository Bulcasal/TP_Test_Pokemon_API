package com.ligue.pokemon;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PokemonManagementApiApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void should_load_spring_context_when_application_starts() {
        // Arrange

        // Act
        Object controller = context.getBean("dresseurController");

        // Assert
        assertNotNull(controller);
    }
}