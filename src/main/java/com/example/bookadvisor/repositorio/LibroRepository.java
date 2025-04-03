package com.example.bookadvisor.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bookadvisor.domain.Genero;
import com.example.bookadvisor.domain.Libro;
public interface LibroRepository extends JpaRepository<Libro, Long> {
   
    List<Libro> findByTituloContainingIgnoreCase(String nombre);
    List<Libro> findByGenero(Genero genero);
    @Override
    Optional<Libro> findById(Long id);

    
    @Query("SELECT COALESCE(COUNT(v), 0) FROM Valoracion v WHERE v.libro.id = :libroId")
    long countVotacionesByLibroId(@Param("libroId") Long libroId);
    
    @Query("SELECT COALESCE(SUM(v.puntuacion), 0) FROM Valoracion v WHERE v.libro.id = :libroId")
    int sumPuntosByLibroId(@Param("libroId") Long libroId);
    
    @Query("SELECT COALESCE(AVG(v.puntuacion), 0.0) FROM Valoracion v WHERE v.libro.id = :libroId")
    double avgPuntosByLibroId(@Param("libroId") Long libroId);
    
}
