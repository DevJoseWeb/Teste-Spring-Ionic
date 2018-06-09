package com.nelioalves.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Estado;
import com.nelioalves.cursomc.repositories.EstadoRepository;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class EstadoService {

	@Autowired
	private EstadoRepository repo;
	
	public List<Estado> findAll() {
		Optional<List<Estado>> list = Optional.ofNullable(repo.findAllByOrderByNome());
		return list
				.orElseThrow(() -> new ObjectNotFoundException("Objetos não encontrados! " + Estado.class.getName()));
	}
}