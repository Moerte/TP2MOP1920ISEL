package tps.tp2.pack2Livros;

import java.util.Arrays;

/**
 * Classe Colecca, deve conter a descrição de uma colecção, com título, seus
 * livros e editores
 */
public class Coleccao {

	// número máximo de obras de uma colecção
	private static int MAXOBRAS = 20;

	// prefixo usual
	public static final String GENERALPREFIX = "  ";

	// título da colecção
	private String titulo;

	// Array de livros, em que estas encontram-se sempre nos menores índices e
	// pela ordem de registo
	private Livro[] livros = new Livro[MAXOBRAS];

	// deverá conter sempre o número de livros na colecção
	private int numLivros = 0;

	// Editores, tem as mesmas condicionantes que array de autores na classe
	// livro
	private String[] editores;

	/**
	 * Construtor; o título tem de ter pelo menos um caracter que não seja um espaço
	 * (Character.isWhitespace); o array de editores devem ser pelo menos um e têm
	 * as mesmas restrições que os autores dos livros; o array de livros deve conter
	 * os mesmos sempre nos menores índices
	 */
	public Coleccao(String titulo, String[] editores) {
		// titulo
		if (titulo == null || titulo.length() == 0
				|| (titulo.length() == 1 && Character.isWhitespace(titulo.charAt(0))))
			throw new IllegalArgumentException("O titulo tem de ter pelo menos um caracter");
		this.titulo = titulo;

		// Editores
		if (!Livro.validarNomes(editores))
			throw new IllegalArgumentException("O array de autores contém autores invalidos");
		// Valida se não há repetições
		if (Livro.haRepeticoes(editores))
			throw new IllegalArgumentException("O array de autores contém autores repetidos");
		// Remove os espaços extra
		for (int i = 0; i < editores.length; i++) {
			editores[i] = Livro.removeExtraSpaces(editores[i]);
		}
		// Faz uma cópia do array de editores para o array privado de editores
		this.editores = Arrays.copyOf(editores, editores.length);
	}

	/**
	 * Obtem o titulo da colecção
	 */
	public String getTitulo() {
		return this.titulo;
	}

	/**
	 * Obtem o número total de páginas da colecção
	 */
	public int getNumPaginas() {
		int numPag = 0;
		for (int i = 0; i < numLivros; i++) {
			if (this.livros[i] != null)
				numPag += this.livros[i].getNumPaginas();
		}
		return numPag;
	}

	/**
	 * Devolve o preço da colecção tendo em conta que as colecções com 4 ou mais
	 * livros têm um desconto de 20% nos livros que custam pelo menos 10 euros e que
	 * têm mais de 200 páginas
	 */
	public float getPreco() {

		float preco = 0;
		for (int i = 0; i < numLivros; i++) {
			if (this.livros[i] != null) {
				float aux = 0;
				if (numLivros >= 4 && this.livros[i].getPreco() >= 10.0
						&& this.livros[i].getNumPaginas() > 200) {
					aux += (this.livros[i].getPreco() - this.livros[i].getPreco()*20/100);
				}
				preco += (aux > 0) ? aux : this.livros[i].getPreco();
			}
		}
		return (float) (Math.round(preco* 100.0) / 100.0);
	}

	/**
	 * Adiciona um livro se puder e este não seja null e a colecção não ficar com
	 * livros iguais. Deve utilzar o método getIndexOfLivro.
	 */
	public boolean addLivro(Livro livro) {

		if (livro != null && getIndexOfLivro(livro.getTitulo()) == -1) {
			for (int i = 0; i < this.livros.length; i++) {
				if (this.livros[i] == null) {
					this.livros[i] = livro;
					this.numLivros++;
					return true;
				}
			}

		}
		return false;
	}

	/**
	 * Devolve o index no array de livros onde estiver o livro com o nome
	 * pretendido. Devolve -1 caso não o encontre
	 */
	private int getIndexOfLivro(String titulo) {

		int index = -1;
		for (int i = 0; i < numLivros; i++) {
			if (this.livros[i] != null) {
				if (titulo.equals(this.livros[i].getTitulo()))
					index = i;
			}
		}
		return index;
	}

	/**
	 * Remove do array o livro com o título igual ao título recebido. Devolve o
	 * livro removido ou null caso não tenha encontrado o livro. Deve-se utilizar o
	 * método getIndexOfLivro. Recorda-se que os livros devem ocupar sempre os
	 * menores índices, ou seja, não pode haver nulls entre os livros
	 */
	public Livro remLivro(String titulo) {

		int index = getIndexOfLivro(titulo);
		Livro toRemove = (index == -1) ? null : this.livros[index];
		if (index > -1) {
			Livro[] newLivros = new Livro[this.livros.length - 1];
			this.numLivros--;
			for (int i = 0, k = 0; i < this.livros.length; i++) {
				if (i == index)
					continue;
				newLivros[k++] = this.livros[i];

			}

			this.livros = Arrays.copyOf(newLivros, newLivros.length);
		}
		return toRemove;
	}

	/**
	 * Devolve o nº de obras de uma pessoa. A colecção deve contabilizar-se como uma
	 * obra para os editores.
	 */
	public int getNumObrasFromPerson(String autorEditor) {
		int countLivros = 0;
		String[] autoreEditores = getAutoresEditores();
		for (int i = 0; i < autoreEditores.length; i++) {
			if (autorEditor.equals(autoreEditores[i]))
				countLivros++;
		}
		for (int i = 0; i < numLivros; i++) {
			if (this.livros[i] != null) {
				if (this.livros[i].contemAutor(autorEditor))
					countLivros++;
			}
		}

		return countLivros;
	}

	/**
	 * Devolver um novo array (sem nulls) com os livros de que a pessoa recebida é
	 * autor
	 */
	public Livro[] getLivrosComoAutor(String autorNome) {
		int count = 0;
		for (int i = 0; i < this.livros.length; i++) {
			if (this.livros[i] != null) {
				if (this.livros[i].contemAutor(autorNome))
					count++;
			}
		}
		Livro[] newList = new Livro[count];
		for (int i = 0; i < numLivros; i++) {
			if (this.livros[i] != null) {
				if (this.livros[i].contemAutor(autorNome))
					newList[i] = this.livros[i];
			}
		}
		return newList;
	}

	/**
	 * Deve devolver uma string compatível com os outputs desejados
	 */
	public String toString() {
		return "Coleção " + getTitulo() + ", editores " + Arrays.toString(getAutoresEditores()) + ", " + this.numLivros
				+ " livros, " + getNumPaginas() + "p " + getPreco() + "€";
	}

	/**
	 * Deve devolver um array, sem nulls, com todos os autores e editores existentes
	 * na colecção. O resultado não deve conter repetições. Deve utilizar o método
	 * mergeWithoutRepetitions
	 */
	public String[] getAutoresEditores() {
		String[] aux1 = new String[this.livros.length];
		for (int i = 0; i < numLivros; i++) {
			if (this.livros[i] != null) {
				String[] aux = this.livros[i].getAutores();
				for (int j = 0; j < aux.length; j++) {
					aux1[i] = aux[j];
				}
			}
		}
		String[] result = mergeWithoutRepetitions(this.editores, aux1);
		return result;
	}

	/**
	 * Método que recebendo dois arrays sem repetições devolve um novo array com
	 * todos os elementos dos arrays recebidos mas sem repetições
	 */
	private static String[] mergeWithoutRepetitions(String[] a1, String[] a2) {

		String[] test = new String[a1.length + a2.length];
		int current = 0;
		for (int i = 0; i < a1.length; i++) {
			if (a1[i] != null)
				test[current++] = a1[i];
		}
		for (int j = 0; j < a2.length; j++) {
			boolean isRepition = false;
			if (a2[j] != null) {
				for (int k = 0; k < current; k++) {
					if (a2[j].equalsIgnoreCase(test[k])) {
						isRepition = true;
						break;
					}
				}
				if (isRepition) {
					continue;
				} else {
					test[current++] = a2[j];
				}
			} else {
				continue;
			}
		}
		String[] result = new String[current];
		for (int i = 0; i < current; i++) {
			result[i] = test[i];
		}
		return result;
	}

	/**
	 * Devolve true caso a colecção recebida tenha o mesmo título e a mesma lista de
	 * editores. Para verificar verificar se os editores são os mesmos devem
	 * utilizar o método mergeWithoutRepetitions
	 */
	public boolean equals(Coleccao c) {
		String[] test = mergeWithoutRepetitions(this.editores, c.editores);
		return (c != null) && getTitulo().equalsIgnoreCase(c.getTitulo())
				&& (test.length == this.editores.length && test.length == c.editores.length);
	}

	/**
	 * Mostra uma colecção segundo os outputs desejados
	 */
	public void print(String prefix) {
		String strLivros = "";
		for (int i = 0; i < numLivros; i++) {
			if (this.livros[i] != null) {
				strLivros += "\n "+ this.livros[i];	
			}
		}
		System.out.println(prefix + "" + toString() + strLivros);
	}

	/**
	 * main
	 */
	public static void main(String[] args) {
		Livro l1 = new Livro("Viagem aos Himalaias", 340, 12.3f, new String[] { "João Mendonça", "Mário Andrade" });
		Livro l2 = new Livro("Viagem aos Pirinéus", 270, 11.5f, new String[] { "João Mendonça", "Júlio Pomar" });
		Livro l3 = new Livro("Eu, Robo", 253, 17.6f, new String[] { "Isaac Asimov" });
		Livro l4 = new Livro("Fundação", 256, 11.1f, new String[] { "Isaac Asimov"});

		Coleccao c1 = new Coleccao("Primavera", new String[] { "João Mendonça", "Manuel Alfazema" });

		boolean res;

		res = c1.addLivro(l1);
		res = c1.addLivro(l2);
		System.out.println("c1 -> " + c1);
		c1.print("");
		System.out.println();

		// adicionar um livro com nome de outro já existente
		res = c1.addLivro(l2);
		System.out.println("adição novamente de Viagem aos Pirinéus a c1 -> " + res);
		System.out.println("c1 -> " + c1);
		System.out.println();

		// adicionar um livro valida
		res = c1.addLivro(l3);
		System.out.println("Adição de Eu, Robo a c1 -> " + res);
		System.out.println("c1 -> " + c1);
		System.out.println();
		
		//calculo da percentagem de desconto
		res = c1.addLivro(l4);
		System.out.println("[Calculo do desconto] Adição de Fundação a c1 -> " + res);
		System.out.println("c1 -> " + c1);
		System.out.println();


		// get editores autores
		String[] ae = c1.getAutoresEditores();
		System.out.println("Autores editores of c1 -> " + Arrays.toString(ae));
		System.out.println();

		// getNumObrasFromPerson
		String nome = "João Mendonça";
		int n = c1.getNumObrasFromPerson(nome);
		System.out.println("Nº de obras de " + nome + " -> " + n);
		System.out.println();

		// getNumObrasFromPerson (não existe)
		String newNome = "Nuno Oliveira";
		int x = c1.getNumObrasFromPerson(newNome);
		System.out.println("Nº de obras de " + newNome + " -> " + x);
		System.out.println();

		// getLivrosComoAutor
		nome = "João Mendonça";
		Livro[] obras = c1.getLivrosComoAutor(nome);
		System.out.println("Livros de " + nome + " -> " + Arrays.toString(obras));
		System.out.println();

		// getLivrosComoAutor (Não existe)
		Livro[] newObras = c1.getLivrosComoAutor(newNome);
		System.out.println("Livros de " + newNome + " -> " + Arrays.toString(newObras));
		System.out.println();

		// rem livro
		String nomeLivro = "Viagem aos Himalaias";
		Livro l = c1.remLivro(nomeLivro);
		System.out.println("Remoção de " + nomeLivro + " -> " + l);
		c1.print("");
		System.out.println();

		// rem livro invalida
		String nomeLivroToRemove = "Vaticanum";
		Livro lR = c1.remLivro(nomeLivro);
		System.out.println("Remoção de " + nomeLivroToRemove + " -> " + lR);
		c1.print("");
		System.out.println();

		// equals
		Coleccao c2 = new Coleccao("Primavera", new String[] { "João Mendonça", "Manuel Alfazema" });
		boolean same = c1.equals(c2);
		System.out.println("c2:");
		c2.print("");
		System.out.println("c1.equals(c2) -> " + same);
		System.out.println();

		Coleccao c3 = new Coleccao("Primavera", new String[] { "João Mendonça" });
		same = c1.equals(c3);
		System.out.println("c3:");
		c3.print("");
		System.out.println("c1.equals(c3) -> " + same);

	}
}
