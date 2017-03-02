package com.algaworks.brewer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.brewer.model.Marca;

public interface Marcas extends JpaRepository<Marca, Long> {

}
