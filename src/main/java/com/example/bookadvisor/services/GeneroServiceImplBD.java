package com.example.bookadvisor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bookadvisor.domain.Genero;
import com.example.bookadvisor.repositorio.GeneroRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GeneroServiceImplBD implements GeneroService {

    @Autowired
    private GeneroRepository generoRepository;

    @Override
    public List<Genero> getAllGeneros() {
        return generoRepository.findAll();
    }

    @Override
    public Genero createGenero(Genero genero) {
        return generoRepository.save(genero);
    }

    @Override
    public Optional<Genero> getGeneroById(Long id) {
        return generoRepository.findById(id);
    }

    @Override
    public Genero updateGenero(Long id, Genero generoDetails) {
        Optional<Genero> optionalGenero = generoRepository.findById(id);
        if (optionalGenero.isPresent()) {
            Genero genero = optionalGenero.get();
            genero.setNombre(generoDetails.getNombre());
            return generoRepository.save(genero);
        }
        return null;
    }

    @Override
    public void deleteGenero(Long id) {
        generoRepository.deleteById(id);
    }
}
