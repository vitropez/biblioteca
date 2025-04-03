package com.example.bookadvisor.services;

import java.util.List;
import java.util.Optional;

import com.example.bookadvisor.domain.Usuario;
import com.example.bookadvisor.domain.Valoracion;

public interface ValoracionService {

    // Obtener todas las valoraciones
    List<Valoracion> getAllValoraciones();

    // Crear una nueva valoración
    Valoracion createValoracion(Valoracion valoracion);

    // Obtener una valoración por ID
    Optional<Valoracion> getValoracionById(Long id);

    // Actualizar una valoración existente
    Valoracion updateValoracion(Long id, Valoracion valoracionDetails);

    // Eliminar una valoración
    void deleteValoracion(Long id, Usuario usuarioActual);
}
