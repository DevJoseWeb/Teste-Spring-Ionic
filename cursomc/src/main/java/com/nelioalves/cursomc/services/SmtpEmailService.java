package com.nelioalves.cursomc.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class SmtpEmailService extends AbstractEmailService {

	//quando o objeto for instanciado, ira adicionar 
	//todas as informacoes no .properties q foi informado
	@Autowired
	private MailSender mailSender;

	private static final Logger LOG = LoggerFactory.getLogger(MockEmailService.class);

	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Enviando de email...");
		mailSender.send(msg);
		LOG.info("Email enviado");
	}

}
