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
	public static final Word NotEqual = new Word( "!=", Tag.NEQ );
	public static final Word LessEqual= new Word( "<=", Tag.MENOREQ );
	public static final Word GreaterEqual = new Word( ">=", Tag.MAIOREQ );
	public static final Word And= new Word( "&&", Tag.AND );
	public static final Word Or = new Word( "||", Tag.OR );
	public static final Word minus = new Word( "minus", Tag.MINUS ); //Será usada na árvore sintática, conforme implementação Aho (2006)
	public static final Word temp = new Word( "t", Tag.TEMP ); //Será usada na árvore sintática, conforme implementação Aho (2006)
}
