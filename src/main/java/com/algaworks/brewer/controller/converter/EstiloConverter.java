package com.algaworks.brewer.controller.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import com.algaworks.brewer.model.Estilo;
import com.algaworks.brewer.repository.Estilos;

public final class EstiloConverter implements Converter<String, Estilo> {

    @Autowired
    private Estilos service;

    @Override
    public Estilo convert(String codigo) {
        if (!StringUtils.isEmpty(codigo)) {
            Estilo estilo = new Estilo();
            // estilo.setCodigo(Long.valueOf(codigo));
            estilo = service.findOne(Long.valueOf(codigo));
            return estilo;
        }

        return null;
    }

}