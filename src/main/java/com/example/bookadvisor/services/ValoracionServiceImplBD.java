package com.example.bookadvisor.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bookadvisor.domain.Usuario;
import com.example.bookadvisor.domain.Valoracion;
import com.example.bookadvisor.repositorio.ValoracionRepository;

@Service
public class ValoracionServiceImplBD implements ValoracionService {

    @Autowired
    private ValoracionRepository valoracionRepository;

    @Override
    public List<Valoracion> getAllValoraciones() {
        return valoracionRepository.findAll();
    }

    @Override
    public Valoracion createValoracion(Valoracion valoracion) {
        Optional<Valoracion> existingValoracion = valoracionRepository.findByUsuarioAndLibro(valoracion.getUsuario(),
                valoracion.getLibro());
        if (existingValoracion.isPresent()) {
            throw new IllegalStateException("El usuario ya ha valorado este libro.");
        }
        return valoracionRepository.save(valoracion);
    }

    @Override
    public Optional<Valoracion> getValoracionById(Long id) {
        return valoracionRepository.findById(id);
    }

    @Override
    public Valoracion updateValoracion(Long id, Valoracion valoracionDetails) {
        Optional<Valoracion> optionalValoracion = valoracionRepository.findById(id);
        if (optionalValoracion.isPresent()) {
            Valoracion valoracion = optionalValoracion.get();
            valoracion.setPuntuacion(valoracionDetails.getPuntuacion());
            valoracion.setComentario(valoracionDetails.getComentario());
            return valoracionRepository.save(valoracion);
        }
        return null;
    }

    // valoracionRepository.deleteById(id);
    @Override
    public void deleteValoracion(Long id, Usuario usuarioActual) {
        Valoracion valoracion = valoracionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La valoración no existe"));

        if (!valoracion.getUsuario().getId().equals(usuarioActual.getId())) {
            throw new SecurityException("No puedes borrar valoraciones de otros usuarios");
        }

        valoracionRepository.deleteById(id);
    }

}
