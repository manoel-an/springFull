package com.algaworks.brewer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.brewer.model.Refrigerante;
import com.algaworks.brewer.repository.filter.RefrigeranteFilter;
import com.algaworks.brewer.repository.helper.cliente.PageableQueries;

@Repository
public interface Refrigerantes extends JpaRepository<Refrigerante, Long>, PageableQueries<Refrigerante, RefrigeranteFilter> {
	List<Refrigerante> findByNomeContaining(String nome);
}
