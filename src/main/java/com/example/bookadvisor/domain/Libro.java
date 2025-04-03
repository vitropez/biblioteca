package com.example.bookadvisor.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Setter
@Getter
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private LocalDate ano;
    private String autor;
    private Idioma idioma;
    private String sipnosis;
    private LocalDate fechaAlta;
    private String foto;

    @ManyToOne
    @JoinColumn(name = "genero_id")
    private Genero genero;

    @OneToMany(mappedBy = "libro")
    private List<Valoracion> valoraciones;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Usuario> usuario = new ArrayList<>();

}
