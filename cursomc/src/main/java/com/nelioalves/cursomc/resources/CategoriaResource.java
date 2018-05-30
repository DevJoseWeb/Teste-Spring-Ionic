package com.nelioalves.cursomc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.services.CategoriaService;

//classe é um controlador rest
@RestController
// vai responder pelo endpoint abaixo. padrao rest é no plural
@RequestMapping(value = "/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaService service;

	// tem q usar os verbos http p cada operacao. obtendo um dado
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	// @PathVariable, p saber q id da url tem q ir p parametro do metodo
	// retorno, ja encapsula com varias informacoes de resposta http p um serviço rest. <?> pode encontrar ou n
	public ResponseEntity<?> find(@PathVariable Integer id) {
		Categoria obj = service.buscar(id);
		return ResponseEntity.ok().body(obj);
	}
}
