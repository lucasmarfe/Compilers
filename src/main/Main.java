package main;

import java.io.IOException;
import java.util.Set;
import lexer.*;

public class Main {

	public static void main(String[] args) throws IOException {
		Token tokens;

		Lexer m_lex = new Lexer("Testes/teste1.mini");

		int i = 1;

		System.out.println("Tokens extraídos:");
		System.out.println("----------------------");
		while (m_lex.getReader().ready()) {
			tokens = m_lex.scan();

			System.out.println("Token " + i + ": " + "<" + tokens + ","
					+ tokens.m_tag + ">");

			i++;
		}
		System.out.println("----------------------\n");
		System.out.println("\n\nTabela de Símbolos");
		System.out.println("----------------------");

		Set<String> keys = m_lex.getHashtable().keySet();

		for (String aux : keys) {
			System.out.println(m_lex.getHashtable().get(aux));
		}

		System.out.println("----------------------");
		System.out.println("\nFim");

	}

}
