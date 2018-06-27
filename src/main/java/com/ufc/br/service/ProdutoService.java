package com.ufc.br.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ufc.br.model.Cliente;
import com.ufc.br.model.Compra;
import com.ufc.br.model.Produto;
import com.ufc.br.repository.ProdutoRepository;
import com.ufc.br.util.FilesUtils;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CompraService compraService;
	
	public void salvarProduto(Produto produto, MultipartFile imagem) {
		String caminho = "images/" + produto.getNome() + ".png";
		FilesUtils.salvarImagem(caminho, imagem);
		produtoRepository.save(produto);
		
	}

	public List<Produto> listarProdutos() {
		return produtoRepository.findAll();
	}

	public void excluiPorId(Long id) {
		produtoRepository.deleteById(id);		
	}

	public Produto buscarPorId(Long id) {
		return produtoRepository.getOne(id);
	}
	
	public List<Produto> removerCarrinho(Produto produt, List<Produto> produtos) {
		Long id = produt.getId();
		for(Produto produto : produtos) {
			if(produto.getId() == id) {
				produtos.remove(produto);
				return produtos;
			}
		}
		return null;
	}
}
