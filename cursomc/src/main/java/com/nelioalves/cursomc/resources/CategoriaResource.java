package com.nelioalves.cursomc.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nelioalves.cursomc.domain.Categoria;

//classe é um controlador rest
@RestController
//vai responder pelo endpoint abaixo. padrao rest é no plural
@RequestMapping(value="/categorias")
public class CategoriaResource {

//	tem q usar os verbos http p cada operacao. obtendo um dado
	@RequestMapping(method=RequestMethod.GET)
	public List<Categoria> listar() {
		Categoria cat1 = new Categoria(1, "Informática");
		Categoria cat2 = new Categoria(2, "Escritório");
		
		List<Categoria> lista = new ArrayList<>();
		lista.addAll(Arrays.asList(cat1, cat2));
		
		return lista;
	}
}
