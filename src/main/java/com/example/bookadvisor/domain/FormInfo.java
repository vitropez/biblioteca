
package com.example.bookadvisor.domain;

//import com.example.bookadvisor.Asunto;

public class FormInfo {
    private String nombre;
    private String email;

    private Asunto asunto;
    private boolean aceptoCondiciones;
    private String texto;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Asunto getAsunto() {
        return asunto;
    }

    public void setAsunto(Asunto asunto) {
        this.asunto = asunto;
    }

    public boolean isAceptoCondiciones() {
        return aceptoCondiciones;
    }

    public void setAceptoCondiciones(boolean aceptoCondiciones) {
        this.aceptoCondiciones = aceptoCondiciones;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

}
