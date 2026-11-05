package com.ligue.pokemon.service;

import com.ligue.pokemon.dto.DresseurDto;
import com.ligue.pokemon.entity.Dresseur;
import com.ligue.pokemon.entity.Pokemon;
import com.ligue.pokemon.exception.DresseurNotFoundException;
import com.ligue.pokemon.repository.DresseurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DresseurService {

    static final int NIVEAU_SANS_POKEMON = 1;
    static final int SEUIL_TYPES_DIVERSITE = 3;
    static final int BONUS_DIVERSITE = 20;
    static final int SEUIL_ELITE = 50;
    static final int BONUS_ELITE = 10;

    private final DresseurRepository dresseurRepository;

    public DresseurService(DresseurRepository dresseurRepository) {
        this.dresseurRepository = dresseurRepository;
    }

    @Transactional(readOnly = true)
    public int calculerNiveauDresseur(Long dresseurId) {
        Dresseur dresseur = trouverDresseur(dresseurId);
        return calculerNiveau(dresseur.getPokemons());
    }

    @Transactional(readOnly = true)
    public DresseurDto getDresseur(Long dresseurId) {
        Dresseur dresseur = trouverDresseur(dresseurId);
        List<Pokemon> pokemons = dresseur.getPokemons();
        return new DresseurDto(dresseur.getNom(), pokemons.size(), calculerNiveau(pokemons));
    }

    // Vérifie que le dresseur existe, sinon lève une exception (-> code HTTP d'erreur)
    private Dresseur trouverDresseur(Long dresseurId) {
        return dresseurRepository.findById(dresseurId)
                .orElseThrow(() -> new DresseurNotFoundException(dresseurId));
    }

    private int calculerNiveau(List<Pokemon> pokemons) {
        // Cas limite : aucun Pokémon -> niveau 1
        if (pokemons == null || pokemons.isEmpty()) {
            return NIVEAU_SANS_POKEMON;
        }

        // Base : somme des niveaux
        int niveau = pokemons.stream().mapToInt(Pokemon::getNiveau).sum();

        // Bonus diversité : au moins 3 types distincts -> +20 (une seule fois)
        long typesDistincts = pokemons.stream().map(Pokemon::getType).distinct().count();
        if (typesDistincts >= SEUIL_TYPES_DIVERSITE) {
            niveau += BONUS_DIVERSITE;
        }

        // Bonus élite : +10 pour CHAQUE Pokémon de niveau >= 50
        long nombreElites = pokemons.stream().filter(p -> p.getNiveau() >= SEUIL_ELITE).count();
        niveau += (int) nombreElites * BONUS_ELITE;

        return niveau;
    }
}
