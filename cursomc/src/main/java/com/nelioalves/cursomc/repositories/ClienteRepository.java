package com.nelioalves.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nelioalves.cursomc.domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer>{

	/*n necessita em ser envolvida na transaçao do 
	banco de dados. deixa mais rapido e diminui o 
	locking no gerenciamento de transacoes do banco de dados*/
	@Transactional(readOnly=true)
	Cliente findByEmail(String email);
}
