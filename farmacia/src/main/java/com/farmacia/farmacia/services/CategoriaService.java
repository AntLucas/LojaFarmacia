package com.farmacia.farmacia.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmacia.farmacia.models.Categoria;
import com.farmacia.farmacia.models.Produto;
import com.farmacia.farmacia.repositories.CategoriaRepository;
import com.farmacia.farmacia.repositories.ProdutoRepository;


@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repository;
	@Autowired
	private ProdutoRepository repositoryp;
	
	/**
	 * Método para cadastrar categorias caso não haja alguma com a mesma descrição, caso haja não é cadastrado
	 * @param novaCategoria - objeto passado pelo controller
	 * @return um Optional com os dados cadastrados da nova descrição ou retorna um Optional vazio
	 */
	public Optional<Object> cadastrarPostagem (Categoria novaCategoria){
		Optional<Categoria> categoriaExistente = repository.findByDescricao(novaCategoria.getDescricao());
		
		if(categoriaExistente.isEmpty()) {
			return Optional.ofNullable(repository.save(novaCategoria));
		}
		else {
			return Optional.empty();
		}
	}
	
	/**
	 * Método para atualizar categorias caso existam no banco de dados
	 * @param id
	 * @param dadosCategoria
	 * @return Optional com a categoria atualizado ou Optional vazio
	 */
	public Optional<Object> atualizarCategoria (Long id, Categoria dadosCategoria){
		Optional<Categoria> categoriaExistente = repository.findById(id);
		if(categoriaExistente.isPresent()) {
			categoriaExistente.get().setDescricao(dadosCategoria.getDescricao());
			return Optional.ofNullable(repository.save(categoriaExistente.get()));
		}
		else {
			return Optional.empty();
		}
	}
	
	/**
	 * Método para cadastrar produtos caso não exista um com o mesmo título no banco de dados
	 * e exista uma categoria com o id passado
	 * @param idCategoria
	 * @param novoProduto
	 * @return Optional com o produto cadastrado ou um Optional vazio
	 */
	public Optional <Object> cadastrarProduto (Long idCategoria, Produto novoProduto){
		Optional<Categoria> categoriaExistente = repository.findById(idCategoria);
		
		if(categoriaExistente.isPresent()) {
			Optional<Produto> produtoExistente = repositoryp.findByTitulo(novoProduto.getTitulo());
			
			if(produtoExistente.isEmpty()) {
				novoProduto.setCategoria(categoriaExistente.get());
				return Optional.ofNullable(repositoryp.save(novoProduto));
			}
			else {
				return Optional.empty();
			}
		}
		else {
			return Optional.empty();
		}
	}
	
	/**
	 * Método para atualizar produtos caso exista o produto e a categoria passada, e caso não tenha um produto
	 * com o mesmo título já cadastrado
	 * @param idProduto
	 * @param dadosProduto
	 * @return Optional com o produto atualizado ou Optional vazio
	 */
	public Optional <Object> atualizarProduto (Long idProduto ,Produto dadosProduto){
		Optional<Produto> produtoExistente = repositoryp.findById(idProduto);
		
		if(produtoExistente.isPresent()) {
			
			Optional<Produto> outroProduto = repositoryp.findByTitulo(dadosProduto.getTitulo());
			
			if(outroProduto.isEmpty()) {
				Optional<Categoria> categoriaExistente = repository.findById(dadosProduto.getCategoria().getId());
				
				if(categoriaExistente.isPresent()) {
					produtoExistente.get().setCategoria(dadosProduto.getCategoria());
					produtoExistente.get().setTitulo(dadosProduto.getTitulo());
					
					return Optional.ofNullable(repositoryp.save(produtoExistente.get()));
				}
			}
			else {
				
			}
			
			return Optional.empty();
		}
		else {
			return Optional.empty();
		}
	}
}