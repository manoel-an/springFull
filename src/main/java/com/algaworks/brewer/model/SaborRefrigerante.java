package com.algaworks.brewer.model;

public enum SaborRefrigerante {
	
	COLA("Cola"), LARANJA("Laranja"), GUARANA("Guaraná"), LIMAO("Limão"), UVA("Uva");
	
	private String descricao;
	
	SaborRefrigerante(String descricao){
		this.setDescricao(descricao);
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
