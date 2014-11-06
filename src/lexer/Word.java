package lexer;

public class Word extends Token{
	
	public String m_lexema = "";
	
	public Word(String p_cadeia, int p_tag) {
		super(p_tag);
		m_lexema = p_cadeia;
	}
	
	@Override
	public String toString() {
		return m_lexema;
	}
	//Criação de palavras reservadas com suas respectivas tags
	public static final Word Program = new Word( "program", Tag.PROGRAM );
	public static final Word Int = new Word( "integer", Tag.INTEGER );
	public static final Word Real = new Word( "real", Tag.REAL );
	public static final Word If = new Word( "if", Tag.IF );
	public static final Word Then = new Word( "then", Tag.THEN );
	public static final Word End = new Word( "end", Tag.END );
	public static final Word Else = new Word( "else", Tag.ELSE );
	public static final Word Repeat = new Word( "repeat", Tag.REPEAT );
	public static final Word Until = new Word( "until", Tag.UNTIL );
	public static final Word While = new Word( "while", Tag.WHILE );
	public static final Word Do= new Word( "do", Tag.DO );
	public static final Word Read= new Word( "read", Tag.READ );
	public static final Word Write= new Word( "write", Tag.WRITE);
	public static final Word Equal = new Word( "=", Tag.EQ );
	public static final Word NotEqual = new Word( "!=", Tag.NEQ );
	public static final Word Less= new Word( "!=", Tag.MENOR );
	public static final Word Greater = new Word( ">", Tag.MAIOR );
	public static final Word LessEqual= new Word( "<=", Tag.MENOREQ );
	public static final Word GreaterEqual = new Word( ">=", Tag.MAIOREQ );
	public static final Word And= new Word( "&&", Tag.AND );
	public static final Word Or = new Word( "||", Tag.OR );
	public static final Word Menos= new Word( "!=", Tag.MENOS );
	public static final Word Mais = new Word( "II", Tag.MAIS );
	public static final Word Multiplica= new Word( "!=", Tag.MULT );
	public static final Word Divide = new Word( "II", Tag.DIV );
	public static final Word minus = new Word( "minus", Tag.MINUS ); //Será usada na árvore sintática, conforme implementação Aho (2006)
	public static final Word temp = new Word( "t", Tag.TEMP ); //Será usada na árvore sintática, conforme implementação Aho (2006)
}
