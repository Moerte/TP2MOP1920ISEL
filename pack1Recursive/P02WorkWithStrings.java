package tps.tp2.pack1Recursive;

/**
 * Classe para conter exerc�cios recursivos com inteiro
 */
public class P02WorkWithStrings {

	/**
	 * Main, m�todo de arranque da execu��o
	 */
	public static void main(String[] args) {

		// ====================================================
		// test method compareStrings
		test_compareStrings(null, null); // result = 0
		test_compareStrings(null, ""); // result = -1
		test_compareStrings("", null); // result = 1
		test_compareStrings("a", ""); // result = 1
		test_compareStrings("", "a"); // result = -1
		test_compareStrings("a", "a"); // result = 0
		test_compareStrings("b", "a"); // result = 1
		test_compareStrings("a", "b"); // result = -1
		test_compareStrings("aa", "a"); // result = 2
		test_compareStrings("a", "aa"); // result = -2
		test_compareStrings("aa", "aa"); // result = 0
		test_compareStrings("ab", "aa"); // result = 2
		test_compareStrings("ab", "ab"); // result = 0
		test_compareStrings("abc", "abc"); // result = 0
		test_compareStrings("abc", "abd"); // result = -3
		System.out.println();
	}

	/**
	 * Este m�todo recebe duas Strings s1 e s2 e procede � sua compara��o,
	 * devolvendo um valor positivo se s1 for maior que s2, negativo se ao contr�rio
	 * e 0 se iguais. A compara��o deve ser feita primeiro em termos lexicogr�ficos
	 * caracter a caracter come�ando pelos caracteres de menor peso ou em segundo
	 * lugar em termos de n�mero de caracteres. Se diferentes deve devolver o �ndice
	 * +1/-1 do caractere que faz a diferen�a. Ex. s1="Bom", s2="Dia", deve devolver
	 * -1; s1="Boa", s2="Bom", deve devolver -3; s1="Bom", s2="Bo", deve devolver 3.
	 * Uma String a null � considerada menor que uma string n�o null.
	 * 
	 * @param s1 string a comparar
	 * @param s2 string a comparar
	 * @return o resultado da compara��o
	 */
	private static int compareStrings(String s1, String s2) {
		
		if (s1 == null && s2 == null)
			return 0;
		else if (s1 == null && s2 != null)
			return -1;
		else if (s1 != null && s2 == null)
			return 1;
		else{
			if (s1.length() == 0 && s2.length() == 0)
				return 0;
			else if (s1.length() == 0)
				return -1;
			else if (s2.length() == 0)
				return 1;
			else if (s1.charAt(0) < s2.charAt(0))
				return -1;
			else if (s1.charAt(0) > s2.charAt(0))
				return 1;
			else {
				
				int index = compareStrings(s1.substring(1), s2.substring(1));
				if(index <0) 
					return --index;
				else if(index > 0)
					return ++index;
				else
					return 0;
			}
		}
	}

	/**
	 * Auxiliary method that call compareStrings with two strings
	 */
	private static void test_compareStrings(String s1, String s2) {
		try {

			System.out.print("compareStrings (" + s1 + ", " + s2 + ") = ");
			int res = compareStrings(s1, s2);
			System.out.println(res);

		} catch (IllegalArgumentException e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

}
