package com.algaworks.brewer.repository.helper.cliente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PageableQueries<T, F> {

    public Page<T> filtrar(F filtro, Pageable pageable);

}