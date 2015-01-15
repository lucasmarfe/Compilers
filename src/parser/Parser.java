package parser;

import java.io.IOException;

import symbols.Type;

import lexer.Lexer;
import lexer.Tag;
import lexer.Token;
import lexer.Word;

public class Parser {

	private Token m_tok;
	
	private Lexer m_lexer;
	
	private int num_erros = 0;
	
	private int prod_atual; //Apenas para auxiliar na verifica√ß√£o do parser

	public Parser(Lexer lex) throws Exception {
		m_lexer = lex;
		advance();
	}

	Token advance() throws Exception {
		m_tok = m_lexer.getToken();
		return m_tok;
	}

	void eat(int t) throws Exception {

		if (m_tok.m_tag == t)
			advance();
		else
			error(Character.toString((char)t)); 
	}

	void error(String follow) {
		num_erros++;
		System.out.println("Erro Sintatico na producao " + prod_atual +  ": " + "Esperava encontrar: " + follow + " Encontrado: " +  m_tok.toString() + " na linha " + m_lexer.m_line);
	}
	
	
	int encontrado(int []sync, int tok){
		for(int i = 0; i < sync.length; i++){
			if(tok == sync[i]){
				return tok;
			}	
		}
		return -1; // n√£o encontrado
	}
	
	void skipto(int []sync) throws Exception{
		do{
			if(m_lexer.getReader().ready())	
			advance();
		}	
		while(encontrado(sync, m_tok.m_tag) == -1 && m_lexer.getReader().ready());
		
	}

	public void program() throws Exception { // program ::= program identifier
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

	private void body() throws Exception {
		// body ::= decl-list ÔøΩ {ÔøΩ stmt-list ÔøΩ}ÔøΩ
		
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

	private void decllist() throws Exception {
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

	private void decl() throws Exception {
		// decl ::= type ident-list
		prod_atual = 4;
		switch (m_tok.m_tag) {
		case Tag.BASIC:
			Type l_type = type();
			identlist(l_type);
			break;
		default:
			error("integer ou real");
		}
	}

	private void identlist(Type p_type) throws Exception {
		// ident-list ::= identifier ident-list '
		prod_atual = 5;
		switch (m_tok.m_tag) {
		case Tag.ID:
			if(! (((Word)m_tok).is_First_Time))
				throw new Exception("Variavel " + ((Word)m_tok).m_lexema +" declarada mais de uma vez.");
			else
			{
				((Word)m_tok).was_Declared = true;
				((Word)m_tok).m_Tipo = p_type;
			}
			eat(Tag.ID);
			identlistline();
			break;
		default:
			error("integer ou real");
		}
	}

	private void identlistline() throws Exception {
		// ident-list' ::= , identifier ident-list' | lambda
		prod_atual = 6;
		switch (m_tok.m_tag) {
		case ',':
			eat(',');
			if(! (((Word)m_tok).is_First_Time))
				throw new Exception("Variavel " + ((Word)m_tok).m_lexema +" declarada mais de uma vez.");
			else
				((Word)m_tok).was_Declared = true;
			
			eat(Tag.ID);
			identlistline();
			break;
		case ';':
			break;
		default:
			error("integer ou real");
		}
	}

	private Type type() throws Exception {
		// type ::= integer | real
		prod_atual = 7;
		switch (m_tok.m_tag) {
		case Tag.BASIC:
			Token l_aux =m_tok;
			eat(Tag.BASIC);
			if(((Word)l_aux).m_lexema.equals("integer"))
				return Type.Int;
			if(((Word)l_aux).m_lexema.equals("real"))
				return Type.Real;
		default:
			error("integer ou real");
		}
		return null;
	}

	private void stmtlist() throws Exception {
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

	private void stmt() throws Exception {
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

	private void assignstmt() throws Exception { //TODO Verificar se variavel foi declarada - OK
		// assign-stmt ::= identifier ":=" simple_expr
		prod_atual = 10;
		switch (m_tok.m_tag) {
		case Tag.ID:
			if(! (((Word)m_tok).was_Declared))
				throw new Exception("Variavel " + ((Word)m_tok).m_lexema +" n„o declarada.");
			Type l_typeID = m_tok instanceof Word ? ((Word)m_tok).m_Tipo : null;
			eat(Tag.ID);
			eat(Tag.ASSIGN);
			Type l_typeExp = simpleexpr();
			if (l_typeID!= null && l_typeID.equals(Type.Int) && l_typeExp.equals(Type.Real))
				System.out.println("Warning: PossÌvel atribuiÁ„o de um valor real a uma varÌavel inteira na linha " + m_lexer.m_line);
			break;
		default:
			error("identificador, if, while, repeat, read ou write");
		}
	}

	private void ifstmt() throws Exception {
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

	private void ifstmtline() throws Exception {
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

	private void condition() throws Exception {
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

	private void repeatstmt() throws Exception {
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

	private void stmtsuffix() throws Exception {
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

	private void whilestmt() throws Exception {
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

	private void stmtprefix() throws Exception {
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

	private void readstmt() throws Exception { //TODO Verificar se variavel foi declarada - OK
		// read-stmt ::= read "(" identifier ")"
		prod_atual = 18;
		switch (m_tok.m_tag) {
		case Tag.READ:
			eat(Tag.READ);
			eat('(');
			if(! (((Word)m_tok).was_Declared))
				throw new Exception("Variavel " + ((Word)m_tok).m_lexema +" n„o declarada.");
			eat(Tag.ID);
			eat(')');
			break;
		default:
			error("read");
		}
	}

	private void writestmt() throws Exception {
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

	private void writable() throws Exception {
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

	private void expression() throws Exception {
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

	private void expressionline() throws Exception {
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

	private Type simpleexpr() throws Exception {
		// simple-expr ::= term simple-expr'
		prod_atual = 23;
		switch (m_tok.m_tag) {
		case '(':
		case Tag.ID:
		case Tag.INTEGER:
		case Tag.REAL:
		case ('!'):
		case ('-'):
			Type l_return = term();
			simpleexprline();
			return l_return;
		default:
			error("')', identificador, real, inteiro, '!', '-'");
		}
		return Type.Int;
	}

	private void simpleexprline() throws Exception {
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

	private Type term() throws Exception {
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
			Type l_return = termline();
			return l_return;
		default:
			error("'(', identificador, real, inteiro, '!', '-'");
		}
		return Type.Int;
	}

	private Type termline() throws Exception {
		// term' ::= mulop factor-a term' | lambda
		prod_atual = 26;
		switch (m_tok.m_tag) {
		case ('*'):
		case ('/'):
		case Tag.AND:
			mulop();
			factora();
			termline();
			return Type.Real;
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
		return Type.Int;
	}

	private void factora() throws Exception {
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

	private void factor() throws Exception { //TODO Verificar se variavel foi declarada
		// factor ::= identifier | constant | "(" expression ")"
		prod_atual = 28;
		switch (m_tok.m_tag) {

		case Tag.ID:
			if(! (((Word)m_tok).was_Declared))
				throw new Exception("Variavel " + ((Word)m_tok).m_lexema +" n„o declarada.");
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

	private void addop() throws Exception {
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

	private void relop() throws Exception {
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

	private void mulop() throws Exception {
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


