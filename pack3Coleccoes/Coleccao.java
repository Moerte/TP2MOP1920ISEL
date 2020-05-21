package tps.tp2.pack3Coleccoes;

import java.util.Arrays;

import tps.tp2.pack2Livros.Livro;  // estaremos a trabalhar com a classe Livro do pack2Livros

/**
 * Classe Coleccao, deve conter a descrição de uma colecção, com título, os seus
 * livros, colecções e editores
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

	// array de colecções, estas devem ocupar sempre os menores índices
	private Coleccao[] coleccoes = new Coleccao[MAXOBRAS];

	// deverá conter sempre o número de colecções dentro da colecção
	private int numColeccoes = 0;

	// Editores, tem as mesmas condicionantes que array de autores na classe
	// livro
	private String[] editores;

	/**
	 * Construtor; o título tem de ter pelo menos um caracter que não seja um espaço
	 * (Character.isWhitespace); o array de editores devem ser pelo menos um e têm
	 * as mesmas restrições que os autores dos livros;
	 */
	public Coleccao(String titulo, String[] editores) {
		// titulo
		if (titulo == null || titulo.length() == 0 || (titulo.length() == 1 && Character.isWhitespace(titulo.charAt(0))))
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
	 * Obtem o número total de páginas da colecção, páginas dos livros e das
	 * colecções
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
	 * As colecções com mais de 5000 páginas nos seus livros directos têm um
	 * desconto de 20% nesses livros. As colecções em que o somatório de páginas das
	 * suas subcolecções directas seja igual ou superior ao quádruplo do nº de
	 * páginas da sua subcolecção directa com mais páginas deverão aplicar um
	 * desconto de 10% sobre os preços das suas subcolecções
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
	 * Adiciona um livro à colecção se puder e este não seja null e a colecção não
	 * ficar com livros iguais ao nível imediato da colecção. Deve utilzar o método
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
	 * Adiciona uma colecção à colecção se puder, esta não seja null e a colecção
	 * não ficar com obras imediatas com títulos repetidos. Deve utilizar o método
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
	 * Devolve o index no array de colecções onde estiver a colecção com o nome
	 * pretendido. Devolve -1 caso não o encontre
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
	 * Remove do array de colecções a colecção com o título igual ao título
	 * recebido. Devolve a colecção removida ou null caso não tenha encontrado.
	 * Deve-se utilizar o método getIndexOfColeccao. Recorda-se que as colecções
	 * devem ocupar sempre os menores índices, ou seja, não pode haver nulls entre
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
	 * Devolve o nº de obras de uma pessoa. Cada colecção deve contabilizar-se como
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
	 * Devolver um novo array (sem nulls) com os livros de que a pessoa recebida é
	 * autor. Não deve conter repetições, para excluir as repetições devem utilizar
	 * o método mergeWithoutRepetitions
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
	 * Método idêntico ao método anterior mas agora com arrays de livros
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
		Livro l1 = new Livro("Viagem aos Himalaias", 340, 12.3f, new String[] { "João Mendonça", "Mário Andrade" });
		Livro l2 = new Livro("Viagem aos Pirinéus", 270, 11.5f, new String[] { "João Mendonça", "Júlio Pomar" });

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

		// Outra colecção
		Livro l21 = new Livro("Viagem aos Himalaias 2", 340, 12.3f, new String[] { "João Mendonça", "Mário Andrade" });
		Livro l22 = new Livro("Viagem aos Pirinéus 2", 270, 11.5f, new String[] { "João Mendonça", "Júlio Pomar" });
		Livro l23 = new Livro("Eu, Robo", 253, 17.6f, new String[] { "Isaac Asimov" });

		Coleccao cx2 = new Coleccao("Outono", new String[] { "João Mendonça", "Manuel Antunes" });
		cx2.addLivro(l21);
		cx2.addLivro(l22);
		cx2.addLivro(l23);
		System.out.println("cx2 -> " + cx2);
		cx2.print("");
		System.out.println();

		// adicioná-la a c1
		c1.addColeccao(cx2);
		System.out.println("c1 após adição da colecção cx2 -> " + c1);
		c1.print("");
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

		// getLivrosComoAutor
		nome = "João Mendonça";
		Livro[] obras = c1.getLivrosComoAutor(nome);
		System.out.println("Livros de " + nome + " -> " + Arrays.toString(obras));
		System.out.println();

		// Merge of Books
		Livro[] test = new Livro[MAXOBRAS];
		int k = 0;
		test[k++] = new Livro("Codex 632", 550, 22.21f, new String[] { "José Rodrigues dos Santos" });
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
		System.out.println("Remoção de " + nomeLivro + " -> " + l);
		c1.print("");
		System.out.println();
		
		// Adicionar livro com nome igual a uma coleção
		Livro l3 = new Livro("Primavera", 222 , 22.2f, new String[] {"Nuno Oliveira"});
		System.out.println("Tentativa de inserção de um livro com o mesmo titulo que uma coleção: "+c1.addLivro(l3));
		c1.print("");

	}
}