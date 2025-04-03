package com.example.bookadvisor.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bookadvisor.domain.Libro;
import com.example.bookadvisor.domain.Usuario;
import com.example.bookadvisor.domain.Valoracion;
import com.example.bookadvisor.services.BookService;
import com.example.bookadvisor.services.UsuarioService;
import com.example.bookadvisor.services.ValoracionService;

@Controller
@RequestMapping("/valoraciones")
public class ValoracionController {
    @Autowired
    private ValoracionService valoracionService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BookService libroService;

    @GetMapping
    public String getAllValoraciones(Model model) {
        List<Valoracion> valoraciones = valoracionService.getAllValoraciones();

        model.addAttribute("valoraciones", valoraciones);
        return "valoraciones";
    }

    @GetMapping("/nueva/{id}")
    public String nuevaValoracionForm(Model model, Authentication authentication, @PathVariable Long id) {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(authentication.getName());
        Libro libro = libroService.obtenerPorId(id);
        model.addAttribute("valoracion", new Valoracion());
        model.addAttribute("libro", libro);
        model.addAttribute("usuario", usuarioActual);
        model.addAttribute("libros", libroService.obtenerTodos());
        return "nueva_valoracion";
    }

    @PostMapping("/crearValoracion")
    public String createValoracion(@ModelAttribute Valoracion valoracion,
            @RequestParam Long libroId,
            Authentication authentication) {
        // Obtener el libro usando el ID pasado desde el formulario
        Libro libro = libroService.obtenerPorId(libroId);

        // Asociar el libro y el usuario a la valoración
        valoracion.setLibro(libro);
        valoracion.setUsuario(usuarioService.getUsuarioByNombre(authentication.getName()));

        // Guardar la nueva valoración
        valoracionService.createValoracion(valoracion);

        return "redirect:/valoraciones";
    }

    @PostMapping("/{id}")
    public String borrarValoracion(@PathVariable Long id, Authentication authentication) {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(authentication.getName());

        try {
            // Transformar el rol para hacerlo comparable
            String rol = usuarioActual.getRol().toString().replace("ROLE_", "");

            switch (rol) {
                case "ADMIN": // El usuario es ADMIN
                    valoracionService.deleteValoracion(id, usuarioActual);
                    break;
                case "USER": // El usuario es USER
                    valoracionService.deleteValoracion(id, usuarioActual);
                    break;
                default:
                    throw new SecurityException("No tienes permiso para realizar esta acción");
            }
        } catch (SecurityException e) {
            return "redirec:/public/accesserror";
        }

        return "redirect:/";
    }

}
