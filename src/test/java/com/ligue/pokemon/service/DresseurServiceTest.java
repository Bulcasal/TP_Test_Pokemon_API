package com.ligue.pokemon.service;

import com.ligue.pokemon.dto.DresseurDto;
import com.ligue.pokemon.entity.Dresseur;
import com.ligue.pokemon.entity.Pokemon;
import com.ligue.pokemon.exception.DresseurNotFoundException;
import com.ligue.pokemon.repository.DresseurRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Test unitaire pur : pas de contexte Spring, pas de base de données
@ExtendWith(MockitoExtension.class)
class DresseurServiceTest {

    @Mock
    private DresseurRepository dresseurRepository;

    @InjectMocks
    private DresseurService dresseurService;

    // Petite méthode utilitaire pour construire un dresseur de test
    private Dresseur dresseurAvec(Pokemon... pokemons) {
        Dresseur dresseur = new Dresseur("Sacha", "Kanto");
        dresseur.setId(1L);
        for (Pokemon p : pokemons) {
            dresseur.addPokemon(p);
        }
        return dresseur;
    }

    @Test
    void should_return_sum_of_levels_when_no_bonus_applies() {
        // Arrange : 2 types seulement, aucun niveau >= 50
        Dresseur dresseur = dresseurAvec(
                new Pokemon("Pikachu", "Électrik", 25),
                new Pokemon("Salamèche", "Feu", 30));
        when(dresseurRepository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert : 25 + 30
        assertEquals(55, niveau);
        verify(dresseurRepository).findById(1L);
    }

    @Test
    void should_return_1_when_dresseur_has_no_pokemon() {
        // Arrange
        Dresseur dresseur = dresseurAvec();
        when(dresseurRepository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(1, niveau);
    }

    @Test
    void should_apply_diversity_bonus_when_dresseur_has_3_different_types() {
        // Arrange : 3 types distincts, aucun élite
        Dresseur dresseur = dresseurAvec(
                new Pokemon("Pikachu", "Électrik", 10),
                new Pokemon("Salamèche", "Feu", 20),
                new Pokemon("Carapuce", "Eau", 30));
        when(dresseurRepository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert : 60 + 20 (diversité)
        assertEquals(80, niveau);
    }

    @Test
    void should_apply_elite_bonus_for_each_pokemon_when_level_is_at_least_50() {
        // Arrange : 2 élites (dont un à 50 pile), même type -> pas de diversité
        Dresseur dresseur = dresseurAvec(
                new Pokemon("Dracaufeu", "Feu", 50),
                new Pokemon("Arcanin", "Feu", 60),
                new Pokemon("Goupix", "Feu", 49));
        when(dresseurRepository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert : 159 + 2 x 10 (élites)
        assertEquals(179, niveau);
    }

    @Test
    void should_cumulate_diversity_and_elite_bonus_when_both_conditions_are_met() {
        // Arrange : 3 types distincts + 2 élites
        Dresseur dresseur = dresseurAvec(
                new Pokemon("Pikachu", "Électrik", 30),
                new Pokemon("Dracaufeu", "Feu", 60),
                new Pokemon("Tortank", "Eau", 55));
        when(dresseurRepository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert : 145 + 20 (diversité) + 2 x 10 (élites)
        assertEquals(185, niveau);
    }

    @Test
    void should_throw_exception_when_dresseur_does_not_exist() {
        // Arrange
        when(dresseurRepository.findById(99L)).thenReturn(Optional.empty());

        // Act : l'appel est préparé ici, il sera exécuté par assertThrows
        Executable appel = () -> dresseurService.calculerNiveauDresseur(99L);

        // Assert
        assertThrows(DresseurNotFoundException.class, appel);
    }

    @Test
    void should_build_dto_with_name_count_and_level_when_dresseur_exists() {
        // Arrange
        Dresseur dresseur = dresseurAvec(
                new Pokemon("Pikachu", "Électrik", 30),
                new Pokemon("Dracaufeu", "Feu", 60),
                new Pokemon("Tortank", "Eau", 55));
        when(dresseurRepository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        DresseurDto dto = dresseurService.getDresseur(1L);

        // Assert
        assertEquals("Sacha", dto.nomDresseur());
        assertEquals(3, dto.nombrePokemons());
        assertEquals(185, dto.niveauDresseur());
    }
}
