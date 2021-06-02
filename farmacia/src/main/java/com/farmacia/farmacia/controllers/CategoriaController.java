package com.farmacia.farmacia.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmacia.farmacia.models.Categoria;
import com.farmacia.farmacia.models.Produto;
import com.farmacia.farmacia.repositories.CategoriaRepository;
import com.farmacia.farmacia.services.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

	@Autowired
	private CategoriaRepository repository;
	@Autowired
	private CategoriaService service;

	// Categorias
	@GetMapping("/todas")
	public ResponseEntity<List<Categoria>> findAllCategoria() {
		List<Categoria> listaDeCategorias = repository.findAll();
		return ResponseEntity.status(200).body(listaDeCategorias);
	}

	@GetMapping("/id")
	public ResponseEntity<Categoria> findByIDCategoria(@RequestParam ("id") long id) {
		return repository.findById(id).map(categoriaDoId -> ResponseEntity.status(200).body(categoriaDoId))
				.orElse(ResponseEntity.status(400).build());
	}

	@GetMapping("/descricao/{descricao}")
	public ResponseEntity<List<Categoria>> findByDescricaoCategoria(@PathVariable String descricao) {

		return ResponseEntity.status(200).body(repository.findAllByDescricaoContainingIgnoreCase(descricao));
	}

	@PostMapping("/cadastrar")
	public ResponseEntity<String> postCategoria(@Valid @RequestBody Categoria novaCategoria) {
		return service.cadastrarPostagem(novaCategoria)
				.map(categoriaCadastrada -> ResponseEntity.status(201)
						.body("Categoria: " + novaCategoria.getDescricao() + "\nCadastrada com sucesso!"))
				.orElse(ResponseEntity.status(400)
						.body("Falha no cadastro de categoria, já existe uma categoria com essa descrição."));
	}

	@PutMapping("/atualizar/{id}")
	public ResponseEntity<String> putCategoria(@Valid @PathVariable(value = "id") Long id,
			@RequestBody Categoria dadosNovos) {
		return service.atualizarCategoria(id, dadosNovos)
				.map(categoriaAtualizada -> ResponseEntity.status(201)
						.body("Categoria: " + dadosNovos.getDescricao() + "\nAtualizada com sucesso!"))
				.orElse(ResponseEntity.status(400).body("A categoria não existe."));
	}

	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<String> deleteCategoria(@PathVariable long id) {
		Optional<Categoria> categoriaExistente = repository.findById(id);

		if (categoriaExistente.isPresent()) {
			repository.deleteById(id);
			return ResponseEntity.status(200).body("Categoria deletada com sucesso!");
		} else {
			return ResponseEntity.status(400).body("A categoria não existe.");
		}
	}

	// Produtos
	@PostMapping("/produtos/cadastrar/{idCategoria}")
	public ResponseEntity<String> postProduto(@PathVariable(value = "idCategoria") Long idCategoria,
			@Valid @RequestBody Produto novoProduto) {
		return service.cadastrarProduto(idCategoria, novoProduto).map(cadastrado -> {
			return ResponseEntity.status(201).body("Produto: " + novoProduto.getTitulo() + "\nCategoria: "
					+ novoProduto.getCategoria().getDescricao() + "\nCadastrado com sucesso!");
		}).orElse(ResponseEntity.status(400)
				.body("Erro no cadastro, categoria não existe ou um produto com o mesmo título já existe no sistema."));
	}

	@PutMapping("/produtos/atualizar/{idProduto}")
	public ResponseEntity<String> putProduto(@PathVariable(value = "idProduto") Long idProduto,
			@Valid @RequestBody Produto dadosProduto) {
		return service.atualizarProduto(idProduto, dadosProduto).map(dadosAtualizados -> {
			Optional<Categoria> categoriaExistente = repository.findById(dadosProduto.getCategoria().getId());
			return ResponseEntity.status(201).body("Produto: " + dadosProduto.getTitulo() + "\nCategoria: "
					+ categoriaExistente.get().getDescricao() + "\nCadastrado com sucesso!");
		}).orElse(ResponseEntity.status(400)
				.body("Erro ao atualizar, categoria ou produto não existem, ou já há um produto com o mesmo título."));
	}

}
