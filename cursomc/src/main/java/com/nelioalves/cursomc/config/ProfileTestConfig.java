package com.nelioalves.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.nelioalves.cursomc.services.DBService;
import com.nelioalves.cursomc.services.EmailService;
import com.nelioalves.cursomc.services.MockEmailService;

//configurando o profile pela classe
@Configuration
@Profile("test")
public class ProfileTestConfig {

	@Autowired
	private DBService dbService;

	@Bean
	public boolean instatiateDatabase() throws ParseException {
		dbService.instantiateTestDatabase();
		return true;
	}
	
	/*o metodo vai ta disponivel como componente no sistema. se em outra classe 
	fizer uma injecao de depencia, injetando uma instancia
	de emailservice, o spring vai procurar um componente q pode ser um '@Bean' 
	q devolve uma instancia dele. no caso vai encontrar
	um metodo q retorna uma instancia dele.*/
	@Bean
	public EmailService emailService() {
		return new MockEmailService();
	}
}
