package com.example.bookadvisor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.bookadvisor.domain.Genero;
import com.example.bookadvisor.domain.Rol;
import com.example.bookadvisor.domain.Usuario;
import com.example.bookadvisor.services.GeneroService;
import com.example.bookadvisor.services.UsuarioService;

@SpringBootApplication
public class BookadvisorApplication {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private GeneroService generoService;

	public static void main(String[] args) {
		SpringApplication.run(BookadvisorApplication.class, args);
	}

	@Bean
	CommandLineRunner init() {
		return args -> {
			usuarioService.añadir(new Usuario(null, "Santiago", "miCorreo@mail.com", Rol.ADMIN, "1234", null));
			generoService.createGenero(new Genero(null, "Novela"));
			generoService.createGenero(new Genero(null, "Poesía"));
			generoService.createGenero(new Genero(null, "Cuento"));
			generoService.createGenero(new Genero(null, "Ensayo"));
			generoService.createGenero(new Genero(null, "Teatro"));
			generoService.createGenero(new Genero(null, "Ciencia ficción"));
			generoService.createGenero(new Genero(null, "Fantasía"));
			generoService.createGenero(new Genero(null, "Biografía"));
			generoService.createGenero(new Genero(null, "Novela"));

		};
	}
}
