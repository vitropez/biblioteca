package com.example.bookadvisor.repositorio;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bookadvisor.domain.Genero;

public interface GeneroRepository extends  JpaRepository<Genero, Long>{
   
}
