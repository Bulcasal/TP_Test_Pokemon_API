package com.ligue.pokemon.controller;

import com.ligue.pokemon.dto.DresseurDto;
import com.ligue.pokemon.exception.DresseurNotFoundException;
import com.ligue.pokemon.service.DresseurService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Charge uniquement la couche web (le contrôleur), pas le service ni la base
@WebMvcTest(DresseurController.class)
class DresseurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Le service est remplacé par un mock dans le contexte Spring
    @MockitoBean
    private DresseurService dresseurService;

    @Test
    void should_return_200_and_dresseur_dto_when_dresseur_exists() throws Exception {
        // Arrange
        DresseurDto dto = new DresseurDto("Sacha", 3, 185);
        when(dresseurService.getDresseur(1L)).thenReturn(dto);

        // Act
        ResultActions resultat = mockMvc.perform(get("/dresseurs/1"));

        // Assert
        resultat.andExpect(status().isOk())
                .andExpect(jsonPath("$.nomDresseur").value("Sacha"))
                .andExpect(jsonPath("$.nombrePokemons").value(3))
                .andExpect(jsonPath("$.niveauDresseur").value(185));
    }

    @Test
    void should_return_404_when_dresseur_does_not_exist() throws Exception {
        // Arrange
        when(dresseurService.getDresseur(99L)).thenThrow(new DresseurNotFoundException(99L));

        // Act
        ResultActions resultat = mockMvc.perform(get("/dresseurs/99"));

        // Assert
        resultat.andExpect(status().isNotFound());
    }
}
