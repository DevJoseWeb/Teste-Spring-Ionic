package com.nelioalves.cursomc.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/*serializable diz q os objetos da classe pode ser convertidos em 
 * uma sequencia de bits p q sejam gravados em arquivos, trafegado 
 * pela rede*/
@Entity
public class Categoria implements Serializable {
	//tem q ter numero de versao. primeira versao da classe
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String nome;

	//tratamento a referencia ciclica na serializacao do json
	//uma categoria tem varios produtos, um produto tem varias categorias, p n rodar infinitamente tem q cortar isso
	//faz isso do lado q quer q venha os objetos associados
	//retira pois sem ele, ira serializar automaticamente
	//@JsonManagedReference
	//mapeamento foi feito em cima do atributo categorias
	@ManyToMany(mappedBy="categorias")
	private List<Produto> produtos = new ArrayList<>();
	
	public Categoria() {
		super();
	}

	public Categoria(Integer id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Categoria other = (Categoria) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Categoria [id=" + id + ", nome=" + nome + ", produtos=" + produtos + "]";
	}
}
