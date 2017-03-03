
package com.algaworks.brewer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.brewer.model.Cliente;
import com.algaworks.brewer.repository.filter.ClienteFilter;
import com.algaworks.brewer.repository.helper.cliente.PageableQueries;

public interface Clientes extends JpaRepository<Cliente, Long>, PageableQueries<Cliente, ClienteFilter>{

    public Optional<Cliente> findByCpfOuCnpj(String cpfOuCnpj);

    public List<Cliente> findByNomeStartingWithIgnoreCase(String nome);

}