package com.ufc.br.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ufc.br.model.Cliente;
import com.ufc.br.model.Produto;
import com.ufc.br.repository.ClienteRepository;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	
	public void salvarCliente(Cliente cliente) {
		cliente.setPassword(new BCryptPasswordEncoder().encode(cliente.getPassword()));
		clienteRepository.save(cliente);
	}

	public List<Cliente> listarCliente() {
		return clienteRepository.findAll(); 
	}
	
	public void excluiPorId(Long id) {
		clienteRepository.deleteById(id);		
	}

	public Cliente buscarPorId(Long id) {
		return clienteRepository.getOne(id);
	}
	
	public Cliente buscarPorUsername(String username) {
		return clienteRepository.findByUsername(username);
	}
	
	
}
