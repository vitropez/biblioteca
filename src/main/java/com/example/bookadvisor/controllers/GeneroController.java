package com.example.bookadvisor.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.bookadvisor.domain.Genero;
import com.example.bookadvisor.services.GeneroService;

@Controller
@RequestMapping("/generos")
public class GeneroController {

    @Autowired
    private GeneroService generoService;

    // Obtener todos los géneros y mostrarlos en la vista
    @GetMapping
    public String listarGeneros(Model model) {
        List<Genero> generos = generoService.getAllGeneros();

        model.addAttribute("generos", generos);
        return "listaGeneros";
    }

    // Mostrar formulario de creación de un nuevo género
    @GetMapping("/nuevo")
    public String mostrarFormularioDeCreacion(Model model) {
        model.addAttribute("genero", new Genero());
        return "formularioGenero";
    }

    // Crear un nuevo género
    @PostMapping
    public String crearGenero(@ModelAttribute Genero genero) {
        generoService.createGenero(genero);
        return "redirect:/generos";
    }

    // Obtener un género por ID y mostrar el formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEdicion(@PathVariable Long id, Model model) {
        Optional<Genero> generoOpt = generoService.getGeneroById(id);
        if (generoOpt.isPresent()) {
            model.addAttribute("genero", generoOpt.get());
            return "genero/formularioGenero";
        } else {
            return "redirect:/generos";
        }
    }

    // Actualizar un género existente
    @PostMapping("/editar")
    public String editarGenero(@ModelAttribute Genero genero) {
        generoService.updateGenero(genero.getId(), genero);
        return "redirect:/generos";
    }

    // Eliminar un género por ID
    @PostMapping("/borrar/{id}")
    public String borrarGenero(@PathVariable Long id) {
        generoService.deleteGenero(id);
        return "redirect:/generos";
    }
}
