package com.nelioalves.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Cidade;
import com.nelioalves.cursomc.repositories.CidadeRepository;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CidadeService {
	
	@Autowired
	private CidadeRepository repo;

	public List<Cidade> findByEstado(Integer estadoId) {
		Optional<List<Cidade>> obj = Optional.ofNullable(repo.findCidades(estadoId));
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objetos não encontrados! " + Cidade.class.getName()));
	}
}