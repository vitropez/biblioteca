package com.example.bookadvisor.controllers;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.bookadvisor.domain.FormInfo;
import com.example.bookadvisor.domain.Usuario;
import com.example.bookadvisor.services.BookService;
import com.example.bookadvisor.services.UsuarioService;

@Controller
@RequestMapping("/public")
public class PublicController {
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    BookService bookService;

    @GetMapping
    public String showPrincipal(@RequestParam Optional<String> usuario,
            Model model, Authentication authentication) {
        // Comprueba si el usuario está autenticado
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated();
        model.addAttribute("isAuthenticated", isAuthenticated);

        // Solo asigna el nombre si el usuario está autenticado
        String nombre = isAuthenticated ? authentication.getName() : "Invitado";

        model.addAttribute("nombre", nombre);
        model.addAttribute("cliente", usuario.orElse("AMIGO"));
        model.addAttribute("annoActual", LocalDate.now().getYear());
        Usuario usuarioEncontrado = usuarioService.buscarPorName(nombre);
        model.addAttribute("usuarioEditar", usuarioEncontrado);
        return "indexView";
    }

    @GetMapping({ "/informacion" })
    public String showDatos(Model model) {

        return "QuienSomos";
    }

    @GetMapping("/accessError")
    public String showAccessError() {
        return "accessErrorView";
    }

    @ResponseBody
    public ResponseEntity serveFile(@PathVariable String filename) {
        Resource file = bookService.loadAsResource(filename);
        return ResponseEntity.ok().body(file);
    }

    @GetMapping({ "/contacto" })
    public String showContacto(Model model) {
        model.addAttribute("formInfo", new FormInfo());
        return "ContactoView";
    }

    @PostMapping("/myForm/submit")
    public String showMyformSubmit(FormInfo formInfo, Model model) {

        try {

            bookService.enviarEmail(formInfo);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return "indexView";
    }

}
