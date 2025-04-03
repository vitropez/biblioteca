package com.example.bookadvisor.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bookadvisor.domain.Libro;
import com.example.bookadvisor.domain.Usuario;
import com.example.bookadvisor.domain.Valoracion;

public interface ValoracionRepository extends  JpaRepository<Valoracion, Long>{
    Optional<Valoracion> findByUsuarioAndLibro(Usuario usuario, Libro libro); 
    Optional<Valoracion> findById(Long id);

}
