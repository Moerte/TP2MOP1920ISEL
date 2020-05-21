package tps.tp2.pack3Coleccoes;

import java.util.Arrays;

import tps.tp2.pack2Livros.Livro;  // estaremos a trabalhar com a classe Livro do pack2Livros

/**
 * Classe Coleccao, deve conter a descri��o de uma colec��o, com t�tulo, os seus
 * livros, colec��es e editores
 */
public class Coleccao {
	// n�mero m�ximo de obras de uma colec��o
	private static int MAXOBRAS = 20;

	// prefixo usual
	public static final String GENERALPREFIX = "  ";

	// t�tulo da colec��o
	private String titulo;

	// Array de livros, em que estas encontram-se sempre nos menores �ndices e
	// pela ordem de registo
	private Livro[] livros = new Livro[MAXOBRAS];

	// dever� conter sempre o n�mero de livros na colec��o
	private int numLivros = 0;

	// array de colec��es, estas devem ocupar sempre os menores �ndices
	private Coleccao[] coleccoes = new Coleccao[MAXOBRAS];

	// dever� conter sempre o n�mero de colec��es dentro da colec��o
	private int numColeccoes = 0;

	// Editores, tem as mesmas condicionantes que array de autores na classe
	// livro
	private String[] editores;

	/**
	 * Construtor; o t�tulo tem de ter pelo menos um caracter que n�o seja um espa�o
	 * (Character.isWhitespace); o array de editores devem ser pelo menos um e t�m
	 * as mesmas restri��es que os autores dos livros;
	 */
	public Coleccao(String titulo, String[] editores) {
		// titulo
		if (titulo == null || titulo.length() == 0 || (titulo.length() == 1 && Character.isWhitespace(titulo.charAt(0))))
			throw new IllegalArgumentException("O titulo tem de ter pelo menos um caracter");
		this.titulo = titulo;

		// Editores
		if (!Livro.validarNomes(editores))
			throw new IllegalArgumentException("O array de autores cont�m autores invalidos");
		// Valida se n�o h� repeti��es
		if (Livro.haRepeticoes(editores))
			throw new IllegalArgumentException("O array de autores cont�m autores repetidos");
		// Remove os espa�os extra
		for (int i = 0; i < editores.length; i++) {
			editores[i] = Livro.removeExtraSpaces(editores[i]);
		}
		// Faz uma c�pia do array de editores para o array privado de editores
		this.editores = Arrays.copyOf(editores, editores.length);
	}

	/**
	 * Obtem o titulo da colec��o
	 */
	public String getTitulo() {
		return this.titulo;
	}

	/**
	 * Obtem o n�mero total de p�ginas da colec��o, p�ginas dos livros e das
	 * colec��es
	 */
	public int getNumPaginas() {

		int numPag = 0;
		for (int i = 0; i < numColeccoes; i++) {
			if (this.coleccoes[i] != null) {
				for (int j = 0; j < this.coleccoes[i].livros.length; j++) {
					if (this.coleccoes[i].livros[j] != null)
						numPag += this.coleccoes[i].livros[j].getNumPaginas();
				}
			}

		}
		for (int i = 0; i < numLivros; i++) {
			if (this.livros[i] != null)
				numPag += this.livros[i].getNumPaginas();
		}
		return numPag;
	}

	/**
	 * As colec��es com mais de 5000 p�ginas nos seus livros directos t�m um
	 * desconto de 20% nesses livros. As colec��es em que o somat�rio de p�ginas das
	 * suas subcolec��es directas seja igual ou superior ao qu�druplo do n� de
	 * p�ginas da sua subcolec��o directa com mais p�ginas dever�o aplicar um
	 * desconto de 10% sobre os pre�os das suas subcolec��es
	 */
	public float getPreco() {
		float preco = 0;
		float auxPag = 0;
		for (int i = 0; i < numLivros; i++) {
			if (this.livros[i] != null) {
				preco += this.livros[i].getPreco();
			}
		}
		for (int i = 0; i < numColeccoes; i++) {
			if (this.coleccoes[i] != null) {
				auxPag = (auxPag < this.coleccoes[i].getNumPaginas()) ? this.coleccoes[i].getNumPaginas() : auxPag;
				preco += this.coleccoes[i].getPreco();
			}
		}

		float discount = getNumPaginas() > 5000 ? preco * 20 / 100
				: ((getNumPaginas() * 4) <= auxPag) ? preco * 10 / 100 : 0;

		return (float) (Math.round((preco - discount) * 100.0) / 100.0);
	}

	/**
	 * Adiciona um livro � colec��o se puder e este n�o seja null e a colec��o n�o
	 * ficar com livros iguais ao n�vel imediato da colec��o. Deve utilzar o m�todo
	 * getIndexOfLivro e getIndexOfColeccao
	 */
	public boolean addLivro(Livro livro) {

		if (livro != null && getIndexOfLivro(livro.getTitulo()) == -1 && getIndexOfColeccao(livro.getTitulo()) == -1) {
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
	 * 
	 * Adiciona uma colec��o � colec��o se puder, esta n�o seja null e a colec��o
	 * n�o ficar com obras imediatas com t�tulos repetidos. Deve utilizar o m�todo
	 * getIndexOfLivro e getIndexOfColeccao
	 */
	public boolean addColeccao(Coleccao col) {

		if (col != null && getIndexOfColeccao(col.getTitulo()) == -1 && getIndexOfLivro(col.getTitulo()) == -1) {
			for (int i = 0; i < this.coleccoes.length; i++) {
				if (this.coleccoes[i] == null) {
					this.coleccoes[i] = col;
					this.numColeccoes++;
					return true;
				}
			}

		}
		return false;
	}

	/**
	 * Devolve o index no array de livros onde estiver o livro com o nome
	 * pretendido. Devolve -1 caso n�o o encontre
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
	 * Devolve o index no array de colec��es onde estiver a colec��o com o nome
	 * pretendido. Devolve -1 caso n�o o encontre
	 */
	private int getIndexOfColeccao(String titulo) {
		int index = -1;
		for (int i = 0; i < numColeccoes; i++) {
			if (this.coleccoes[i] != null) {
				if (titulo.equals(this.coleccoes[i].getTitulo()))
					index = i;
			}
		}
		index = (getTitulo().equals(titulo))? index +1:index;
		return index;
	}

	/**
	 * Remove do array o livro com o t�tulo igual ao t�tulo recebido. Devolve o
	 * livro removido ou null caso n�o tenha encontrado o livro. Deve-se utilizar o
	 * m�todo getIndexOfLivro. Recorda-se que os livros devem ocupar sempre os
	 * menores �ndices, ou seja, n�o pode haver nulls entre os livros
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
	 * Remove do array de colec��es a colec��o com o t�tulo igual ao t�tulo
	 * recebido. Devolve a colec��o removida ou null caso n�o tenha encontrado.
	 * Deve-se utilizar o m�todo getIndexOfColeccao. Recorda-se que as colec��es
	 * devem ocupar sempre os menores �ndices, ou seja, n�o pode haver nulls entre
	 * elas
	 */
	public Coleccao remColeccao(String titulo) {

		int index = getIndexOfColeccao(titulo);
		Coleccao toRemove = (index == -1) ? null : this.coleccoes[index];
		if (index > -1) {
			Coleccao[] newColecoes = new Coleccao[this.coleccoes.length - 1];
			this.numColeccoes--;
			for (int i = 0, k = 0; i < this.coleccoes.length; i++) {
				if (i == index)
					continue;
				newColecoes[k++] = this.coleccoes[i];

			}

			this.coleccoes = Arrays.copyOf(newColecoes, newColecoes.length);
		}
		return toRemove;
	}

	/**
	 * Devolve o n� de obras de uma pessoa. Cada colec��o deve contabilizar-se como
	 * uma obra para os editores.
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
		for (int i = 0; i < numColeccoes; i++) {
			if (this.coleccoes[i] != null) {
				autoreEditores = this.coleccoes[i].getAutoresEditores();
				for (int j = 0; j < autoreEditores.length; j++) {
					if (autorEditor.equals(autoreEditores[j])) {
						countLivros++;
					}
				}
				for (int j = 0; j < this.coleccoes[i].livros.length; j++) {
					if (this.coleccoes[i].livros[j] != null) {
						if (this.coleccoes[i].livros[j].contemAutor(autorEditor))
							countLivros++;
					}
				}
			}
		}
		return countLivros;
	}

	/**
	 * Devolver um novo array (sem nulls) com os livros de que a pessoa recebida �
	 * autor. N�o deve conter repeti��es, para excluir as repeti��es devem utilizar
	 * o m�todo mergeWithoutRepetitions
	 */
	public Livro[] getLivrosComoAutor(String autorNome) {
		int count = 0, k = 0;
		for (int i = 0; i < numLivros; i++) {
			if (this.livros[i] != null) {
				if (this.livros[i].contemAutor(autorNome))
					count++;
			}
		}
		for (int i = 0; i < numColeccoes; i++) {
			if (this.coleccoes[i] != null) {
				for (int j = 0; j < this.coleccoes[i].livros.length; j++) {
					if (this.coleccoes[i].livros[j] != null) {
						if (this.coleccoes[i].livros[j].contemAutor(autorNome))
							count++;
					}
				}
			}
		}

		Livro[] newList = new Livro[count];
		for (int i = 0; i <numLivros; i++) {
			if (this.livros[i] != null) {
				if (this.livros[i].contemAutor(autorNome)) {
					newList[i] = this.livros[i];
					k++;
				}
			}
		}
		for (int i = 0; i < numColeccoes; i++) {
			if (this.coleccoes[i] != null) {
				for (int j = 0; j < this.coleccoes[i].livros.length; j++) {
					if (this.coleccoes[i].livros[j] != null) {
						if (this.coleccoes[i].livros[j].contemAutor(autorNome))
							newList[k++] = this.coleccoes[i].livros[j];
					}
				}
			}
		}
		return newList;
	}

	/**
	 * Deve devolver uma string compat�vel com os outputs desejados
	 */
	public String toString() {
		
		return "Cole��o " + getTitulo() + ", editores " + Arrays.toString(getAutoresEditores()) + ", " + this.numLivros
				+ " livros, " + getNumPaginas() + "p " + getPreco() + "�";
	}

	/**
	 * Deve devolver um array, sem nulls, com todos os autores e editores existentes
	 * na colec��o. O resultado n�o deve conter repeti��es. Deve utilizar o m�todo
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
	 * M�todo que recebendo dois arrays sem repeti��es devolve um novo array com
	 * todos os elementos dos arrays recebidos mas sem repeti��es
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
	 * M�todo id�ntico ao m�todo anterior mas agora com arrays de livros
	 */
	private static Livro[] mergeWithoutRepetitions(Livro[] a1, Livro[] a2) {
		Livro[] test = new Livro[a1.length + a2.length];
		int current = 0;
		for (int i = 0; i < a1.length; i++) {
			if (a1[i] != null)
				test[current++] = a1[i];
		}
		for (int j = 0; j < a2.length; j++) {
			boolean isRepition = false;
			if (a2[j] != null) {
				for (int k = 0; k < current; k++) {
					if ((a2[j]).equals(test[k])) {
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
		Livro[] result = new Livro[current];
		for (int i = 0; i < current; i++) {
			result[i] = test[i];
		}
		return result;
	}

	/**
	 * Devolve true caso a colec��o recebida tenha o mesmo t�tulo e a mesma lista de
	 * editores. Para verificar verificar se os editores s�o os mesmos devem
	 * utilizar o m�todo mergeWithoutRepetitions
	 */
	public boolean equals(Coleccao c) {
		String[] test = mergeWithoutRepetitions(this.editores, c.editores);
		return (c != null) && getTitulo().equalsIgnoreCase(c.getTitulo())
				&& (test.length == this.editores.length && test.length == c.editores.length);
	}

	/**
	 * Mostra uma colec��o segundo os outputs desejados
	 */
	public void print(String prefix) {
	
		System.out.println(prefix + this);
		for (int j = 0; j < this.livros.length; j++) {
			if(this.livros[j] != null) this.livros[j].print(" "+prefix);
		}
		for (int i = 0; i < numColeccoes; i++) {
			if(this.coleccoes[i] != null) this.coleccoes[i].print(" "+ prefix);
		}
		
	}

	/**
	 * main
	 */
	public static void main(String[] args) {
		Livro l1 = new Livro("Viagem aos Himalaias", 340, 12.3f, new String[] { "Jo�o Mendon�a", "M�rio Andrade" });
		Livro l2 = new Livro("Viagem aos Pirin�us", 270, 11.5f, new String[] { "Jo�o Mendon�a", "J�lio Pomar" });

		Coleccao c1 = new Coleccao("Primavera", new String[] { "Jo�o Mendon�a", "Manuel Alfazema" });

		boolean res;

		res = c1.addLivro(l1);
		res = c1.addLivro(l2);
		System.out.println("c1 -> " + c1);
		c1.print("");
		System.out.println();

		// adicionar um livro com nome de outro j� existente
		res = c1.addLivro(l2);
		System.out.println("adi��o novamente de Viagem aos Pirin�us a c1 -> " + res);
		System.out.println("c1 -> " + c1);
		System.out.println();

		// Outra colec��o
		Livro l21 = new Livro("Viagem aos Himalaias 2", 340, 12.3f, new String[] { "Jo�o Mendon�a", "M�rio Andrade" });
		Livro l22 = new Livro("Viagem aos Pirin�us 2", 270, 11.5f, new String[] { "Jo�o Mendon�a", "J�lio Pomar" });
		Livro l23 = new Livro("Eu, Robo", 253, 17.6f, new String[] { "Isaac Asimov" });

		Coleccao cx2 = new Coleccao("Outono", new String[] { "Jo�o Mendon�a", "Manuel Antunes" });
		cx2.addLivro(l21);
		cx2.addLivro(l22);
		cx2.addLivro(l23);
		System.out.println("cx2 -> " + cx2);
		cx2.print("");
		System.out.println();

		// adicion�-la a c1
		c1.addColeccao(cx2);
		System.out.println("c1 ap�s adi��o da colec��o cx2 -> " + c1);
		c1.print("");
		System.out.println();

		// get editores autores
		String[] ae = c1.getAutoresEditores();
		System.out.println("Autores editores of c1 -> " + Arrays.toString(ae));
		System.out.println();

		// getNumObrasFromPerson
		String nome = "Jo�o Mendon�a";
		int n = c1.getNumObrasFromPerson(nome);
		System.out.println("N� de obras de " + nome + " -> " + n);
		System.out.println();

		// getLivrosComoAutor
		nome = "Jo�o Mendon�a";
		Livro[] obras = c1.getLivrosComoAutor(nome);
		System.out.println("Livros de " + nome + " -> " + Arrays.toString(obras));
		System.out.println();

		// Merge of Books
		Livro[] test = new Livro[MAXOBRAS];
		int k = 0;
		test[k++] = new Livro("Codex 632", 550, 22.21f, new String[] { "Jos� Rodrigues dos Santos" });
		for (int i = k; i < c1.livros.length; i++) {
			test[i] = c1.livros[i-1];
			k++;
		}
		Livro[] testMerge = mergeWithoutRepetitions(c1.livros, test);
		System.out.println("Livros de C1: " + Arrays.toString(c1.livros));
		System.out.println("Livros de Test: " + Arrays.toString(test)+ "\n");
		System.out.println("Teste ao Merge dos Livros: " + Arrays.toString(testMerge));
		System.out.println();
		
		// rem livro
		String nomeLivro = "Viagem aos Himalaias";
		Livro l = c1.remLivro(nomeLivro);
		System.out.println("Remo��o de " + nomeLivro + " -> " + l);
		c1.print("");
		System.out.println();
		
		// Adicionar livro com nome igual a uma cole��o
		Livro l3 = new Livro("Primavera", 222 , 22.2f, new String[] {"Nuno Oliveira"});
		System.out.println("Tentativa de inser��o de um livro com o mesmo titulo que uma cole��o: "+c1.addLivro(l3));
		c1.print("");

	}
}