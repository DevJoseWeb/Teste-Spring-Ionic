package com.nelioalves.cursomc.services;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private EmailService emailService;
	
	private Random rand = new Random();
	
	public void sendNewPassword(String email) {
		Optional<Cliente> clienteOptional = Optional.ofNullable(clienteRepository.findByEmail(email));
		clienteOptional.orElseThrow(() -> new ObjectNotFoundException("Email não encontrado"));
		Cliente cliente = clienteOptional.get();
		
		String newPass = newPassword();
		cliente.setSenha(pe.encode(newPass));
		
		clienteRepository.save(cliente);
		emailService.sendNewPasswordEmail(cliente, newPass);
	}

	private String newPassword() {
		char[] vet = new char[10];
		for (int i = 0; i < 10; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}

	private char randomChar() {
		int opt = rand.nextInt(3);
		if(opt == 0) {
			//unicode do numero 0 é 48 e sao 10 numeros no total
			return (char) (rand.nextInt(10) + 48);
		}else if(opt == 1) {
			//unicode da letra A maiuscula é 65 e sao 26 letras no total
			return (char) (rand.nextInt(26) + 65);
		}else {
			//unicode da letra A minuscula é 97 sao 26 letras no total
			return (char) (rand.nextInt(26) + 97);
		}
	}
}
