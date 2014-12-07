package parser;

import java.io.IOException;
import java.lang.reflect.Field;

import lexer.Lexer;
import lexer.Tag;
import lexer.Token;

public class Parser {

	private Token m_tok;

	private Lexer m_lexer;
	
	private int num_erros = 0;
	
	private int prod_atual; //Apenas para auxiliar na verificação do parser

	public Parser(Lexer lex) throws IOException {
		m_lexer = lex;
		advance();
	}

	Token advance() throws IOException {
		m_tok = m_lexer.getToken();
		return m_tok;
	}

	void eat(int t) throws IOException {

		if (m_tok.m_tag == t)
			advance();
		else
			error(Character.toString((char)t)); 
	}

	void error(String follow) {
		num_erros++;
		System.out.println("Erro Sintático na produção " + prod_atual +  ": " + "Esperava encontrar: " + follow + " Encontrado: " +  m_tok.toString() + " na linha " + m_lexer.m_line);
	}
	
	
	int encontrado(int []sync, int tok){
		for(int i = 0; i < sync.length; i++){
			if(tok == sync[i]){
				return tok;
			}	
		}
		return -1; // não encontrado
	}
	
	void skipto(int []sync) throws IOException{
		do{
			if(m_lexer.getReader().ready())	
			advance();
		}	
		while(encontrado(sync, m_tok.m_tag) == -1 && m_lexer.getReader().ready());
		
	}

	public void program() throws IOException { // program ::= program identifier
												// body
		prod_atual = 1;
		switch (m_tok.m_tag) {
		case Tag.PROGRAM:
			eat(Tag.PROGRAM);
			eat(Tag.ID);
			body();
			break;
		default:
			error("program");
		}
	}

	private void body() throws IOException {
		// body ::= decl-list � {� stmt-list �}�
		
		prod_atual = 2;
		switch (m_tok.m_tag) {
		case '{':
		case Tag.BASIC:
			decllist();
			eat('{');
			stmtlist();
			eat('}');
			break;
		default:
			error("'{', integer ou real ");
		}
	}

	private void decllist() throws IOException {
		// decl-list ::= decl ; decl-list | lambda
		prod_atual = 3;
		switch (m_tok.m_tag) {
		case Tag.BASIC:
			decl();
			eat(';');
			decllist();
			break;
		case '{':
			break;
		default:
			error("'{', integer ou real");
		}
	}

	private void decl() throws IOException {
		// decl ::= type ident-list
		prod_atual = 4;
		switch (m_tok.m_tag) {
		case Tag.BASIC:
			type();
			identlist();
			break;
		default:
			error("integer ou real");
		}
	}

	private void identlist() throws IOException {
		// ident-list ::= identifier ident-list '
		prod_atual = 5;
		switch (m_tok.m_tag) {
		case Tag.ID:
			eat(Tag.ID);
			identlistline();
			break;
		default:
			error("integer ou real");
		}
	}

	private void identlistline() throws IOException {
		// ident-list' ::= , identifier ident-list' | lambda
		prod_atual = 6;
		switch (m_tok.m_tag) {
		case ',':
			eat(',');
			eat(Tag.ID);
			identlistline();
			break;
		case ';':
			break;
		default:
			error("integer ou real");
		}
	}

	private void type() throws IOException {
		// type ::= integer | real
		prod_atual = 7;
		switch (m_tok.m_tag) {
		case Tag.BASIC:
			eat(Tag.BASIC);
			break;
		default:
			error("integer ou real");
		}
	}

	private void stmtlist() throws IOException {
		// stmt-list ::= stmt ; stmt-list | lambda
		prod_atual = 8;
		switch (m_tok.m_tag) {
		case Tag.ID:
		case Tag.IF:
		case Tag.WHILE:
		case Tag.REPEAT:
		case Tag.READ:
		case Tag.WRITE:
			stmt();
			eat(';');
			stmtlist(); 
		break;
		case '}':
			break;
		case Tag.ELSE:
			break;
		case Tag.END:
			break;
		case Tag.UNTIL:
			break;
		default:
			error("identificador, if, while, repeat, read, write, '}', else, end, until");
		}
	}

	private void stmt() throws IOException {
		// stmt ::= assign-stmt | if-stmt | while-stmt | repeat-stmt | read-stmt
		// | write-stmt
		prod_atual = 9;
		switch (m_tok.m_tag) {
		case Tag.ID:
			assignstmt();
			break;
		case Tag.IF:
			ifstmt();
			break;
		case Tag.WHILE:
			whilestmt();
			break;
		case Tag.REPEAT:
			repeatstmt();
			break;
		case Tag.READ:
			readstmt();
			break;
		case Tag.WRITE:
			writestmt();
			break;
		default:
			error("identificador, if, while, repeat, read, ou write");
		}
	}

	private void assignstmt() throws IOException {
		// assign-stmt ::= identifier ":=" simple_expr
		prod_atual = 10;
		switch (m_tok.m_tag) {
		case Tag.ID:
			eat(Tag.ID);
			eat(Tag.ASSIGN);
			simpleexpr();
			break;
		default:
			error("identificador, if, while, repeat, read ou write");
		}
	}

	private void ifstmt() throws IOException {
		// if-stmt ::= if condition then stmt-list if-stmt
		prod_atual = 11;
		switch (m_tok.m_tag) {
		case Tag.IF:
			eat(Tag.IF);
			condition();
			eat(Tag.THEN);
			stmtlist();
			ifstmtline();
			break;
		default:
			error("if");
		}
	}

	private void ifstmtline() throws IOException {
		// if-stmt ' ::= else stmt-list end | end
		prod_atual = 12;
		switch (m_tok.m_tag) {
		case Tag.ELSE:
			eat(Tag.ELSE);
			stmtlist();
			eat(Tag.END);
			break;
		case Tag.END:
			eat(Tag.END);
			break;
		default:
			error("else ou end");
		}
	}

	private void condition() throws IOException {
		// condition ::= expression
		prod_atual = 13;
		switch (m_tok.m_tag) {
		case '(':
		case Tag.ID:
		case Tag.INTEGER:
		case Tag.REAL:
		case '!':
		case '-':
			expression();
			break;
		default:
			error("'(', identificador, constante inteira ou real, '!', '-'");
		}
	}

	private void repeatstmt() throws IOException {
		// repeat-stmt ::= repeat stmt-list stmt-suffix
		prod_atual = 14;
		switch (m_tok.m_tag) {
		case Tag.REPEAT:
			eat(Tag.REPEAT);
			stmtlist();
			stmtsuffix();
			break;
		default:
			error("repeat");
		}
	}

	private void stmtsuffix() throws IOException {
		// stmt-suffix ::= until condition
		prod_atual = 15;
		switch (m_tok.m_tag) {
		case Tag.UNTIL:
			eat(Tag.UNTIL);
			condition();
			break;
		default:
			error("until");
		}
	}

	private void whilestmt() throws IOException {
		// while-stmt ::= stmt-prefix stmt-list end
		prod_atual = 16;
		switch (m_tok.m_tag) {
		case Tag.WHILE:
			stmtprefix();
			stmtlist();
			eat(Tag.END);
			break;
		default:
			error("while");
		}
	}

	private void stmtprefix() throws IOException {
		// stmt-prefix ::= while condition do
		prod_atual = 17;
		switch (m_tok.m_tag) {
		case Tag.WHILE:
			eat(Tag.WHILE);
			condition();
			eat(Tag.DO);
			break;
		default:
			error("while");
		}
	}

	private void readstmt() throws IOException {
		// read-stmt ::= read "(" identifier ")"
		prod_atual = 18;
		switch (m_tok.m_tag) {
		case Tag.READ:
			eat(Tag.READ);
			eat('(');
			eat(Tag.ID);
			eat(')');
			break;
		default:
			error("read");
		}
	}

	private void writestmt() throws IOException {
		// write-stmt ::= write "(" writable ")"
		prod_atual = 19;
		switch (m_tok.m_tag) {
		case Tag.WRITE:
			eat(Tag.WRITE);
			eat('(');
			writable();
			eat(')');
			break;
		default:
			error("write");
		}
	}

	private void writable() throws IOException {
		// writable ::= simple-expr | literal
		prod_atual = 20;
		switch (m_tok.m_tag) {
		case '(':
		case Tag.ID:
			simpleexpr();
			break;
		case Tag.LITERAL:
			eat(Tag.LITERAL);
			break;
		default:
			error("identificador, '(' ou literal");
		}
	}

	private void expression() throws IOException {
		// expression ::= simple-expr expression'
		prod_atual = 21;
		switch (m_tok.m_tag) {
		case '(':
		case Tag.ID:
		case Tag.INTEGER:
		case Tag.REAL:
		case '!':
		case '-':
			simpleexpr();
			expressionline();
			break;
		default:
			error("'(', identificador, constante inteira ou real, '!' ou '-'");
		}
	}

	private void expressionline() throws IOException {
		// expressionline ::= relop simple-expr | lambda
		prod_atual = 22;
		switch (m_tok.m_tag) {
		case ('='):
		case ('>'):
		case ('<'):
		case Tag.MAIOREQ:
		case Tag.MENOREQ:
		case Tag.DIFERENTE:
			relop();
			simpleexpr();
			break;
		case ')':
			break;
		case ';':
			break;
		case Tag.THEN:
			break;
		case Tag.DO:
			break;
		default:
			error("'=', '>', '<', '>=', '<=', '(', ';', then ou do");
		}
	}

	private void simpleexpr() throws IOException {
		// simple-expr ::= term simple-expr'
		prod_atual = 23;
		switch (m_tok.m_tag) {
		case '(':
		case Tag.ID:
		case Tag.INTEGER:
		case Tag.REAL:
		case ('!'):
		case ('-'):
			term();
			simpleexprline();
			break;
		default:
			error("')', identificador, real, inteiro, '!', '-'");
		}
	}

	private void simpleexprline() throws IOException {
		// simple-expr' ::= addop term simple-expr' | lambda
		prod_atual = 24;
		switch (m_tok.m_tag) {
		case ('+'):
		case ('-'):
		case Tag.OR:
			addop();
			term();
			simpleexprline();
			break;
		case (')'):
		case (Tag.THEN):
		case Tag.DO:
		case (';'):
		case ('='):
		case ('>'):
		case ('<'):
		case Tag.MAIOREQ:
		case Tag.MENOREQ:
		case Tag.DIFERENTE:
			break;
		default:
			int []follow = {Tag.OR, (int)')', Tag.THEN, Tag.DO, (int)';', (int)'=', (int)'>', (int)'<', Tag.MAIOREQ, Tag.MENOREQ, Tag.DIFERENTE, 65535};
			error("'+', '-', OR, ')', then, do, ';', '=', '>', '<=', '>=', '!='");
			//skipto(follow);
		}
	}

	private void term() throws IOException {
		// term ::= factor-a term'
		prod_atual = 25;
		switch (m_tok.m_tag) {
		case '(':
		case Tag.ID:
		case Tag.INTEGER:
		case Tag.REAL:
		case ('!'):
		case ('-'):
			factora();
			termline();
			break;
		default:
			error("'(', identificador, real, inteiro, '!', '-'");
		}
	}

	private void termline() throws IOException {
		// term' ::= mulop factor-a term' | lambda
		prod_atual = 26;
		switch (m_tok.m_tag) {
		case ('*'):
		case ('/'):
		case Tag.AND:
			mulop();
			factora();
			termline();
		case ('+'):
		case ('-'):
		case Tag.OR:
		case (')'):
		case (Tag.THEN):
		case Tag.DO:
		case (';'):
		case ('='):
		case ('>'):
		case ('<'):
		case Tag.MAIOREQ:
		case Tag.MENOREQ:
		case Tag.DIFERENTE:
			break;
		default:
			int []follow = {(int)'+', (int)'-', Tag.OR, (int)')', 65535};
			error("'+', '-', OR, ')', then, do, ';', '=', '>', '<=', '>=', '!='");
			//skipto(follow);
		}
	}

	private void factora() throws IOException {
		// fator-a ::= factor | ! factor | "-" factor
		prod_atual = 27;
		switch (m_tok.m_tag) {
		case '(':
		case Tag.ID:
		case Tag.INTEGER:
		case Tag.REAL:
			factor();
			break;
		case '!':
			eat('!');
			factor();
			break;
		case '-':
			eat('-');
			factor();
			break;
		default:
			error("'(', identificador, real, inteiro, '!', '-'");
		}
	}

	private void factor() throws IOException {
		// factor ::= identifier | constant | "(" expression ")"
		prod_atual = 28;
		switch (m_tok.m_tag) {

		case Tag.ID:
			eat(Tag.ID);
			break;
		case Tag.INTEGER:
			eat(Tag.INTEGER);
			break;
		case Tag.REAL:
			eat(Tag.REAL);
			break;
		case '(':
			eat('(');
			expression();
			eat(')');
			break;
		default:
			error("identificador, real, inteiro, '('");
		}
	}

	private void addop() throws IOException {
		// addop ::= "+" | "-" | ||
		prod_atual = 29;
		switch (m_tok.m_tag) {
		case ('+'):
			eat('+');
			break;
		case ('-'):
			eat('-');
			break;
		case Tag.OR:
			eat(Tag.OR);
			break;
		default:
			error("'+', '-', '||'");
		}

	}

	private void relop() throws IOException {
		// relop ::= "=" | ">" | ">=" | "<" | "<=" | "!="
		prod_atual = 30;
		switch (m_tok.m_tag) {
		case ('='):
			eat('+');
			break;
		case ('>'):
			eat('>');
			break;
		case ('<'):
			eat('<');
			break;
		case Tag.MAIOREQ:
			eat(Tag.MAIOREQ);
			break;
		case Tag.MENOREQ:
			eat(Tag.MENOREQ);
			break;
		case Tag.DIFERENTE:
			eat(Tag.DIFERENTE);
			break;
		default:
			error("'=', '>', '<', '>=', '<=', '!='");
		}
	}

	private void mulop() throws IOException {
		// mulop ::= "*" | "/" | &&
		prod_atual = 31;
		switch (m_tok.m_tag) {
		case ('*'):
			eat('*');
			break;
		case ('/'):
			eat('/');
			break;
		case Tag.AND:
			eat(Tag.AND);
			break;
		default:
			error("'*', '/', AND");
		}
	}
	
	public int getNumErros(){
		
		return num_erros;
		
	}
}


