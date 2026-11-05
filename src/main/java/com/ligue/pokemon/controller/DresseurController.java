package com.ligue.pokemon.controller;

import com.ligue.pokemon.dto.DresseurDto;
import com.ligue.pokemon.service.DresseurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dresseurs")
public class DresseurController {

    private final DresseurService dresseurService;

    public DresseurController(DresseurService dresseurService) {
        this.dresseurService = dresseurService;
    }

    // GET /dresseurs/{id} -> 200 + DresseurDto, ou 404 si le dresseur n'existe pas
    @GetMapping("/{id}")
    public ResponseEntity<DresseurDto> getDresseur(@PathVariable Long id) {
        return ResponseEntity.ok(dresseurService.getDresseur(id));
    }
}
