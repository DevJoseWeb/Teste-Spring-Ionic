package com.nelioalves.cursomc.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.domain.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

	// como é uma consulta, n é necessario fazer uma transacao
	@Transactional(readOnly = true)
	/*se usar o padrao de nomes do data jpa, n precisa implementar o corpo do
	 * metodo. jpql se baseia no nome da classe e atributos, entao o nome deve ser
	 * igual
	 */
	@Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias")
	/*coloca o atributo do metodo, dentro do atributo que esta entre "" da query do
	 * jpql. : se refere q o atributo sera substituido por outro informado
	 */
	Page<Produto> search(@Param("nome") String nome, @Param("categorias") List<Categoria> categorias,
			Pageable pageRequest);

	// faz a mesma consulta, só q usando o padrao de nomes do spring data jpa
	// Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome,
	// List<Categoria> categorias, Pageable pageRequest);
}
