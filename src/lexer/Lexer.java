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
	public FileInputStream stream;
	public InputStreamReader reader; // Estão como public apenas para poder
										// testar na main

	ArrayList<Character> Delimitadores = new ArrayList<Character>();

	boolean isDelimiter(char c) { // Método que determina se um caractere pode
									// ser desconsiderado

		int tam = Delimitadores.size();
		for (int i = 0; i < tam; i++) {
			if (c == Delimitadores.get(i)) {
				return true;
			}
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

		// Adiciona os delimitadores à lista de delimitadores:
		Delimitadores.add(' ');
		Delimitadores.add('\t');
		Delimitadores.add('\r');

		try {
			stream = new FileInputStream(arq_fonte);
			reader = new InputStreamReader(stream);
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo não encontrado");
			throw e;
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

	public Token scan() throws IOException {
		//TODO Implementar
		
		/**Desconsidera espaços em branco, tabulações, quebra de linha e comentário de uma linha**/
		for( ; ; readch() ) 
		{
			if (isDelimiter(m_charLido))
			{
				continue;
			}
			
			// Tratamento de comentarios
			else if(m_charLido == '%'){
				StringBuffer coment = new StringBuffer();
				readch();
				while(m_charLido != '\n' && m_charLido != '\r'){
					coment.append(m_charLido);
					readch();
				}
				
				if ( m_charLido == '\n'){
					m_line++;
					return new Token(Tag.COMENTARIO, coment.toString());
								
				}
			}
			
			else if ( m_charLido == '\n') 
			{
				m_line++;
			}
			else
			{
				break;
			}
		}
		
		switch( m_charLido ) {
	      case '&':
	         if( readch('&') ) return Word.And;  else return new Token('&');
	      case '|':
	         if( readch('|') ) return Word.Or;   else return new Token('|');
	      case '!':
	         if( readch('=') ) return Word.NotEqual;   else return new Token('!');
	      case '<':
	         if( readch('=') ) return Word.LessEqual;   else return new Token((int) '<');
	      case '>':
	         if( readch('=') ) return Word.GreaterEqual;   else return new Token((int) '>');
	      case '=':
	    	  if( readch(':') ) return Word.Assign;   else return new Token((int) '=');
		}
		
		// Real ou integer
		if( Character.isDigit(m_charLido) ) {
			
			StringBuffer stringInteiro = new StringBuffer();
			StringBuffer stringDecimal = new StringBuffer();
			
	         do 
	         {
	        	stringInteiro.append(m_charLido); readch();
	            
	         } 
	         while( Character.isDigit(m_charLido) );
	         
	         int inteiro = 0;
	         try{
	        	 inteiro = Integer.parseInt(stringInteiro.toString());
		         }
		         catch(java.lang.NumberFormatException e){
		        	 
		        	 System.out.println("Erro linha " + m_line + ": o número não pode ser representado");
		        	 return new Token(Tag.ERRO);
		         }
	         
	         if( m_charLido != '.' ) return new NumInteger(inteiro);
	         
	         
	         int i;
	         for(i = 0;; i++)
	         {
	            readch();

	            if( ! Character.isDigit(m_charLido)) 
            	{
	            	
	            		if(i == 0){  // Se 1o caracter após '.' for um delimitador, deverá ser apontado o erro
	            		  System.out.println("Erro na linha " + m_line + ": " + "constante mal formada");
	            		  return new Token(Tag.ERRO);
	            		}
	            		else{
	            		  break; // Reconhecimento do número real
	            		}
	            	
            	}
	          
	            
	            stringDecimal.append(m_charLido);
	            
	            	            
	         }
	         int decimal = 0;
	         try{ // Caso haja overflow, reportar o erro
	         decimal = Integer.parseInt(stringDecimal.toString());
	         }
	         catch(java.lang.NumberFormatException e){
	        	 
	        	 System.out.println("Erro linha " + m_line + ": o número não pode ser representado");
	        	 return new Token(Tag.ERRO);
	         }
	         
	         
	         float result = (float)inteiro + (float)decimal/(float)Math.pow(10, i);
	         return new NumReal(result);
	        
	      }
		
		//Literal: caracteres entre parentesis
		if( m_charLido == '"') {
	         StringBuffer b = new StringBuffer();
	         do 
	         {
	            b.append(m_charLido);
	            readch();
	         } 
	         while( m_charLido != '"' );
	         b.append(m_charLido);
	         String s = b.toString();
	         return new Literal(s);
	      }
		
		//Identificadores
		if( Character.isLetter(m_charLido) ) {
	         StringBuffer b = new StringBuffer();
	         do 
	         {
	            b.append(m_charLido); readch();
	         } 
	         while(Character.isLetterOrDigit(m_charLido) || m_charLido == '_');
	         String s = b.toString();
	         if(b.length() >= 25){   // Caso o identificador tenha mais do que 25 caracteres
	         s = s.substring(0, 24); //Identificadores não devem ter mais que 25 caracteres
	         }
	         Word w = (Word)words.get(s);
	         if( w != null )
	         {
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

	public InputStreamReader getReader() {
		return reader;
	}
	
	public Hashtable<String, Token> getHashtable() {
		return words;
	}
}