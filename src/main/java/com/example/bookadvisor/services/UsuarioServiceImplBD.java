package com.example.bookadvisor.services;

/*import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;*/
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.bookadvisor.domain.FormInfo;
import com.example.bookadvisor.domain.Usuario;
import com.example.bookadvisor.domain.UsuarioDTO;
import com.example.bookadvisor.repositorio.UsuarioRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class UsuarioServiceImplBD implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    /*
     * * @Autowired
     * private final Path rootLocation = Paths.get("uploadFotos");
     */

    @Autowired
    private ModelMapper modelMapper;
    // private final Path rootLocation = Paths.get("uploadFotos");

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
    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = usuarioRepository.findAll();
        return lista;

    }

    @Override
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    /*
     * for (Usuario usuario : usuarioRepository.findAll()) {
     * if (usuario.getId() == id) {
     * 
     * return usuario;
     * }
     * }
     * throw new RuntimeException(); // Lanzar RuntimeException si no encontrado
     * }
     */

    @Override
    public Usuario añadir(Usuario usuario) {

        // Collections.sort(repositorio, Comparator.comparing(Libro::getTitulo));
        if (usuarioRepository.findByNombre(usuario.getNombre()) != null)
            return null; // ya existe ese nombre de usuario
        String passCrypted = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passCrypted);
        try {
            return usuarioRepository.save(usuario);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Usuario editar(Usuario usuario) {
        Usuario usuarioEncontrado = usuarioRepository.findById(usuario.getId()).orElse(null);
        if (usuarioEncontrado == null) {
            return null;
        }
        String passCrypted = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passCrypted);
        usuarioRepository.save(usuarioEncontrado); // Si lo encuentra, lo sustituye
        return usuario;
    }

    @Override
    public boolean borrar(Long id) {
        Usuario usuario = obtenerPorId(id);
        if (usuario == null) {
            return false;
        }
        usuarioRepository.delete(usuario);
        return true;

    }

    @Override
    public List<Usuario> buscarPorNombre(String textoNombre) {
        textoNombre = textoNombre.toLowerCase();
        List<Usuario> encontrados = usuarioRepository.findByNombreContainingIgnoreCase(textoNombre);

        return encontrados;
    }

    /*
     * @Override
     * public String store(MultipartFile file, String titulo) throws
     * RuntimeException {
     * if (file.isEmpty())
     * throw new RuntimeException("Fichero vacío");
     * String filename = StringUtils.cleanPath(file.getOriginalFilename());
     * if (filename.contains("..")) {
     * throw new RuntimeException("Fichero incorrecto");
     * }
     * String extension = StringUtils.getFilenameExtension(filename);
     * String storedFilename = titulo + "." + extension;
     * 
     * try (InputStream inputStream = file.getInputStream()) {
     * Files.copy(inputStream, this.rootLocation.resolve(storedFilename),
     * StandardCopyOption.REPLACE_EXISTING);
     * return storedFilename;
     * } catch (IOException ioe) {
     * throw new RuntimeException("Error en escritura");
     * }
     * }
     */

    /*
     * @Override
     * public void delete(String filename) throws RuntimeException {
     * try {
     * Path file = rootLocation.resolve(filename);
     * if (!Files.exists(file))
     * throw new RuntimeException("No existe el fichero");
     * Files.delete(file);
     * } catch (IOException ioe) {
     * throw new RuntimeException("Error en borrado");
     * }
     * 
     * }
     */

    /*
     * @Override
     * public Resource loadAsResource(String filename) throws RuntimeException {
     * try {
     * Path file = rootLocation.resolve(filename);
     * Resource resource = new UrlResource(file.toUri());
     * if (resource.exists() || resource.isReadable())
     * return resource;
     * else
     * throw new RuntimeException("Error IO");
     * } catch (Exception e) {
     * throw new RuntimeException("Error IO");
     * }
     * }
     */

    @Override
    public void updateUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    @Override
    public Usuario getUsuarioByNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre);
    }

    @Override
    public UsuarioDTO convertUsuarioToDto(Usuario usuario) {
        try {
            return modelMapper.map(usuario, UsuarioDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir usuario a DTO", e);
        }
    }

    @Override
    public Usuario buscarPorName(String nombre) {
        return usuarioRepository.findByNombre(nombre);
    }

}
