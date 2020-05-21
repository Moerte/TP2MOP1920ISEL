package tps.tp2.pack2Livros;

import java.util.Arrays;

/**
 * Classe que dever� suportar um livro
 */
public class Livro {

	// T�tulo do livro
	private String titulo;

	// n�mero de p�ginas
	private int numPaginas;

	// pre�o do livro
	private float preco;

	// array de autores, este array n�o deve ter nulls
	private String[] autores;

	/**
	 * Deve criar um novo livro com os dados recebidos. O t�tulo n�o deve ser null
	 * nem vazio. O n�mero de p�ginas n�o pode ser menor que 1. O pre�o n�o pode ser
	 * negativo. O array de autores n�o deve conter nem nulls e deve conter pelo
	 * menos um autor v�lido. N�o pode haver repeti��es dos nomes dos autores,
	 * considera-se os nomes sem os espa�os extra (ver removeExtraSpaces). Este
	 * m�todo deve utilizar os m�todos auxiliares existentes. Em caso de nome
	 * inv�lido deve lan�ar uma excep��o de IllegalArgumentException com a indica��o
	 * do erro ocorrido
	 */
	public Livro(String titulo, int numPaginas, float preco, String[] autores) {

		// t�tulo
		if (titulo == null || titulo.length() == 0)
			throw new IllegalArgumentException("O titulo tem de ter pelo menos um caracter");
		this.titulo = titulo;
		// N�mero de P�ginas
		if (numPaginas < 1)
			throw new IllegalArgumentException("O n� de p�ginas n�o pode ser negativo");
		this.numPaginas = numPaginas;
		// Pre�o
		if (preco < 0.0f)
			throw new IllegalArgumentException("O pre�o n�o pode ser negativo");
		this.preco = preco;
		// Autores
		// Valida se nao h� nulls ou n�meros nos nomes dos autores
		if (!validarNomes(autores))
			throw new IllegalArgumentException("O array de autores cont�m autores invalidos");
		// Valida se n�o h� repeti��es
		if (haRepeticoes(autores))
			throw new IllegalArgumentException("O array de autores cont�m autores repetidos");
		// Remove os espa�os extra
		for (int i = 0; i < autores.length; i++) {
			autores[i] = removeExtraSpaces(autores[i]);
		}
		// Faz uma c�pia do array de autores para o array privado de autores
		this.autores = Arrays.copyOf(autores, autores.length);

	}

	/**
	 * Devolve o t�tulo do livro
	 */
	public String getTitulo() {

		return this.titulo;
	}

	/**
	 * Devolve o n�mero de p�ginas do livro
	 */
	public int getNumPaginas() {

		return this.numPaginas;
	}

	/**
	 * Devolve o pre�o do livro
	 */
	public float getPreco() {

		return this.preco;
	}

	/**
	 * Devolve uma c�pia do array de autores do livro
	 */
	public String[] getAutores() {

		return this.autores;
	}

	/**
	 * Deve devolver true se o array conter apenas nomes v�lidos. Um nome � v�lido
	 * se conter pelo menos uma letra (Character.isLetter) e s� conter letras e
	 * espa�os (Character.isWhitespace). Deve chamar validarNome.
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
	 * Um nome v�lido se n�o for null e n�o conter pelo menos uma letra
	 * (Character.isLetter) e s� conter letras e espa�os (Character.isWhitespace)
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
	 * Recebe um nome j� previamente validado, ou seja s� com letras ou espa�os.
	 * Deve devolver o mesmo nome mas sem espa�os (utilizar trim e
	 * Character.isWhitespace) no in�cio nem no fim e s� com um espa�o ' ' entre
	 * cada nome. Deve utilizar um StringBuilder para ir contendo o nome j�
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
	 * M�todo que verifica se h� elementos repetidos. O array recebido n�o cont�m
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
	 * n�o cont�m espa�os extra.
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
	 * Devolve uma string com a informa��o do livro (ver outputs desejados)
	 */
	public String toString() {
		return getTitulo() + ", " + getNumPaginas()+ "p, " + getPreco() + "�, " + Arrays.toString(getAutores());
	}

	/**
	 * Deve mostrar na consola a informa��o do livro precedida do prefixo
	 */
	public void print(String prefix) {
		System.out.println(prefix + this);
	}

	/**
	 * O Livro recebido � igual se tiver o mesmo t�tulo que o t�tulo do livro
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
		Livro l = new Livro("Viagem aos Himalaias", 340, 12.3f, new String[] { "Jo�o Mendon�a", "M�rio Andrade" });
		System.out.println("Livro -> " + l);
		l.print("");
		l.print("-> ");
		System.out.println();

		// cont�m autor
		String autorNome = "M�rio Andrade";
		System.out.println("Livro com o autor " + autorNome + "? -> " + l.contemAutor(autorNome));
		autorNome = "M�rio Zambujal";
		System.out.println("Livro com o autor " + autorNome + "? -> " + l.contemAutor(autorNome));
		System.out.println();

		// equals
		System.out.println("Livro: " + l);
		System.out.println("equals Livro: " + l);
		System.out.println(" -> " + l.equals(l));

		Livro l2 = new Livro("Viagem aos Himalaias", 100, 10.3f, new String[] { "Vitor Z�spara" });
		System.out.println("Livro: " + l);
		System.out.println("equals Livro: " + l2);
		System.out.println(" -> " + l.equals(l2));
		System.out.println();

		// testes que d�o excep��o - mostra-se a excep��o

		// livro lx1
		System.out.println("Livro lx1: ");
		try {
			Livro lx1 = new Livro("Viagem aos Himalaias", -1, 12.3f, new String[] { "Jo�o Mendon�a", "M�rio Andrade" });
			System.out.println("Livro lx1: " + lx1);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}
		System.out.println();

		// livro lx2
		System.out.println("Livro lx2: ");
		try {
			Livro lx2 = new Livro("Viagem aos Himalaias", 200, -12.3f,
					new String[] { "Jo�o Mendon�a", "M�rio Andrade" });
			System.out.println("Livro lx2: " + lx2);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}
		System.out.println();

		// livro lx3
		System.out.println("Livro lx3: ");
		try {
			Livro lx3 = new Livro(null, 200, -12.3f, new String[] { "Jo�o Mendon�a", "M�rio Andrade" });
			System.out.println("Livro lx3: " + lx3);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}
		System.out.println();

		// livro lx4
		System.out.println("Livro lx4: ");
		try {
			Livro lx4 = new Livro("Viagem aos Himalaias", 200, 12.3f,
					new String[] { "Jo�o Mendon�a", "M�rio Andrade", "Jo�o Mendon�a" });
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
			Livro lx6 = new Livro("Funda��o", 256, 11.1f, new String[] { "Isaac Asimov2"});
			System.out.println("Livro lx6: " + lx6);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}
	}
}

