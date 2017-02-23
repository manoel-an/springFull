package com.algaworks.brewer.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.controller.page.PageWrapper;
import com.algaworks.brewer.model.Grupo;
import com.algaworks.brewer.model.Usuario;
import com.algaworks.brewer.repository.Grupos;
import com.algaworks.brewer.repository.Usuarios;
import com.algaworks.brewer.repository.filter.UsuarioFilter;
import com.algaworks.brewer.service.CadastroUsuarioService;
import com.algaworks.brewer.service.StatusUsuario;
import com.algaworks.brewer.service.exception.EmailUsuarioJaCadastradoException;
import com.algaworks.brewer.service.exception.SenhaObrigatoriaUsuarioException;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {

    @Autowired
    private CadastroUsuarioService cadastroUsuarioService;

    @Autowired
    private Grupos grupos;

    @Autowired
    private Usuarios usuarios;

    @RequestMapping("/novo")
    public ModelAndView novo(Usuario usuario) {
        ModelAndView mv = new ModelAndView("usuario/CadastroUsuario");
        mv.addObject("grupos", grupos.findAll());
        return mv;
    }

    @PostMapping("/novo")
    public ModelAndView salvar(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return novo(usuario);
        }

        try {
            cadastroUsuarioService.salvar(usuario);
        } catch (EmailUsuarioJaCadastradoException e) {
            result.rejectValue("email", e.getMessage(), e.getMessage());
            return novo(usuario);
        } catch (SenhaObrigatoriaUsuarioException e) {
            result.rejectValue("senha", e.getMessage(), e.getMessage());
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

    @GetMapping
    public ModelAndView pesquisar(UsuarioFilter usuarioFilter, @PageableDefault(size = 3) Pageable pageable,
            HttpServletRequest httpServletRequest) {
        ModelAndView mv = new ModelAndView("/usuario/PesquisaUsuarios");
        mv.addObject("grupos", grupos.findAll());

        PageWrapper<Usuario> paginaWrapper = new PageWrapper<>(usuarios.filtrar(usuarioFilter, pageable),
                httpServletRequest);
        mv.addObject("pagina", paginaWrapper);
        return mv;
    }

    @PutMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public void atualizarStatus(@RequestParam("codigos[]") Long[] codigos,
            @RequestParam("status") StatusUsuario statusUsuario) {
        cadastroUsuarioService.alterarStatus(codigos, statusUsuario);
    }

    @RequestMapping(value = "/carregarGrupos", method = RequestMethod.POST, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody ResponseEntity<?> carregarGrupos(@RequestBody @RequestParam("codigo") Long codigo) {
        List<Grupo> grupos = this.usuarios.grupos(codigo);
        for (Grupo grupo : grupos) {
            grupo.setPermissoes(null);
        }
        return ResponseEntity.ok(grupos);
    }

    // @RequestMapping(value = "/submit.htm", method =
    // RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    // public @ResponseBody
    // String Submit(@RequestParam("name") String name,@RequestParam("location")
    // String location) {
    // // your logic here
    // return resp;
    // }
}