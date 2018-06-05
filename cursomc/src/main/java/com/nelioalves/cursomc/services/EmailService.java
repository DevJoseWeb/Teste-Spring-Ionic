package com.nelioalves.cursomc.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Pedido;

/*serviço de email vai criar como interface e n como classe;
como vai criar o mock e o smtp, vai usar o polimorfismo p 
obter as duas implementacoes de forma super flexivel;
vai usar o padrao strategy, q é implementar uma interface e 
depois instancia uma implementacao p dizer como o email 
service vai se comportar*/
public interface EmailService {

	//criar um metodo auxiliar, e instanciar o simplemail a partir dele 
	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
}
