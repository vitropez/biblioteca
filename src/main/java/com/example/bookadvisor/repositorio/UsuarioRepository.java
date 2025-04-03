package com.example.bookadvisor.repositorio;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.bookadvisor.domain.Usuario;

public interface UsuarioRepository extends  JpaRepository<Usuario, Long>{
List<Usuario> findByNombreContainingIgnoreCase(String nombre);
Usuario findByNombre(String nombre);
Usuario findByEmail(String email);


}
