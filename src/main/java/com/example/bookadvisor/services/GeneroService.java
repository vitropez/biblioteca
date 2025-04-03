package com.example.bookadvisor.services;

import java.util.List;
import java.util.Optional;

import com.example.bookadvisor.domain.Genero;

public interface GeneroService {

    // Obtener todos los géneros
    List<Genero> getAllGeneros();

    // Crear un nuevo género
    Genero createGenero(Genero genero);

    // Obtener un género por ID
    Optional<Genero> getGeneroById(Long id);

    // Actualizar un género existente
    Genero updateGenero(Long id, Genero generoDetails);

    // Eliminar un género
    void deleteGenero(Long id);
}
