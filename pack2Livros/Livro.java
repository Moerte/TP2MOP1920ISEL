package tps.tp2.pack2Livros;

import java.util.Arrays;

/**
 * Classe que deverá suportar um livro
 */
public class Livro {

	// Título do livro
	private String titulo;

	// número de páginas
	private int numPaginas;

	// preço do livro
	private float preco;

	// array de autores, este array não deve ter nulls
	private String[] autores;

	/**
	 * Deve criar um novo livro com os dados recebidos. O título não deve ser null
	 * nem vazio. O número de páginas não pode ser menor que 1. O preço não pode ser
	 * negativo. O array de autores não deve conter nem nulls e deve conter pelo
	 * menos um autor válido. Não pode haver repetições dos nomes dos autores,
	 * considera-se os nomes sem os espaços extra (ver removeExtraSpaces). Este
	 * método deve utilizar os métodos auxiliares existentes. Em caso de nome
	 * inválido deve lançar uma excepção de IllegalArgumentException com a indicação
	 * do erro ocorrido
	 */
	public Livro(String titulo, int numPaginas, float preco, String[] autores) {

		// título
		if (titulo == null || titulo.length() == 0)
			throw new IllegalArgumentException("O titulo tem de ter pelo menos um caracter");
		this.titulo = titulo;
		// Número de Páginas
		if (numPaginas < 1)
			throw new IllegalArgumentException("O nº de páginas não pode ser negativo");
		this.numPaginas = numPaginas;
		// Preço
		if (preco < 0.0f)
			throw new IllegalArgumentException("O preço não pode ser negativo");
		this.preco = preco;
		// Autores
		// Valida se nao há nulls ou números nos nomes dos autores
		if (!validarNomes(autores))
			throw new IllegalArgumentException("O array de autores contém autores invalidos");
		// Valida se não há repetições
		if (haRepeticoes(autores))
			throw new IllegalArgumentException("O array de autores contém autores repetidos");
		// Remove os espaços extra
		for (int i = 0; i < autores.length; i++) {
			autores[i] = removeExtraSpaces(autores[i]);
		}
		// Faz uma cópia do array de autores para o array privado de autores
		this.autores = Arrays.copyOf(autores, autores.length);

	}

	/**
	 * Devolve o título do livro
	 */
	public String getTitulo() {

		return this.titulo;
	}

	/**
	 * Devolve o número de páginas do livro
	 */
	public int getNumPaginas() {

		return this.numPaginas;
	}

	/**
	 * Devolve o preço do livro
	 */
	public float getPreco() {

		return this.preco;
	}

	/**
	 * Devolve uma cópia do array de autores do livro
	 */
	public String[] getAutores() {

		return this.autores;
	}

	/**
	 * Deve devolver true se o array conter apenas nomes válidos. Um nome é válido
	 * se conter pelo menos uma letra (Character.isLetter) e só conter letras e
	 * espaços (Character.isWhitespace). Deve chamar validarNome.
	 */
	public static boolean validarNomes(String[] nomes) {

		boolean answer = false;
		int count = 0;
		for (int i = 0; i < nomes.length; i++) {
			if (nomes[i] == null)
				return false;
			if (validarNome(nomes[i]))
				count++;
		}
		answer = count == nomes.length ? true : false;
		return answer;
	}

	/**
	 * Um nome válido se não for null e não conter pelo menos uma letra
	 * (Character.isLetter) e só conter letras e espaços (Character.isWhitespace)
	 */
	public static boolean validarNome(String nome) {
		if (nome == null || nome.length() == 0) {
			return false;
		}
		char[] chars = nome.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (!Character.isLetter(chars[i]) && !Character.isWhitespace(chars[i])) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Recebe um nome já previamente validado, ou seja só com letras ou espaços.
	 * Deve devolver o mesmo nome mas sem espaços (utilizar trim e
	 * Character.isWhitespace) no início nem no fim e só com um espaço ' ' entre
	 * cada nome. Deve utilizar um StringBuilder para ir contendo o nome já
	 * corrigido
	 */
	public static String removeExtraSpaces(String nome) {

		// nome = nome.trim().replaceAll("\\s+", " ");
		StringBuilder strNome = new StringBuilder();
		char[] chars = nome.trim().toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (Character.isLetter(chars[i])
					|| (!Character.isWhitespace(chars[i - 1]) && Character.isWhitespace(chars[i]))) {
				strNome.append(chars[i]);
			}
		}
		nome = strNome.toString();
		return nome;
	}

	/**
	 * Método que verifica se há elementos repetidos. O array recebido não contém
	 * nulls.
	 */
	public static boolean haRepeticoes(String[] elems) {

		boolean answer = false;
		int countTwo = 0;
		for (int i = 0; i < elems.length; i++) {
			int count = 0;
			for (int j = 0; j < elems.length; j++) {
				if (elems[i].equals(elems[j])) {
					count++;
					if (count > 1) {
						countTwo++;
					}
				}
			}
		}
		answer = (countTwo > 0) ? true : false;
		return answer;
	}

	/**
	 * Devolve true se o autor recebido existe como autor do livro. O nome recebido
	 * não contém espaços extra.
	 */
	public boolean contemAutor(String autorNome) {

		String[] autoresToCheck = this.getAutores();
		int count = 0;
		for (int i = 0; i < autoresToCheck.length; i++) {
			if (autorNome.equals(autoresToCheck[i]))
				count++;
		}
		return (count > 0) ? true : false;
	}

	/**
	 * Devolve uma string com a informação do livro (ver outputs desejados)
	 */
	public String toString() {
		return getTitulo() + ", " + getNumPaginas()+ "p, " + getPreco() + "€, " + Arrays.toString(getAutores());
	}

	/**
	 * Deve mostrar na consola a informação do livro precedida do prefixo
	 */
	public void print(String prefix) {
		System.out.println(prefix + this);
	}

	/**
	 * O Livro recebido é igual se tiver o mesmo título que o título do livro
	 * corrente
	 */
	public boolean equals(Livro l) {
		return (l != null) && this.getTitulo().equalsIgnoreCase(l.getTitulo());
	}

	/**
	 * main
	 */
	public static void main(String[] args) {

		// constructor e toString
		Livro l = new Livro("Viagem aos Himalaias", 340, 12.3f, new String[] { "João Mendonça", "Mário Andrade" });
		System.out.println("Livro -> " + l);
		l.print("");
		l.print("-> ");
		System.out.println();

		// contém autor
		String autorNome = "Mário Andrade";
		System.out.println("Livro com o autor " + autorNome + "? -> " + l.contemAutor(autorNome));
		autorNome = "Mário Zambujal";
		System.out.println("Livro com o autor " + autorNome + "? -> " + l.contemAutor(autorNome));
		System.out.println();

		// equals
		System.out.println("Livro: " + l);
		System.out.println("equals Livro: " + l);
		System.out.println(" -> " + l.equals(l));

		Livro l2 = new Livro("Viagem aos Himalaias", 100, 10.3f, new String[] { "Vitor Záspara" });
		System.out.println("Livro: " + l);
		System.out.println("equals Livro: " + l2);
		System.out.println(" -> " + l.equals(l2));
		System.out.println();

		// testes que dão excepção - mostra-se a excepção

		// livro lx1
		System.out.println("Livro lx1: ");
		try {
			Livro lx1 = new Livro("Viagem aos Himalaias", -1, 12.3f, new String[] { "João Mendonça", "Mário Andrade" });
			System.out.println("Livro lx1: " + lx1);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}
		System.out.println();

		// livro lx2
		System.out.println("Livro lx2: ");
		try {
			Livro lx2 = new Livro("Viagem aos Himalaias", 200, -12.3f,
					new String[] { "João Mendonça", "Mário Andrade" });
			System.out.println("Livro lx2: " + lx2);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}
		System.out.println();

		// livro lx3
		System.out.println("Livro lx3: ");
		try {
			Livro lx3 = new Livro(null, 200, -12.3f, new String[] { "João Mendonça", "Mário Andrade" });
			System.out.println("Livro lx3: " + lx3);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}
		System.out.println();

		// livro lx4
		System.out.println("Livro lx4: ");
		try {
			Livro lx4 = new Livro("Viagem aos Himalaias", 200, 12.3f,
					new String[] { "João Mendonça", "Mário Andrade", "João Mendonça" });
			System.out.println("Livro lx4: " + lx4);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}
		// livro lx5
		System.out.println("Livro lx5: ");
		try {
			Livro lx5 = new Livro("Eu, Robo", 253, 17.6f, new String[] { "Isaac Asimov", null });
			System.out.println("Livro lx5: " + lx5);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}
		// livro lx6
		System.out.println("Livro lx6: ");
		try {
			Livro lx6 = new Livro("Fundação", 256, 11.1f, new String[] { "Isaac Asimov2"});
			System.out.println("Livro lx6: " + lx6);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}
	}
}

