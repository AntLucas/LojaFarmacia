package com.farmacia.farmacia.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "produto")
public class Produto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@Size(min = 2, max = 50)
	private String titulo;
	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "produtos")
	@JsonIgnoreProperties("produtos")
	private Categoria categoria;

	public long getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

}