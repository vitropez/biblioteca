package com.example.bookadvisor.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.bookadvisor.domain.FindForm;
import com.example.bookadvisor.domain.Genero;
import com.example.bookadvisor.domain.Libro;
import com.example.bookadvisor.domain.LibroDTO;
import com.example.bookadvisor.services.BookService;
import com.example.bookadvisor.services.GeneroService;

@Controller
@RequestMapping("/libros")
public class VistaController {
    @Autowired
    private BookService bookService;

    @Autowired
    private GeneroService generoService;

    @GetMapping
    public String showLibros(@RequestParam(required = false) Integer numMsg,
            Model model) {

        if (numMsg != null) {
            switch (numMsg) {
                case 1 ->
                    model.addAttribute("msg", "libro no encontrado");
                case 2 ->
                    model.addAttribute("msg", "Formulario incorrecto");
            }
        }

        List<Genero> listaGeneros = generoService.getAllGeneros();
        model.addAttribute("listaGeneros", listaGeneros);
        List<Libro> listaLibros = bookService.obtenerTodos();
        List<LibroDTO> listaDTO = bookService.convertLibroToDto(listaLibros);

        model.addAttribute("listaLibros", listaDTO);
        model.addAttribute("findForm", new FindForm());
        return "bookListView";
    }

    @GetMapping("/{id}")
    public String showElement(@PathVariable Long id, Model model) {

        try {
            Libro libro = bookService.obtenerPorId(id);

            model.addAttribute("libro", libro);
            long cantidadVotantes = bookService.contarVotantes(id);
            int sumaPuntos = bookService.sumarPuntos(id);
            double mediaPuntos = bookService.mediaPuntos(id);

            model.addAttribute("cantidadVotantes", cantidadVotantes);
            model.addAttribute("sumaPuntos", sumaPuntos);
            model.addAttribute("mediaPuntos", mediaPuntos);
            return "bookView";
        } catch (RuntimeException e) {

            return "redirect:/?numMsg=1";
        }
    }

    @GetMapping("/nuevo")
    public String showNew(Model model) {

        try {
            List<Genero> generos = generoService.getAllGeneros();

            model.addAttribute("generos", generos);
            model.addAttribute("libroForm", new Libro());
            return "newFormView";
        } catch (RuntimeException e) {

            return "redirect:/?numMsg=1";
        }
    }

    @PostMapping("/submit")
    public String showNewSubmit(Libro libroForm, @RequestParam MultipartFile portada, Model model) throws IOException {

        try {

            String img = bookService.store(portada, libroForm.getTitulo());

            libroForm.setFoto(img);
            bookService.añadir(libroForm);

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return "redirect:/?numMsg=2";
        }

        return "redirect:/libros";
    }

    @GetMapping("/editar/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {

        try {
            Libro libro = bookService.obtenerPorId(id);
            List<Genero> generos = generoService.getAllGeneros();
            model.addAttribute("generos", generos);

            if (libro == null) {
                return "redirect:/?numMsg=1";

            }
            model.addAttribute("libroForm", libro);
            model.addAttribute("generos", generos);
            return "editFormView";
        } catch (RuntimeException e) {

            return "redirect:/?numMsg=1";
        }
    }

    @PostMapping("/editar/{id}/submit")
    public String showEditSubmit(@PathVariable int id, Libro libroForm,
            RedirectAttributes redirectAttrs, Model model, @RequestParam MultipartFile portada) {

        try {
            Libro libroAntesUpdated = bookService.obtenerPorId(libroForm.getId());
            String imgAntigua = libroAntesUpdated.getFoto();
            Libro libro = bookService.editar(libroForm);
            if (libro == null) {
                return "redirect:/?numMsg=2";
            }
            if (portada.isEmpty()) {
                libro.setFoto(imgAntigua);
                bookService.updateLibro(libro);
            } else {
                String img = bookService.store(portada, libroForm.getTitulo());
                libro.setFoto(img);
                bookService.updateLibro(libro);
            }

            return "redirect:/libros";
        } catch (RuntimeException e) {

            return "redirect:/?numMsg=2";
        }
    }

    @GetMapping("/borrar/{id}")
    public String showDelete(@PathVariable Long id, RedirectAttributes redirectAttrs, Model model) {

        try {
            Libro libro = bookService.obtenerPorId(id);

            bookService.borrar(libro.getId());

            return "redirect:/libros";
        } catch (RuntimeException e) {

            return "redirect:/?numMsg=1";
        }
    }

    @PostMapping("/findByName")
    public String showFindByNameSubmit(FindForm findForm, Model model) {

        try {
            model.addAttribute("listaLibros",
                    bookService.buscarPorNombre(findForm.getTitulo()));

            return "bookListView";
        } catch (RuntimeException e) {

            return "redirect:/?numMsg=1";
        }
    }

    @GetMapping("/findByGenero/{genero}")
    public String showFindByGenero(@PathVariable Genero genero, Model model) {

        model.addAttribute("listaLibros", bookService.buscarPorGenero(genero));
        model.addAttribute("generoSeleccionado", genero);
        model.addAttribute("findForm", new FindForm());
        return "bookListView";
    }

    // @GetMapping("/public/files/{filename:.+}")

    @ResponseBody
    public ResponseEntity serveFile(@PathVariable String filename) {
        Resource file = bookService.loadAsResource(filename);
        return ResponseEntity.ok().body(file);
    }

}
