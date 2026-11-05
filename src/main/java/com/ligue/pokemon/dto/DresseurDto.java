package com.ligue.pokemon.dto;

// Objet renvoyé par l'API : on n'expose pas l'entité JPA directement
public record DresseurDto(String nomDresseur, int nombrePokemons, int niveauDresseur) {
}
