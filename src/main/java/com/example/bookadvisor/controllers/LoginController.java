package com.example.bookadvisor.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.bookadvisor.domain.Rol;
import com.example.bookadvisor.domain.Usuario;

@Controller
public class LoginController {
    @GetMapping("/signin")
    public String showLogin() {
        return "signinView";
    }

    @GetMapping("/signout")
    public String showLogout() {
        return "signoutView";
    }

    @GetMapping("/signup")
    public String showSignup(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("rol", Rol.USER);
        return "signinupView"; // Vista de registro
    }
}
