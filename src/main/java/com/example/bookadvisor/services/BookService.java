package com.example.bookadvisor.services;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.bookadvisor.domain.FormInfo;
import com.example.bookadvisor.domain.Genero;
import com.example.bookadvisor.domain.Libro;
import com.example.bookadvisor.domain.LibroDTO;

@Service

public interface BookService {
    public void enviarEmail(FormInfo formInfo);

    public List<Libro> obtenerTodos();

    public void añadir(Libro libro);

    public Libro editar(Libro libro);

    public boolean borrar(Long id);

    public List<Libro> buscarPorNombre(String textoNombre);

    public List<Libro> buscarPorGenero(Genero genero);

    public String store(MultipartFile file, String titulo);

    public Libro obtenerPorId(Long id);

    public void delete(String filename);

    public Resource loadAsResource(String filename);

    public void updateLibro(Libro libro);

    public List<LibroDTO> convertLibroToDto(List<Libro> listaLibros);

    public long contarVotantes(Long libroId);

    public int sumarPuntos(Long libroId);

    public double mediaPuntos(Long libroId);
}
