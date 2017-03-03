package com.algaworks.brewer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.controller.page.PageWrapper;
import com.algaworks.brewer.model.Marca;
import com.algaworks.brewer.model.Refrigerante;
import com.algaworks.brewer.model.SaborRefrigerante;
import com.algaworks.brewer.service.RefrigeranteService;

@Controller
public class RefrigerantesController extends FileController {
	
	@Autowired
	private RefrigeranteService service;
	
	@GetMapping("/refrigerantes")
	public ModelAndView pesquisar(@PageableDefault(size = 3) Pageable pageable, HttpServletRequest httpServletRequest){
		ModelAndView modelAndView = new ModelAndView("refrigerante/PesquisaRefrigerante");
		PageWrapper<Refrigerante> paginaWrapper = new PageWrapper<>(service.getRefrigerantes().filtrar(null, pageable),
                httpServletRequest);
        modelAndView.addObject("pagina", paginaWrapper);
		return modelAndView;
	}
	
	@RequestMapping(path = "/refrigerantes/novo", method = RequestMethod.GET)	
	public ModelAndView cadastrarRefrigerante(Refrigerante refrigerante){
		ModelAndView modelAndView = new ModelAndView("refrigerante/CadastroRefrigerante");
		modelAndView.addObject("sabores", SaborRefrigerante.values());
		modelAndView.addObject("refrigerantes", service.getRefrigerantes().findAll());
		modelAndView.addObject("marcas", service.getMarcas().findAll());
		return modelAndView;
	}
	
	@RequestMapping(path = "/refrigerantes/novoajax", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public ResponseEntity<?> salvarRefrigerante(@RequestBody @Valid Refrigerante refrigerante, BindingResult result){
		if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }else{
        	service.save(refrigerante);
        	return ResponseEntity.ok(service.getRefrigerantes().findAll());
        }
	}
	
	@RequestMapping(path = "/refrigerantes/novo", method = RequestMethod.POST)
	public ModelAndView salvarRefrigerante(Refrigerante refrigerante, BindingResult result, RedirectAttributes attributes){
		if (result.hasErrors()) {
            return cadastrarRefrigerante(refrigerante);
        }
		this.service.save(refrigerante);
		attributes.addFlashAttribute("mensagem", "Salvo com sucesso!");
		return new ModelAndView("redirect:/refrigerantes/novo");
	}
	
	@GetMapping("/refrigerantes/remover/{id}")
	@ResponseBody
	public void removerRefrigerante(@PathVariable("id") Long id){
		this.service.delete(id);
	}
	
	@GetMapping("/refrigerantes/editar/{id}")
	@ResponseBody
	public ResponseEntity<?> editarRefrigerante(@PathVariable("id") Long id){
		return ResponseEntity.ok(this.service.getRefrigerantes().findOne(id));
	}
	
	@ResponseBody
	@GetMapping("/refrigerantes/pesquisar/{filtro}")
	public ResponseEntity<?> pesquisar(@PathVariable("filtro") String filtro){
		return ResponseEntity.ok(service.getRefrigerantes().findByNomeContaining(filtro));
	}
	
	@ResponseBody
	@GetMapping("/refrigerantes/pesquisar")
	public ResponseEntity<?> pesquisarAJAX(){
		return ResponseEntity.ok(service.getRefrigerantes().findAll());
	}
	
	@ResponseBody
	@PostMapping(path = "/refrigerantes/novamarca", consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> salvarMarca(@RequestBody @Valid Marca marca, BindingResult result){
		if(result.hasErrors()){
			return ResponseEntity.badRequest().body(result.getAllErrors());
		}else{
			this.service.save(marca);
			return ResponseEntity.ok(marca);
		}
	}
	
	@GetMapping("/refrigerantes/imprimir")
	@ResponseBody
	public void imprimir(HttpServletResponse response) throws Exception {
		service.gerarPDF("helloworld", null, "relatorio", true, response);
	}
	
	@GetMapping("/refrigerantes/baixar")
	@ResponseBody
	public void baixar(HttpServletResponse response) throws Exception {
		service.gerarPDF("helloworld", null, "relatorio", false, response);
	}
}
