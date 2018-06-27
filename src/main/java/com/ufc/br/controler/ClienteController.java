package com.ufc.br.controler;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ufc.br.model.Cliente;
import com.ufc.br.model.Produto;
import com.ufc.br.service.ClienteService;
import com.ufc.br.service.CompraService;

@Controller
@RequestMapping("/cliente")
public class ClienteController {
	
	@Autowired
	private ClienteService clienteService;
	
	
	@RequestMapping("/formulario")
	public ModelAndView formularioProduto() {
		ModelAndView mv = new ModelAndView("formulario-cliente");
		mv.addObject("cliente", new Cliente());
		return mv;
	}
	
	@PostMapping("/salvar")
	public ModelAndView salvarCliente(Cliente cliente) {
		clienteService.salvarCliente(cliente);
		ModelAndView mv = new ModelAndView("redirect:/cliente/listar");
		mv.addObject("mensagem", "Cliente salvo com sucesso!");
		return mv;
	}
	
	@GetMapping("/listar")
	public ModelAndView listarCliente() {
		List<Cliente> clientes = clienteService.listarCliente();
		ModelAndView mv = new ModelAndView("lista-cliente");
		mv.addObject("todosOsCliente",clientes);
		return mv;	
	}
	
	@RequestMapping("/excluir/{id}")
	public ModelAndView excluirCliente(@PathVariable Long id) {
		clienteService.excluiPorId(id);
		ModelAndView mv = new ModelAndView("redirect:/cliente/listar");
		return mv;
	}
	
	@RequestMapping("/atualizar/{id}")
	public ModelAndView atualizarCliente(@PathVariable Long id) {
		Cliente cliente = clienteService.buscarPorId(id);
		ModelAndView mv = new ModelAndView("formulario-cliente");
		mv.addObject("cliente", cliente);
		return mv;
	}

	
	@RequestMapping("/logar")
	public String login() {
		return "login";
	}
	

		
}
