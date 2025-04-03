package com.example.bookadvisor.domain;

import jakarta.persistence.Column;
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

public class UsuarioDTO {

    private Long id;
    @Column(nullable = false, unique = true)
    private String nombre;
    private String password;
    private Rol rol;
}
