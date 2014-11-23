package lexer;

public class Tag {
	public final static int PROGRAM = 256;
	public final static int ID = 265; //Identificador
	public final static int INTEGER = 266; //Integer - Palavra reservada
	public final static int REAL = 267; //Real -Palavra reservada
	public final static int LITERAL = 268; //Literal
	public final static int	INDEX = 269; //Sera usada na arvore sintatica, conforme implementacao Aho (2006)
	public final static int MINUS = 270; //Sera usada na arvore sintatica, conforme implementacao Aho (2006)
	public final static int TEMP = 271; //Sera usada na arvore sintatica, conforme implementacao Aho (2006)
	public final static int IF = 272; //if
	public final static int THEN = 273; //then
	public final static int END = 274; //end
	public final static int ELSE = 275; //else
	public final static int REPEAT = 276; //repeat
	public final static int UNTIL = 277; //until
	public final static int WHILE = 278; //while
	public final static int DO = 279; //do
	public final static int READ = 280; //read
	public final static int	OR = 281; //Operador || or
	public final static int	AND = 282; //Operador && and
	public final static int MENOREQ = 285; // Menor igual <=
	public final static int MAIOREQ = 287; // Maior igual >=
	public final static int DIFERENTE = 288;//Diferente !=
	public final static int INTEGER_CONST = 293; // Constante inteira
	public final static int FLOAT_CONST = 294; // Constante float
	public final static int ASSIGN = 295; //Operador assign :=
	public final static int WRITE = 296; //write
	public final static int BASIC = 297; //
	public final static int ERRO = -10; // Erro
	public final static int COMENTARIO = -11;
}
