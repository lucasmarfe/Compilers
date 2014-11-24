package lexer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;

import symbols.Type;

public class Lexer {
	public static int m_line = 1;
	public char m_charLido = ' ';
	Hashtable<String, Token> words = new Hashtable<String, Token>();
	FileInputStream stream;
	InputStreamReader reader;

	ArrayList<Character> Delimitadores = new ArrayList<Character>();

	boolean isDelimiter(char c) { // Metodo que determina se um caractere pode
									// ser desconsiderado

		int tam = Delimitadores.size();
		for (int i = 0; i < tam; i++) {
			if (c == Delimitadores.get(i)) {
				return true;
			}
		}
		return false;

	}

	boolean isValidChar(char c) {

		if (((byte) c >= 0 && (byte) c <= 127)) {
			return true;
		}
		return false;
	}

	void addReserveWords(Word w) {
		words.put(w.m_lexema, w);
	}

	public Lexer(String arq_fonte) throws FileNotFoundException {
		// Adiciona as palavras reservadas na tabela
		addReserveWords(new Word("if", Tag.IF));
		addReserveWords(new Word("then", Tag.THEN));
		addReserveWords(new Word("end", Tag.END));
		addReserveWords(new Word("else", Tag.ELSE));
		addReserveWords(new Word("repeat", Tag.REPEAT));
		addReserveWords(new Word("until", Tag.UNTIL));
		addReserveWords(new Word("while", Tag.WHILE));
		addReserveWords(new Word("do", Tag.DO));
		addReserveWords(new Word("read", Tag.READ));
		addReserveWords(new Word("write", Tag.WRITE));
		addReserveWords(new Word("program", Tag.PROGRAM));
		addReserveWords(Type.Int);
		addReserveWords(Type.Real);

		// Adiciona os delimitadores a� lista de delimitadores:
		Delimitadores.add(' ');
		Delimitadores.add('\t');
		Delimitadores.add('\r');

		try {
			stream = new FileInputStream(arq_fonte);
			reader = new InputStreamReader(stream);
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo nao encontrado");
			//throw e;
		}
	}

	void readch() throws IOException {
		m_charLido = (char) reader.read();
		;
	}

	boolean readch(char c) throws IOException {
		readch();
		if (m_charLido != c) {
			return false;
		}
		m_charLido = ' ';
		return true;
	}

	public Token getToken() throws IOException {
		// TODO Implementar

		/**
		 * Desconsidera espaços em branco, tabulações, quebra de linha e
		 * comentário de uma linha
		 **/
		for (;; readch()) {
			if (isDelimiter(m_charLido)) {
				continue;
			}

			// Tratamento de comentarios - Sao aceitos caracteres diferentes dos
			// da tabela ASCII
			else if (m_charLido == '%') {
				StringBuffer coment = new StringBuffer();
				readch();
				while (m_charLido != '\n' && m_charLido != '\r') {
					coment.append(m_charLido);
					readch();
				}

				if (m_charLido == '\n') {
					m_line++;
					return new Token(Tag.COMENTARIO, coment.toString());

				}
			}

			else if (m_charLido == '\n') {
				m_line++;
			} else {
				break;
			}
		}

		// Literal: caracteres entre aspas duplas -> podem conter caracteres
		// diferentes dos da tabela ASCII
		if (m_charLido == '"') {
			StringBuffer b = new StringBuffer();
			do {
				b.append(m_charLido);
				readch();

				if (m_charLido == '\n') { // Caso nao feche aspas duplas
					System.out.println("Erro na linha " + m_line
							+ ": esperado token \" após literal");
					return new Token(Tag.ERRO);
				}

			} while (m_charLido != '"');
			b.append(m_charLido);
			String s = b.toString();
			m_charLido = ' ';
			return new Literal(s);
		}

		// A partir deste ponto, nao pode haver caracteres que nao estejam na
		// tabela ASCII
		if (!isValidChar(m_charLido) && m_charLido != (char)65535) {
			System.out.println("Erro na linha " + m_line
					+ ": caractere inválido");
			m_charLido = ' ';
			return new Token(Tag.ERRO);
		}

		switch (m_charLido) {
		case '&':
			if (readch('&'))
				return Word.And;
			else
				return new Token('&');
		case '|':
			if (readch('|'))
				return Word.Or;
			else
				return new Token('|');
		case '!':
			if (readch('='))
				return Word.NotEqual;
			else
				return new Token('!');
		case '<':
			if (readch('='))
				return Word.LessEqual;
			else
				return new Token((int) '<');
		case '>':
			if (readch('='))
				return Word.GreaterEqual;
			else
				return new Token((int) '>');
		case ':':
			if (readch('='))
				return Word.Assign;
			else
				return new Token((int) ':');
		}

		// Real ou integer
		if (Character.isDigit(m_charLido)) {

			int v = 0;
			do {
				v = 10 * v + Character.digit(m_charLido, 10);
				readch();
			} while (Character.isDigit(m_charLido));

			if (m_charLido != '.') {
				return new NumInteger(v);
			}

			int i;
			float x = (float) v;
			float d = 10f;
			int ultimo = 0; // Será usado para determinar a última casa decimal
							// lida, para auxiliar no truncamento

			for (i = 0;; i++) {
				readch();

				if (!Character.isDigit(m_charLido)) {

					if (i == 0) { // Se o caracter for algo diferente
									// de digito, devera ser apontado o erro
						System.out.println("Erro na linha " + m_line + ": "
								+ "constante mal formada");
						return new Token(Tag.ERRO);
					} else {
						break; // Reconhecimento do número real
					}

				}

				x = x + (float) (Character.digit(m_charLido, 10) / d);
				d = d * 10;
				ultimo = Character.digit(m_charLido, 10);
				// Notificacao de overflow
				// Nao funciona em java, uma vez que nao e possivel determinar
				// flags para overflow conforme especificacao do IEEE-754
				if (Float.isInfinite(x)) {
					System.out.println("Erro linha " + m_line
							+ ": o número não pode ser representado");
					return new Token(Tag.ERRO);
				}

			}
			// Tratamento de casas decimais
			if (i > 7) { // Floats representam entre 6 e 7 casas decimais, caso
							// o número possua mais que esse numero de casas,
							// ele é arredondado
				System.out
						.println("Atenção: Linha "
								+ m_line
								+ ": número real com mais de 7 casas decimais - o número será truncado");
			}

			// Tratamento da questao da exatidao de floats em java - apenas para
			// melhorar a representacao
			if (ultimo >= 5) {
				x = (float) Math.ceil(x * (float) Math.pow(10, i + 1))
						/ (float) Math.pow(10, i + 1); // o numero sera truncado
														// no numero de casas
														// decimais do
														// codigo-fonte com base
														// no ultimo digito
			} else if (ultimo < 5) {
				x = (float) Math.floor(x * (float) Math.pow(10, i + 1))
						/ (float) Math.pow(10, i + 1); // o número será truncado
														// no numero de casas
														// decimais do
														// codigo-fonte
			}
			return new NumReal(x);

		}

		// Identificadores
		if (Character.isLetter(m_charLido)) {
			StringBuffer b = new StringBuffer();
			do {
				if (!isValidChar(m_charLido)) {
					System.out.println("Erro na linha " + m_line
							+ ": caractere inválido");
					m_charLido = ' ';
					return new Token(Tag.ERRO);
				}
				b.append(m_charLido);
				readch();
			} while (Character.isLetterOrDigit(m_charLido) || m_charLido == '_');

			String s = b.toString();
			if (b.length() >= 25) { // Caso o identificador tenha mais do que 25
									// caracteres
				s = s.substring(0, 24); // Identificadores nao devem ter mais
										// que 25 caracteres
			}
			Word w = (Word) words.get(s);
			if (w != null) {
				return w;
			}
			w = new Word(s, Tag.ID);
			words.put(w.m_lexema, w);
			return w;
		}

		Token tok = new Token(m_charLido);
		m_charLido = ' ';
		return tok;
	}

	// Metodo para poder acessar reader a partir da main
	public InputStreamReader getReader() {
		return reader;
	}

	// Metodo para poder acessar a tabela de simbolos atraves da main
	public Hashtable<String, Token> getHashtable() {
		return words;
	}
}