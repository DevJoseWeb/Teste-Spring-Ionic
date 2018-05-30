package aplicacao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.swing.JOptionPane;

import dominio.Pessoa;

public class Programa {
	private static EntityManagerFactory emf = null;
	private static EntityManager em = null;

	public static void main(String[] args) {
		// Pessoa p1 = new Pessoa("Carlos da Silva", "carlos@gmail.com");
		// Pessoa p2 = new Pessoa("Joaquim Torres", "joaquim@gmail.com");
		// Pessoa p3 = new Pessoa("Ana Maria", "ana@gmail.com");

		// salvar(p1, p2, p3);
		// System.out.println("salvo");

		Pessoa p = buscar(1);
		System.out.println(p);

		// remover(3);
		// System.out.println("removido");
	}

	public static void salvar(Pessoa... p) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("teste-jpa");
		EntityManager em = emf.createEntityManager();
		// n faz uma leitura do bd, tem q iniciar uma transacao com o bd
		em.getTransaction().begin();

		em.persist(p[0]);
		em.persist(p[1]);
		em.persist(p[2]);

		// confirmar as alteracoes
		em.getTransaction().commit();
		em.close();
		emf.close();
	}

	public static Pessoa buscar(int id) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("teste-jpa");
		EntityManager em = emf.createEntityManager();

		Pessoa p = em.find(Pessoa.class, id);

		em.close();
		emf.close();
		return p;
	}
	
	public static void remover(int id) {
		// objeto monitorado. q acabou de inserir ou q buscou e n fechou o em
		EntityManagerFactory emf = null;
		EntityManager em = null;
		try {
			emf = Persistence.createEntityManagerFactory("teste-jpa");
			em = emf.createEntityManager();
			em.getTransaction().begin();

			Pessoa p = em.find(Pessoa.class, id);
			em.remove(p);

			em.getTransaction().commit();
		} catch (PersistenceException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} finally {
			em.close();
			emf.close();
		}
	}

	/*public static void abrir() {
		emf = Persistence.createEntityManagerFactory("teste-jpa");
		em = emf.createEntityManager();
	}

	public static void fechar() {
		em.close();
		emf.close();
	}*/
}
