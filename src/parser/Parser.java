package parser;

import java.io.IOException;
import lexer.Lexer;
import lexer.Tag;
import lexer.Token;

public class Parser {

	private Token m_tok;
	
	private Lexer m_lexer;
	
	public Parser(Lexer lex) throws IOException
	{
		m_lexer = lex;
		advance();
	}
	
	Token advance() throws IOException 
	{
		m_tok=m_lexer.getToken();
		return m_tok;
	}
	
	void eat(int t) throws IOException 
	{
		if (m_tok.m_tag == t) 
			advance(); 
		else 
			error("Erro sintático ");
	}

	void error(String s) { 
		throw new Error(s + "na linha "+m_lexer.m_line+": "); 
	}
	
	public void program() throws IOException {  // program ::= program identifier body
	    	switch(m_tok.m_tag)
	    	{
	    		case Tag.PROGRAM: 	eat(Tag.PROGRAM);
	    											identifier();
	    											body();
	    											break;
	    		default: error("Erro sintático, esperava encontrar program ");
	    	}
	   }

	private void body() throws IOException {
		// body ::= [ decl-list] “ {“ stmt-list “}”
		
	}

	private void identifier() throws IOException {
		// TODO Auto-generated method stub
		
	}
}
