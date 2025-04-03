package com.example.bookadvisor.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.bookadvisor.domain.FormInfo;
import com.example.bookadvisor.domain.Genero;
import com.example.bookadvisor.domain.Libro;
import com.example.bookadvisor.domain.LibroDTO;
import com.example.bookadvisor.repositorio.GeneroRepository;
import com.example.bookadvisor.repositorio.LibroRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class BookServiceImplBD implements BookService {

    // private final List<Libro> repositorio = new ArrayList<>();
    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private GeneroRepository generoRepository;

    @Autowired
    private JavaMailSender mailSender;
    private final Path rootLocation;

    @Autowired
    public BookServiceImplBD(Path rootLocation) {
        this.rootLocation = rootLocation;
    }

    @Override
    public void enviarEmail(FormInfo formInfo) {
        MimeMessageHelper helper;
        String destinatario = "yagoaltafaj@hotmail.com";
        String asunto = "Nuevo contacto desde la web";
        String cuerpoMensaje = "Nombre: " + formInfo.getNombre() + "\n" +

                "Email: " + formInfo.getEmail() + "\n" +

                "Asunto: " + formInfo.getAsunto() + "\n" +
                "Acepto las condiciones: " + formInfo.isAceptoCondiciones() + "\n" +
                "Comentarios: " + formInfo.getTexto();

        MimeMessage message = mailSender.createMimeMessage();
        try {
            helper = new MimeMessageHelper(message, true);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }

        try {
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(cuerpoMensaje, true);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to set email properties", e);
        }

        mailSender.send(message);
    }

    @Override
    public List<Libro> obtenerTodos() {
        try {
            List<Libro> lista = libroRepository.findAll();
            return lista;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener todos los libros", e);
        }
    }

    @Override
    public Libro obtenerPorId(Long id) {

        return libroRepository.findById(id).orElseThrow(() -> new RuntimeException("Libro no encontrado"));
    }

    @Override
    public void añadir(Libro libro) {

        // try {
        // if (libroRepository.existsById((long) libro.getId())) {
        // return null;
        // }
        Genero genero = generoRepository.findById(libro.getGenero().getId())
                .orElseThrow(() -> new RuntimeException("Género no encontrado"));
        libro.setGenero(genero);
        System.out.println(libro);
        libroRepository.save(libro);
        // Collections.sort(repositorio, Comparator.comparing(Libro::getTitulo));
        // return libro; // Podría no devolver nada, o boolean, etc.
        // } catch (Exception e) {
        // throw new RuntimeException("Error al añadir el libro con ID " +
        // libro.getId(), e);
        // }
    }

    @Override
    public Libro editar(Libro libro) {
        try {
            libroRepository.save(libro); // Si lo encuentra, lo sustituye
            return libro;
        } catch (Exception e) {
            throw new RuntimeException("Error al editar el libro con ID " + libro.getId(), e);
        }
    }

    @Override
    public boolean borrar(Long id) {
        try {
            Libro libro = obtenerPorId(id);
            if (libro == null) {
                return false;
            }
            libroRepository.delete(libro);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error al borrar el libro con ID " + id, e);
        }
    }

    @Override
    public List<Libro> buscarPorNombre(String textoNombre) {
        try {
            textoNombre = textoNombre.toLowerCase();
            List<Libro> encontrados = libroRepository.findByTituloContainingIgnoreCase(textoNombre);
            return encontrados;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar libros por nombre: " + textoNombre, e);
        }
    }

    @Override
    public List<Libro> buscarPorGenero(Genero genero) {
        try {
            List<Libro> encontrados = libroRepository.findByGenero(genero);
            return encontrados;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar libros por género: " + genero, e);
        }
    }

    @Override
    public String store(MultipartFile file, String titulo) throws RuntimeException {
        if (file.isEmpty())
            throw new RuntimeException("Fichero vacío");
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (filename.contains("..")) {
            throw new RuntimeException("Fichero incorrecto");
        }
        String extension = StringUtils.getFilenameExtension(filename);
        String storedFilename = titulo + "." + extension;

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, this.rootLocation.resolve(storedFilename),
                    StandardCopyOption.REPLACE_EXISTING);
            return storedFilename;
        } catch (IOException ioe) {
            throw new RuntimeException("Error en escritura");
        }
    }

    @Override
    public void delete(String filename) throws RuntimeException {
        try {
            Path file = rootLocation.resolve(filename);
            if (!Files.exists(file))
                throw new RuntimeException("No existe el fichero");
            Files.delete(file);
        } catch (IOException ioe) {
            throw new RuntimeException("Error en borrado");
        }

    }

    @Override
    public Resource loadAsResource(String filename) throws RuntimeException {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable())
                return resource;
            else
                throw new RuntimeException("Error IO");
        } catch (Exception e) {
            throw new RuntimeException("Error IO");
        }
    }

    @Override
    public void updateLibro(Libro libro) {
        try {
            libroRepository.save(libro);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el libro con ID " + libro.getId(), e);
        }
    }

    @Override
    public List<LibroDTO> convertLibroToDto(List<Libro> listaLibros) {
        try {
            List<LibroDTO> listaDto = new ArrayList<>();
            for (Libro libro : listaLibros) {
                listaDto.add(modelMapper.map(libro, LibroDTO.class));
            }
            return listaDto;
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir libros a DTO", e);
        }
    }

    @Override
    public long contarVotantes(Long libroId) {

        return libroRepository.countVotacionesByLibroId(libroId);

    }

    @Override
    public int sumarPuntos(Long libroId) {

        Integer suma = libroRepository.sumPuntosByLibroId(libroId);
        return (suma != null) ? suma : 0;

    }

    @Override
    public double mediaPuntos(Long libroId) {

        Double media = libroRepository.avgPuntosByLibroId(libroId);
        return (media != null) ? media : 0.0;

    }
}
