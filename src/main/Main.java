package main;

import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

import parser.Parser;

import lexer.*;

public class Main {

	public static void main(String[] args) throws IOException {
		//Token tokens;
				
		Lexer m_lex = new Lexer(getNomeArquivo());
		Parser parse = new Parser(m_lex);
		parse.program();
		if(parse.getNumErros() == 0)
		System.out.println("Analise sintatica concluida com sucesso.\n");
		else
		System.out.println("Analise sintatica concluida com " + parse.getNumErros() + " erros.");	
		
		/*
		int i = 1;
		System.out.println("Tokens extraídos:");
		System.out.println("----------------------");
		while (m_lex.getReader().ready()) {
			tokens = m_lex.getToken();
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
		*/
	}

	private static String getNomeArquivo() {
		String nome_arq;
		Scanner sc = new Scanner(System.in);
		System.out.print("Insira o caminho do arquivo (Testes/teste#.mini): ");
		nome_arq = sc.next();
		return nome_arq;
	}

}
