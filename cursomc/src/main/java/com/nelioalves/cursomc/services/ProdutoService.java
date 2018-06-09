package com.nelioalves.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.domain.Produto;
import com.nelioalves.cursomc.repositories.CategoriaRepository;
import com.nelioalves.cursomc.repositories.ProdutoRepository;
import com.nelioalves.cursomc.security.UserSS;
import com.nelioalves.cursomc.services.exceptions.AuthorizationException;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Value("${img.profile.size}")
	private Integer size;

	@Value("${img.prefix.product}")
	private String prefix;

	@Autowired
	private ImageService imgService;

	@Autowired
	private S3Service s3service;

	@Autowired
	private ProdutoRepository repo;

	@Autowired
	private CategoriaRepository categoriaRepository;

	public Produto find(Integer id) {
		Optional<Produto> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Produto.class.getName()));
	}

	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy,
			String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		return repo.search(nome, categorias, pageRequest);
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
//		// normal
//		s3service.uploadFile(imgService.getInputStream(jpgImg, "jpg"), fileName, "image");
//
//		return s3service.uploadFile(imgService.getInputStream(jpgImg, "jpg"), fileName, "image");
//	}
}
