package lexer;

import java.io.IOException;
import java.util.Hashtable;

import symbols.Type;

public class Lexer {
	public static int m_line = 1;
	char m_charLido = ' ';
	Hashtable words = new Hashtable();	
	
	void addReserveWords(Word w) 
	{ 
		words.put(w.m_lexema, w); 
	}
	
	public Lexer() {
		// Adiciona as palavras reservadas na tabela
		addReserveWords(new Word("if", Tag.IF));
		addReserveWords(new Word( "then", Tag.THEN ));
		addReserveWords(new Word( "end", Tag.END ));
		addReserveWords(new Word( "else", Tag.ELSE ));
		addReserveWords(new Word( "repeat", Tag.REPEAT ));
		addReserveWords(new Word( "until", Tag.UNTIL ));
		addReserveWords(new Word( "while", Tag.WHILE ));
		addReserveWords(new Word( "do", Tag.DO ));
		addReserveWords(new Word( "read", Tag.READ ));
		addReserveWords(new Word( "write", Tag.WRITE));
		addReserveWords(new Word( "program", Tag.PROGRAM));
		addReserveWords( Type.Int );
		addReserveWords( Type.Real );
	}
	
	void readch() throws IOException 
	{
		m_charLido = (char)System.in.read(); 
	}
	
	boolean readch(char c) throws IOException 
	{
		readch();
		if( m_charLido != c )
		{
			return false;
		}
		m_charLido=' ';
		return true;
	}
	
	public Token scan() throws IOException {
		//TODO Implementar
		/**Desconsidera espaços em branco, tabulações e quebra de linha**/
		for( ; ; readch() ) 
		{
			if ( m_charLido == ' ' || m_charLido == '\t')
			{
				continue;
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
	      case '=':
	         return Word.Equal;
	      case '!':
	         if( readch('=') ) return Word.NotEqual;   else return new Token('!');
	      case '<':
	         if( readch('=') ) return Word.LessEqual;   else return Word.Less;
	      case '>':
	         if( readch('=') ) return Word.Greater;   else return Word.Greater;
	      }
		
		
		
		return null;
	}
	
}
