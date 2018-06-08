package com.nelioalves.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.dto.CategoriaDTO;
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
	// retorno, ja encapsula com varias informacoes de resposta http p um serviço
	// rest. <?> pode encontrar ou n
	public ResponseEntity<Categoria> find(@PathVariable Integer id) {
		// n é bom um metodo no controlador rest ser grande com try e catch
		/*
		 * quando der erro no categoria service, classe objectnotfound é acionada, a
		 * classe resourceexception tbm é acionada, q recebe o request e a mensagem do
		 * error, preenche a classe standarderror com as informacoes e retorna em
		 * formato json
		 */
		Categoria obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.POST)
	// resposta http(responseentity) e n vai ter corpo(void)
	// p obj ser construido atraves dos dados json q enviar, coloca a anotacao, faz
	// o json ser convertido p objeto
	// p ser validado antes de passar pelo metodo @ valid
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO objDto) {
		Categoria obj = service.fromDTO(objDto);
		// quando insere, o banco de dados atribui um novo id ao obj
		obj = service.insert(obj);
		// pega a id e fornece como argumento p a uri
		/*
		 * envia a nova uri como resposta a requisicao q cria com sucesso uma nova
		 * categoria localhost:8080/categorias/3
		 */
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		// gera o codigo 201
		return ResponseEntity.created(uri).build();
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO objDto, @PathVariable Integer id) {
		Categoria obj = service.fromDTO(objDto);
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Categoria> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<CategoriaDTO>> findAll() {
		List<Categoria> list = service.findAll();
		List<CategoriaDTO> listDto = list.stream().map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}

	/* requisicao passa pelo controlador rest, ja captura o objeto dto e 
	 * valida. validacoes customizadas ficam na camada de servico. */
	
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	// parametro opcional. http://localhost:8080/categorias/page?linesPerPage=3&page=0
	// /page?linesPerPage=3&page=2&direction=DESC
	public ResponseEntity<Page<CategoriaDTO>> findPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		Page<Categoria> list = service.findPage(page, linesPerPage, orderBy, direction);
		Page<CategoriaDTO> listDto = list.map(obj -> new CategoriaDTO(obj));
		return ResponseEntity.ok().body(listDto);
	}
}
