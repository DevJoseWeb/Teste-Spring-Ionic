package com.nelioalves.cursomc.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
	public ResponseEntity<Categoria> find(@PathVariable Integer id) {
		//n é bom um metodo no controlador rest ser grande com try e catch
		/*quando der erro no categoria service, classe objectnotfound é acionada, a classe 
		 * resourceexception tbm é acionada, q recebe o request e a mensagem do error, preenche a 
		 * classe standarderror com as informacoes e retorna em formato json*/
		Categoria obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	//resposta http(responseentity) e n vai ter corpo(void)
	//p obj ser construido atraves dos dados json q enviar, coloca a anotacao, faz o json ser convertido p objeto
	public ResponseEntity<Void> insert(@RequestBody Categoria obj) {
		//quando insere, o banco de dados atribui um novo id ao obj
		obj = service.insert(obj);
		//pega a id e fornece como argumento p a uri
		/* envia a nova uri como resposta a requisicao q cria com sucesso uma nova categoria
		 * localhost:8080/categorias/3
		 * */
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		//gera o codigo 201
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@RequestBody Categoria obj, @PathVariable Integer id) {
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Categoria> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
}
