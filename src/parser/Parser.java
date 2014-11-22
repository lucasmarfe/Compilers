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
			error("Erro sintático, esperava encontrar "+ m_tok.m_desc + ", ");
	}

	void error(String s) { 
		throw new Error(s + "na linha "+m_lexer.m_line+": "); 
	}
	
	public void program() throws IOException {  // program ::= program identifier body
	    	switch(m_tok.m_tag)
	    	{
	    		case Tag.PROGRAM: 	eat(Tag.PROGRAM);
	    											eat(Tag.ID);
	    											body();
	    											break;
	    		default: error("Erro sintático, esperava encontrar program ");
	    	}
	   }

	private void body() throws IOException {
		// body ::= decl-list “ {“ stmt-list “}”
		switch(m_tok.m_tag)
    	{
    		case Tag.ABRECHAVES: 	
    		case Tag.INTEGER:
    		case Tag.REAL: decllist(); eat(Tag.ABRECHAVES); stmtlist(); eat(Tag.FECHACHAVES);	break;
    		default: error("Erro sintático, esperava encontrar { integer ou real ");
    	}
	}

	private void decllist() throws IOException {
		// decl-list ::= decl   ;   decl-list   |   lambda
		switch(m_tok.m_tag)
    	{
    		case Tag.INTEGER:
    		case Tag.REAL: decl(); eat(Tag.PONTOVIRGULA); decllist();	break;
    		case Tag.FECHACHAVES: break;
    		default: error("Erro sintático, esperava encontrar: { integer ou real, ");
    	}
	}
	
	private void decl() throws IOException {
		// decl ::= type ident-list
		switch(m_tok.m_tag)
    	{
			case Tag.INTEGER:
			case Tag.REAL: type(); identlist();	break;
			default: error("Erro sintático, esperava encontrar: integer ou real, ");
    	}
	}
	
	private void identlist() throws IOException {
		// ident-list ::= identifier ident-list '
		switch(m_tok.m_tag)
    	{
			case Tag.ID: eat(Tag.ID); identlistline();	break;
			default: error("Erro sintático, esperava encontrar: integer ou real, ");
    	}
	}

	private void identlistline() throws IOException {
		// ident-list' ::= ,  identifier  ident-list'   |   lambda
		switch(m_tok.m_tag)
    	{
			case Tag.VIRGULA: eat(Tag.VIRGULA); eat(Tag.ID); identlistline();	break;
			case Tag.PONTOVIRGULA: break;
			default: error("Erro sintático, esperava encontrar: integer ou real, ");
    	}
	}

	private void type() throws IOException {
		// type  ::= integer | real
		switch(m_tok.m_tag)
    	{
			case Tag.INTEGER: eat(Tag.INTEGER); break;
			case Tag.REAL: eat(Tag.REAL); break;
			default: error("Erro sintático, esperava encontrar: integer ou real, ");
    	}
	}

	private void stmtlist() throws IOException {
		// stmt-list ::= stmt ; stmt-list  |  lambda
		switch(m_tok.m_tag)
    	{
			case Tag.ID:
			case Tag.IF:
			case Tag.WHILE:
			case Tag.REPEAT:
			case Tag.READ:
			case Tag.WRITE: stmt(); eat(Tag.PONTOVIRGULA); stmtlist();
			case Tag.FECHACHAVES: break;
			case Tag.ELSE: break;
			case Tag.END: break;
			case Tag.UNTIL: break;
			default: error("Erro sintático, esperava encontrar: identificador if whilw repeat read  write } else end until, ");
    	}
	}

	private void stmt() throws IOException {
		// stmt ::= assign-stmt | if-stmt | while-stmt | repeat-stmt | read-stmt | write-stmt
		switch(m_tok.m_tag)
    	{
			case Tag.ID: assignstmt(); break;
			case Tag.IF: ifstmt(); break;
			case Tag.WHILE: whilestmt(); break;
			case Tag.REPEAT: repeatstmt(); break;
			case Tag.READ: readstmt(); break;
			case Tag.WRITE: writestmt(); break;
			default: error("Erro sintático, esperava encontrar: identificador if whilw repeat read ou write, ");
    	}
	}

	private void assignstmt() throws IOException {
		// assign-stmt ::= identifier ":=" simple_expr
		switch(m_tok.m_tag)
    	{
			case Tag.ID: eat(Tag.ID); eat(Tag.ASSIGN); simpleexpr(); break;
			default: error("Erro sintático, esperava encontrar: identificador if whilw repeat read ou write, ");
    	}
	}
	
	private void ifstmt() throws IOException {
		// if-stmt  ::= if condition then stmt-list if-stmt'
		switch(m_tok.m_tag)
    	{
			case Tag.IF: eat(Tag.IF); condition(); eat(Tag.THEN); stmtlist(); ifstmtline(); break;
			default: error("Erro sintático, esperava encontrar: identificador if whilw repeat read ou write, ");
    	}
	}
	private void ifstmtline() throws IOException {
		// if-stmt '  ::=  else stmt-list end  | end
		switch(m_tok.m_tag)
    	{
			case Tag.ELSE: eat(Tag.ELSE); stmtlist(); eat(Tag.END); break;
			case Tag.END: eat(Tag.END); break;
			default: error("Erro sintático, esperava encontrar: identificador if whilw repeat read ou write, ");
    	}
	}
	
	private void condition() throws IOException {
		// condition ::= expression
		switch(m_tok.m_tag)
    	{
			case Tag.ABREPARENTESIS: 
			case Tag.ID:
			case Tag.REAL:
			case Tag.INTEGER:
			case Tag.NEGACAO:
			case Tag.TRACO: expression(); break;
			default: error("Erro sintático, esperava encontrar: identificador if whilw repeat read ou write, ");
    	}
	}
	
	private void repeatstmt() throws IOException {
		// repeat-stmt ::= repeat stmt-list stmt-suffix
		switch(m_tok.m_tag)
    	{
			case Tag.REPEAT: eat(Tag.REPEAT); stmtlist(); stmtsuffix(); break;
			default: error("Erro sintático, esperava encontrar: identificador if whilw repeat read ou write, ");
    	}
	}
	
	private void stmtsuffix() throws IOException {
		// stmt-suffix ::= until condition
		switch(m_tok.m_tag)
    	{
			case Tag.UNTIL: eat(Tag.UNTIL); condition(); break;
			default: error("Erro sintático, esperava encontrar: identificador if whilw repeat read ou write, ");
    	}
	}

	private void whilestmt() throws IOException {
		// while-stmt ::= stmt-prefix stmt-list end
		switch(m_tok.m_tag)
    	{
			case Tag.WHILE: stmtprefix(); stmtlist(); eat(Tag.END); break;
			default: error("Erro sintático, esperava encontrar: identificador if whilw repeat read ou write, ");
    	}
	}
	
	private void stmtprefix() throws IOException {
		// stmt-prefix ::= while condition do
		switch(m_tok.m_tag)
    	{
			case Tag.WHILE:  eat(Tag.WHILE); condition(); eat(Tag.DO); break;
			default: error("Erro sintático, esperava encontrar: identificador if whilw repeat read ou write, ");
    	}
	}

	private void readstmt() throws IOException {
		// read-stmt ::= read "(" identifier ")"
		switch(m_tok.m_tag)
    	{
			case Tag.READ:  eat(Tag.READ); eat(Tag.ABREPARENTESIS); eat(Tag.ID); eat(Tag.FECHAPARENTESIS); break;
			default: error("Erro sintático, esperava encontrar: identificador if whilw repeat read ou write, ");
    	}
	}
	
	private void writestmt() throws IOException {
		// write-stmt  ::=   write "(" writable ")"
		switch(m_tok.m_tag)
    	{
			case Tag.WRITE:  eat(Tag.WRITE); eat(Tag.ABREPARENTESIS); writable(); eat(Tag.FECHAPARENTESIS); break;
			default: error("Erro sintático, esperava encontrar: identificador if whilw repeat read ou write, ");
    	}
	}

	private void writable() throws IOException {
		// writable  ::=  simple-expr | literal
		switch(m_tok.m_tag)
    	{
			case Tag.ABREPARENTESIS:  
			case Tag.ID: simpleexpr(); break;
			case Tag.LITERAL: eat(Tag.LITERAL); break;
			default: error("Erro sintático, esperava encontrar: identificador if whilw repeat read ou write, ");
    	}
	}
	
	private void expression() throws IOException {
		// expression ::= simple-expr expression'
		switch(m_tok.m_tag)
    	{
			case Tag.ABREPARENTESIS:  
			case Tag.ID: 
			case Tag.REAL:
			case Tag.INTEGER:
			case Tag.NEGACAO:
			case Tag.TRACO: simpleexpr(); expressionline(); break;
			default: error("Erro sintático, esperava encontrar: identificador if whilw repeat read ou write, ");
    	}
	}

	private void expressionline() throws IOException {
		// expressionline  ::=  relop simple-expr  |  lambda
		switch(m_tok.m_tag)
    	{
			case Tag.RELOP: relop(); simpleexpr(); break; //TODO
			case Tag.ABREPARENTESIS:  break;
			case Tag.PONTOVIRGULA:  break;
			case Tag.THEN:  break;
			case Tag.DO:  break;
			default: error("Erro sintático, esperava encontrar: identificador if whilw repeat read ou write, ");
    	}
	}

	private void simpleexpr() throws IOException {
		//  simple-expr  ::=  term simple-expr'
		switch(m_tok.m_tag)
    	{
    		case Tag.ABREPARENTESIS:
			case Tag.ID:
			case Tag.REAL:
			case Tag.INTEGER:
			case ('!'):
			case ('-'): term(); simpleexprline(); break;
			default: error("Erro sintático, esperava encontrar: ) identificador real inteiro ! - , ");
    	}
	}

	private void simpleexprline() throws IOException {
		//  simple-expr'   ::=  addop term simple-expr'
		switch(m_tok.m_tag)
    	{
    		case ('+'): 
    		case ('-'): 
    		case Tag.OR: addop(); term(); simpleexprline(); break;
    		default: error("Erro sintático, esperava encontrar: um operador de adição ");
    	}
	}

	private void term() throws IOException {
		// term  ::=  factor-a term'
		switch(m_tok.m_tag)
    	{
    		case Tag.ABREPARENTESIS:
			case Tag.ID:
			case Tag.REAL:
			case Tag.INTEGER:
			case ('!'):
			case ('-'): factora(); termline(); break;
			default: error("Erro sintático, esperava encontrar: ) identificador real inteiro ! - , ");
    	}
	}

	private void termline() throws IOException {
		// term'  ::=   mulop factor-a term' 
		switch(m_tok.m_tag)
    	{
			case Tag.MULOP: mulop(); factora(); termline(); //TODO
			case Tag.ADDOP: break;   //TODO
			default: error("Erro sintático, esperava encontrar: ) identificador real inteiro ! - , ");
    	}
	}

	private void factora() throws IOException {
		// fator-a  ::=  factor | ! factor | "-" factor
		switch(m_tok.m_tag)
    	{
			case Tag.ABREPARENTESIS: 
			case Tag.ID: 
			case Tag.INTEGER:
			case Tag.REAL: factor();break;
			case Tag.NEGACAO: eat(Tag.NEGACAO); factor(); break;
			case Tag.TRACO: eat(Tag.TRACO); factor(); break;
			default: error("Erro sintático, esperava encontrar: ) identificador real inteiro ! - , ");
    	}
	}

	private void factor() throws IOException {
		//factor  ::=   identifier | constant | "(" expression ")"
		switch(m_tok.m_tag)
    	{
			
			case Tag.ID: eat(Tag.ID); break;
			case Tag.INTEGER: eat(Tag.INTEGER); break;
			case Tag.REAL: eat(Tag.REAL); break;
			case Tag.ABREPARENTESIS: eat(Tag.ABREPARENTESIS); expression(); eat(Tag.FECHAPARENTESIS); break;
			default: error("Erro sintático, esperava encontrar: ) identificador real inteiro ! - , ");
    	}
	}

	private void addop() throws IOException {
		//addop ::=  "+" | "-" | ||
		switch(m_tok.m_tag)
    	{
			case ('+'): eat('+'); break;
			case ('-'): eat('-'); break;
			case Tag.OR: eat(Tag.OR); break;
			default: error("Erro sintático, esperava encontrar: + - || , ");
    	}
		
	}
	
	private void relop() throws IOException {
		//relop ::=  "=" | ">" | ">=" | "<" | "<=" | "!="
				switch(m_tok.m_tag)
		    	{
					case ('='): eat('+'); break;
					case ('>'): eat('>'); break;
					case ('<'): eat('<'); break;
					case Tag.MAIOREQ: eat(Tag.MAIOREQ); break;
					case Tag.MENOREQ: eat(Tag.MENOREQ); break;
					case Tag.DIFERENTE: eat(Tag.DIFERENTE); break;
					default: error("Erro sintático, esperava encontrar: + - || , ");
		    	}
	}
	
	private void mulop() throws IOException {
		// mulop ::=   "*" | "/" | &&
		switch(m_tok.m_tag)
    	{
			case ('*'): eat('*'); break;
			case ('/'): eat('/'); break;
			case Tag.AND: eat(Tag.AND); break;
			default: error("Erro sintático, esperava encontrar: + - || , ");
    	}
	}
}
