package com.nelioalves.cursomc.domain.enums;

public enum Perfil {
	ADMIN(1, "ROLE_ADMIN"),// descricao p admin é exigencia do framework 
	CLIENTE(2, "ROLE_CLIENTE");

	private int cod;
	private String descricao;

	private Perfil(int cod, String descricao) {
		this.descricao = descricao;
		this.cod = cod;
	}

	public String getDescricao() {
		return descricao;
	}

	public int getCod() {
		return cod;
	}

	public static Perfil toEnum(Integer cod) {
		if (cod == null)
			return null;

		for (Perfil x : Perfil.values())
			if (cod.equals(x.getCod()))
				return x;

		throw new IllegalArgumentException("Id inválido: " + cod);
	}
}
