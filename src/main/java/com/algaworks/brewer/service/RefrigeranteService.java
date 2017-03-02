package com.algaworks.brewer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Marca;
import com.algaworks.brewer.model.Refrigerante;
import com.algaworks.brewer.repository.Marcas;
import com.algaworks.brewer.repository.Refrigerantes;

@Service
public class RefrigeranteService extends RelatorioService {
	
	@Autowired
	private Refrigerantes refrigerantes;
	
	@Autowired
	private Marcas marcas;

	@Transactional
	public void save(Refrigerante refrigerante) {
		refrigerantes.save(refrigerante);
	}
	
	@Transactional
	public void save(Marca marca) {
		marcas.save(marca);
	}
	
	@Transactional
	public void delete(Long id){
		refrigerantes.delete(id);
	}
	
	public Refrigerantes getRefrigerantes() {
		return refrigerantes;
	}
	
	public Marcas getMarcas() {
		return marcas;
	}

}
