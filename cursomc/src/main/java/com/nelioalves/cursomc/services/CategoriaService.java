package com.nelioalves.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.dto.CategoriaDTO;
import com.nelioalves.cursomc.repositories.CategoriaRepository;
import com.nelioalves.cursomc.security.UserSS;
import com.nelioalves.cursomc.services.exceptions.AuthorizationException;
import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	// a dependencia é automaticamente instanciada pelo spring atraves da injecao de
	// dependencias ou inversao de controle
	@Autowired
	private CategoriaRepository repo;
	
	@Value("${img.profile.size}")
	private Integer size;
	
	@Value("${img.prefix.category}")
	private String prefix;
	
	@Autowired
	private ImageService imgService;
	
	@Autowired
	private S3Service s3service;

	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}

	public Categoria insert(Categoria obj) {
		// se n tiver nulo o save vai entender como uma atualizacao e n uma insercao
		obj.setId(null);
		return repo.save(obj);
	}

	public Categoria update(Categoria obj) {
		Categoria newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(obj);
	}

	private void updateData(Categoria newObj, Categoria obj) {
		newObj.setNome(obj.getNome());
	}

	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir uma categoria que possui produtos");
		}
	}

	public List<Categoria> findAll() {
		Optional<List<Categoria>> list = Optional.ofNullable(repo.findAll());
		return list
				.orElseThrow(() -> new ObjectNotFoundException("Objetos não encontrados!" + Categoria.class.getName()));
	}
	
	//pagina q quer, linhas po pagina, ordenado po q atributo, ascendente ou descendente
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
//		prepara as informacoes, p q faca a consulta e retorne a pagina de dados
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Categoria fromDTO(CategoriaDTO objDto) {
		return new Categoria(objDto.getId(), objDto.getNome());
	}
	
//	public URI uploadProfilePicture(MultipartFile multipartFile) {
//		Optional<UserSS> userOptional = Optional.ofNullable(UserService.authenticated());
//		userOptional.orElseThrow(() -> new AuthorizationException("Acesso negado"));
//		UserSS user = userOptional.get();
//		
//		BufferedImage jpgImg = imgService.getJpgImageFromFile(multipartFile);
//		jpgImg = imgService.cropSquare(jpgImg);
//		jpgImg = imgService.resize(jpgImg, size);
//		
//		String fileName = prefix + user.getId() + ".jpg";
//		
//		return s3service.uploadFile(imgService.getInputStream(jpgImg, "jpg"), fileName, "image");
//	}
}
