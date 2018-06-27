package com.ufc.br.controler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ufc.br.model.Cliente;
import com.ufc.br.model.Compra;
import com.ufc.br.model.Produto;
import com.ufc.br.service.ClienteService;
import com.ufc.br.service.CompraService;
import com.ufc.br.service.ProdutoService;

@Controller
@RequestMapping("/produto")
public class ProdutoController {

	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private CompraService compraService;
	
	@Autowired
	private ClienteService clienteService;
	
	public List<Produto> produtos = new ArrayList<Produto>();
	
	@RequestMapping("/formulario")
	public ModelAndView formularioProduto() {
		ModelAndView mv = new ModelAndView("formulario-produto");
		mv.addObject("produto", new Produto());
		return mv;
	}
	
	
	@PostMapping("/salvar")
	public ModelAndView salvarProduto(Produto produto, @RequestParam(value="imagem") MultipartFile imagem) {
		produtoService.salvarProduto(produto, imagem);
		ModelAndView mv = new ModelAndView("redirect:/produto/listar");
		mv.addObject("mensagem", "Produto salvo com sucesso!");
		return mv;
	}
	
	@GetMapping("/listar")
	public ModelAndView listarProdutos() {
		List<Produto> produtos = produtoService.listarProdutos();
		ModelAndView mv = new ModelAndView("lista-produtos");
		mv.addObject("todosOsProdutos",produtos);
		mv.addObject("produto", new Produto());
		return mv;	
	}
	
	@RequestMapping("/excluir/{id}")
	public ModelAndView excluirProduto(@PathVariable Long id) {
		produtoService.excluiPorId(id);
		ModelAndView mv = new ModelAndView("redirect:/produto/listar");
		return mv;
	}
	
	@RequestMapping("/atualizar/{id}")
	public ModelAndView atualizarPessoa(@PathVariable Long id) {
		Produto produto = produtoService.buscarPorId(id);
		ModelAndView mv = new ModelAndView("formulario-produto");
		mv.addObject("produto", produto);
		return mv;
	}
	
	double total=0.0;
	
	@RequestMapping("/comprar/{id}")
	public ModelAndView comprarProduto(@PathVariable Long id) {
		Produto produto = produtoService.buscarPorId(id);
		produtos.add(produto);
		total+=produto.getPreco();
		ModelAndView mv = new ModelAndView("carrinho");
		mv.addObject("listaProdutos",produtos);
		return mv;
	}
	
	@RequestMapping("/remover/{id}")
	public ModelAndView removerProduto(@PathVariable Long id) {
		Produto produto = produtoService.buscarPorId(id);
		produtoService.removerCarrinho(produto, produtos);
		ModelAndView mv = new ModelAndView("carrinho");
		mv.addObject("listaProdutos", produtos);
		return mv;
	}
	
	@RequestMapping("/carrinho")
	public ModelAndView carrinho() {
		ModelAndView mv = new ModelAndView("carrinho");
		mv.addObject("listaProdutos", produtos);
		return mv;
	}
	
	@RequestMapping("/finalizar")
	public ModelAndView finalizarCompra() {
		
		Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails user = (UserDetails) auth;
		Cliente cliente = clienteService.buscarPorUsername(user.getUsername());
		
		for (Produto produto : produtos) {
			Long idProduto = produto.getId();
			Double totalCompra = produto.getPreco();
			Long idCliente = cliente.getId();

			Compra compra = new Compra(idCliente,idProduto,totalCompra);
			compraService.salvarCompra(compra);
		}
		produtos.clear();
		
		total=0.0;
		
		ModelAndView mv = new ModelAndView("redirect:/produto/historico");

		return mv;
	}
	
	@RequestMapping("/historico")
	public ModelAndView historicoCompra() {
		Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails user = (UserDetails) auth;
		Cliente cliente = clienteService.buscarPorUsername(user.getUsername());
		
		List<Compra> compras = compraService.buscarPorIdCliente(cliente.getId());
		
		ModelAndView mv = new ModelAndView("historico");
		mv.addObject("todasAsCompras", compras);
		
		return mv;
	}
	
	@RequestMapping("/detalhes")
	public ModelAndView produtoDetalhe() {
		ModelAndView mv = new ModelAndView("Detalhes");
		mv.addObject("produto", new Produto());
		return mv;
	}
	
	@RequestMapping("/detalhes/{id}")
	public ModelAndView detalhes(@PathVariable Long id) {
		Produto produto = produtoService.buscarPorId(id);
		ModelAndView mv = new ModelAndView("Detalhes");
		mv.addObject("produto", produto);
		return mv;
	}
}
