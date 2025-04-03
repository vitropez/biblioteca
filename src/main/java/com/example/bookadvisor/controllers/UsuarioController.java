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

import com.example.bookadvisor.domain.Rol;
import com.example.bookadvisor.domain.Usuario;
import com.example.bookadvisor.domain.UsuarioDTO;
import com.example.bookadvisor.services.UsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.obtenerTodos();

        model.addAttribute("usuarios", usuarios);
        return "listaUsuarios";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeCreacion(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", Rol.values());
        return "formularioUsuario";
    }

    @PostMapping
    public String crearUsuario(@ModelAttribute Usuario usuario) {
        usuarioService.añadir(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEdicion(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", Rol.values());
        return "formularioUsuario";
    }

    @PostMapping("/editar")
    public String editarUsuario(@ModelAttribute Usuario usuario) {
        usuarioService.editar(usuario);
        return "redirect:/usuarios";
    }

    @PostMapping("/borrar/{id}")
    public String borrarUsuario(@PathVariable Long id) {
        usuarioService.borrar(id);
        return "redirect:/usuarios";
    }

    @GetMapping("/cambiarContrasena")
    public String showRegister(Model model, Authentication authentication) {

        model.addAttribute("usuario", new UsuarioDTO());
        model.addAttribute("nombre", authentication.getName());
        return "register";
    }

    @PostMapping("/cambiarContrasena")
    public String register(@ModelAttribute UsuarioDTO usuarioDTO, Model model) {
        try {

            Usuario usuario = usuarioService.buscarPorName(usuarioDTO.getNombre());

            usuario.setPassword(usuarioDTO.getPassword());
            usuarioService.editar(usuario);

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
        return "redirect:/usuarios";
    }

    @PostMapping("/submit")
    public String crearUsuarioAutoregistro(@ModelAttribute Usuario usuario) {
        usuario.setRol(Rol.USER);
        usuarioService.añadir(usuario);
        return "redirect:/usuarios";
    }

}
