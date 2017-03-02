package com.algaworks.brewer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.brewer.model.Refrigerante;

@Repository
public interface Refrigerantes extends JpaRepository<Refrigerante, Long> {
	List<Refrigerante> findByNomeContaining(String nome);
}
