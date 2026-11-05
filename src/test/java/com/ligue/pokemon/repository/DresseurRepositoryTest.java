package com.ligue.pokemon.repository;

import com.ligue.pokemon.entity.Dresseur;
import com.ligue.pokemon.entity.Pokemon;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Charge uniquement la couche JPA + base H2 en mémoire + data.sql
// Chaque test est transactionnel et annulé (rollback) à la fin
@DataJpaTest
class DresseurRepositoryTest {

    @Autowired
    private DresseurRepository dresseurRepository;

    @Test
    void should_load_all_dresseurs_when_data_sql_is_executed() {
        // Arrange : données insérées par data.sql

        // Act
        List<Dresseur> dresseurs = dresseurRepository.findAll();

        // Assert
        assertEquals(2, dresseurs.size());
    }

    @Test
    void should_return_dresseur_with_his_pokemons_when_searching_by_existing_nom() {
        // Arrange
        String nom = "Sacha";

        // Act
        Optional<Dresseur> resultat = dresseurRepository.findByNom(nom);

        // Assert
        assertTrue(resultat.isPresent());
        assertEquals("Kanto", resultat.get().getRegion());
        assertEquals(3, resultat.get().getPokemons().size());
    }

    @Test
    void should_return_empty_when_dresseur_does_not_exist() {
        // Arrange
        Long idInexistant = 999L;

        // Act
        Optional<Dresseur> resultat = dresseurRepository.findById(idInexistant);

        // Assert
        assertFalse(resultat.isPresent());
    }

    @Test
    void should_persist_dresseur_and_his_pokemon_when_saving() {
        // Arrange
        Dresseur ondine = new Dresseur("Ondine", "Kanto");
        ondine.addPokemon(new Pokemon("Stari", "Eau", 20));

        // Act
        Dresseur sauvegarde = dresseurRepository.saveAndFlush(ondine);

        // Assert
        assertNotNull(sauvegarde.getId());
        assertEquals(3, dresseurRepository.count());
        Dresseur relu = dresseurRepository.findById(sauvegarde.getId()).orElseThrow();
        assertEquals("Ondine", relu.getNom());
        assertEquals(1, relu.getPokemons().size());
        assertNotNull(relu.getPokemons().get(0).getId());
    }
}
