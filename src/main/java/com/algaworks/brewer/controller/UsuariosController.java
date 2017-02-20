package com.algaworks.brewer.controller;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.model.Usuario;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {

    @RequestMapping("/novo")
    public ModelAndView novo(Usuario usuario) {
        ModelAndView mv = new ModelAndView("usuario/CadastroUsuario");
        return mv;
    }

    @PostMapping("/novo")
    public ModelAndView salvar(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return novo(usuario);
        }

        attributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso");
        return new ModelAndView("redirect:/usuarios/novo");
    }

    @RequestMapping(value = "/testeData", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody ResponseEntity<?> testeData(@RequestBody Usuario usuario) {
        System.out.println(usuario.getDataNascimento());
        if (usuario.getDataNascimento() != null) {
            return ResponseEntity.ok("Deu Certo");
        } else {
            return ResponseEntity.badRequest().body("Deu Merda");
        }
    }

}