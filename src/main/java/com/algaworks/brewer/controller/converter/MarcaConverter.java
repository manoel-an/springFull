package com.algaworks.brewer.controller.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import com.algaworks.brewer.model.Marca;
import com.algaworks.brewer.repository.Marcas;

public class MarcaConverter implements Converter<String, Marca>{
	
	@Autowired
	private Marcas marcas;
	
	@Override
	public Marca convert(String source) {
		if(!StringUtils.isEmpty(source)){
			return marcas.findOne(Long.valueOf(source));
		}else{
			return null;
		}
	}

}
