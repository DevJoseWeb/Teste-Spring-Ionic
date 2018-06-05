package com.nelioalves.cursomc.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.nelioalves.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {

	@Value("${default.sender}")
	private String sender;
	
	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private JavaMailSender javaMailSender;
	
	/*template method: consegue implementar um metodo, baseado em 
	metodos abstratos, q depois vai ser implementado nas implementacoes 
	da interface*/
	//instanciar o simplemail atraves de um pedido
	@Override
	public void sendOrderConfirmationEmail(Pedido obj) {
		SimpleMailMessage sm = preparedSimpleMailMessageFromPedido(obj);
		sendEmail(sm);
	}

	protected SimpleMailMessage preparedSimpleMailMessageFromPedido(Pedido obj) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(obj.getCliente().getEmail());
		sm.setFrom(sender);
		sm.setSubject("Pedido confirmado! Código: " + obj.getId());
		//garante que vai ser a data do servidor
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(obj.toString());
		return sm;
	}
	
	public void sendOrderConfirmationHtmlEmail(Pedido obj) {
		try {
			MimeMessage mm = preparedMimeMessageFromPedido(obj);
			sendHtmlEmail(mm);
		} catch (MessagingException e) {
			sendOrderConfirmationEmail(obj);
		}
	}

	private MimeMessage preparedMimeMessageFromPedido(Pedido obj) throws MessagingException {
		MimeMessage mm = javaMailSender.createMimeMessage();
		MimeMessageHelper mmh = new MimeMessageHelper(mm, true);
		mmh.setTo(obj.getCliente().getEmail());
		mmh.setFrom(sender);
		mmh.setSubject("Pedido confirmado! Código: " + obj.getId());
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		mmh.setText(htmlFromTemplatePedido(obj), true);
		return mm;
	}

	protected String htmlFromTemplatePedido(Pedido obj) {
		//acessar o template. a partir dele, envia o pedido p template
		Context context = new Context();
		context.setVariable("pedido", obj);
		//processa o template e retorna o html em forma de string
		return templateEngine.process("email/confirmacaoPedido", context);
	}
}
