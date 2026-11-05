package com.ligue.pokemon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Quand cette exception remonte jusqu'au contrôleur, Spring répond avec ce code HTTP
@ResponseStatus(HttpStatus.NOT_FOUND)
public class DresseurNotFoundException extends RuntimeException {

    public DresseurNotFoundException(Long id) {
        super("Dresseur introuvable : id=" + id);
    }
}
