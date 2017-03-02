package com.algaworks.brewer.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "refrigerante")
public class Refrigerante implements Serializable {

	private static final long serialVersionUID = 9182331735607234651L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "O nome do refrigerante é obrigatório.")
	@Column(unique = true)
	private String nome;
	
	@NotBlank(message = "A descrição do refrigerante é obrigatória.")
	private String descricao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_criacao")
	@NotNull
	@DateTimeFormat
	private Date dataCriacao;
	
	@NotNull(message = "O preço do refrigerante é obrigatório.")
	private BigDecimal preco;
	
	@Enumerated(EnumType.STRING)
	private SaborRefrigerante sabor;
	
	@ManyToOne
	@JoinColumn(name = "marca_id")
	private Marca marca;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public SaborRefrigerante getSabor() {
		return sabor;
	}

	public void setSabor(SaborRefrigerante sabor) {
		this.sabor = sabor;
	}

	public Marca getMarca() {
		return marca;
	}

	public void setMarca(Marca marca) {
		this.marca = marca;
	}
	
	
}
