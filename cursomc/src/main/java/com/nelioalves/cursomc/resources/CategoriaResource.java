package com.nelioalves.cursomc.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//classe é um controlador rest
@RestController
//vai responder pelo endpoint abaixo. padrao rest é no plural
@RequestMapping(value="/categorias")
public class CategoriaResource {

	//tem q usar os verbos http p cada operacao. obtendo um dado
	@RequestMapping(method=RequestMethod.GET)
	public String listar() {
		return "REST está funcionando!";
	}
}
