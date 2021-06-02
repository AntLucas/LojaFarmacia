package com.farmacia.farmacia.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farmacia.farmacia.models.Produto;
import com.farmacia.farmacia.repositories.ProdutoRepository;


@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	@Autowired
	private ProdutoRepository repository;

	
	
	@GetMapping("/todos")
	public ResponseEntity<List<Produto>> findAllProduto(){
		List<Produto> listaDeProdutos = repository.findAll();
		return ResponseEntity.status(200).body(listaDeProdutos);
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<Produto> findByIDProduto (@PathVariable long id){
		return repository.findById(id)
				.map(produtoDoId -> ResponseEntity.status(200).body(produtoDoId))
				.orElse(ResponseEntity.status(400).build());
	}
	
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<Produto> findByDescricaoTitulo (@PathVariable String titulo){
		
		return repository.findByTitulo(titulo)
				.map(retorno -> {
				return ResponseEntity.status(200).body(retorno);
				} )
				.orElse(ResponseEntity.status(404).build());
	}
	
	
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<Object> deleteProduto(@PathVariable long id){
		Optional<Produto> produtoExistente = repository.findById(id);
		
		if(produtoExistente.isPresent()) {
			repository.deleteById(id);
			return ResponseEntity.status(200).body("Produto deletado com sucesso!");
		} else {
			return ResponseEntity.status(400).body("O produto não existe.");
		}
	}
	
}