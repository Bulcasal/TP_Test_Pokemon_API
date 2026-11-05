package com.ligue.pokemon.repository;

import com.ligue.pokemon.entity.Dresseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DresseurRepository extends JpaRepository<Dresseur, Long> {

    //= SELECT d FROM Dresseur d WHERE d.nom = ?1
    Optional<Dresseur> findByNom(String nom);
}
