package com.example.bookadvisor.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")

public class LibroDTO {

    private int id;
    private String titulo;
    private String autor;
    private Idioma idioma;
    private String sipnosis;
    private String foto;
}
