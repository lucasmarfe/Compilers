package lexer;

public class Tag {
	//TODO Lucas - Acrescentar as tags de acordo com a especificação do trabalho
	public final static int 	PROGRAM = 256;
	public final static int ACOL = 257; //Abre Colchetes
	public final static int FCOL = 258; //Fecha colchetes
	public final static int ACHA = 259; //Abre chaves
	public final static int FCHA = 260; //Fecha chaves
	public final static int APAR = 261; //Abre parentesis
	public final static int FPAR = 262; //Fecha parentesis
	public final static int PVG = 263; //Ponto e vírgula
	public final static int VG = 264; //Vírgula
	public final static int ID = 265; //Identificador
	public final static int INTEGER = 266; //Integer
	public final static int REAL = 267; //Real
	public final static int LITERAL = 268; //Literal
	public final static int	INDEX = 269; //Será usada na árvore sintática, conforme implementação Aho (2006)
	public final static int MINUS = 270; //Será usada na árvore sintática, conforme implementação Aho (2006)
	public final static int TEMP = 271; //Será usada na árvore sintática, conforme implementação Aho (2006)
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
	public final static int EQ = 283;//Igual =
	public final static int ME = 284; // Menor <
	public final static int MEEQ = 285; // Menor igual <=
	public final static int MA = 286; // Maior >
	public final static int MAEQ = 287; // Maior igual >=
	public final static int NEQ = 288;//Diferente !=
	public final static int MENOS = 289; //Operador -
	public final static int MAIS = 290; //Operador +
	public final static int DIV = 291; //Operador /
	public final static int MULT = 292; //Operador *
	public final static int INTEGER_CONST = 293; // Constante inteira
	public final static int FLOAT_CONST = 294; // Constante float
	public final static int ASSIGN = 264; //Operador assign :=
}
