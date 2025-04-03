package com.example.bookadvisor.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bookadvisor.domain.FormInfo;
import com.example.bookadvisor.domain.Usuario;
import com.example.bookadvisor.domain.UsuarioDTO;

@Service

public interface UsuarioService {
  public void enviarEmail(FormInfo formInfo);

  public List<Usuario> obtenerTodos();

  public Usuario añadir(Usuario usuario);

  public Usuario editar(Usuario usuario);

  public boolean borrar(Long id);

  public List<Usuario> buscarPorNombre(String textoNombre);

  // public String store(MultipartFile file,String titulo);
  public Usuario obtenerPorId(Long id);

  // public void delete(String filename);
  // public Resource loadAsResource(String filename);
  public void updateUsuario(Usuario usuario);

  public Usuario getUsuarioByNombre(String nombre);

  public UsuarioDTO convertUsuarioToDto(Usuario usuario);

  public Usuario buscarPorName(String nombre);

}
