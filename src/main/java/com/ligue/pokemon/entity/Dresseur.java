package com.ligue.pokemon.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dresseur")
public class Dresseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String region;

    // Côté "inverse" de la relation : la clé étrangère est portée par Pokemon.dresseur
    @OneToMany(mappedBy = "dresseur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pokemon> pokemons = new ArrayList<>();

    public Dresseur() {}

    public Dresseur(String nom, String region) {
        this.nom = nom;
        this.region = region;
    }


    public void addPokemon(Pokemon pokemon) {
        pokemons.add(pokemon);
        pokemon.setDresseur(this);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public List<Pokemon> getPokemons() { return pokemons; }
    public void setPokemons(List<Pokemon> pokemons) { this.pokemons = pokemons; }
}
