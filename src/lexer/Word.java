package lexer;

import symbols.Type;

public class Word extends Token{
	
	public String m_lexema = "";
	public Boolean is_First_Time;
	public Boolean was_Declared;
	
	public Type m_Tipo = null;
	
	public Word(String m_cadeia, int m_tag) {
		super(m_tag);
		this.m_lexema = m_cadeia;
		is_First_Time = true;
		was_Declared = false;
	}
	
	@Override
	public String toString() {
		return m_lexema;
	}
	//Criacao de palavras reservadas com suas respectivas tags
	public static final Word NotEqual = new Word( "!=", Tag.DIFERENTE );
	public static final Word Assign = new Word( ":=", Tag.ASSIGN );
	public static final Word LessEqual= new Word( "<=", Tag.MENOREQ );
	public static final Word GreaterEqual = new Word( ">=", Tag.MAIOREQ );
	public static final Word And= new Word( "&&", Tag.AND );
	public static final Word Or = new Word( "||", Tag.OR );
	public static final Word minus = new Word( "minus", Tag.MINUS ); //Sera usada na arvore sintatica, conforme implementacao Aho (2006)
	public static final Word temp = new Word( "t", Tag.TEMP ); //Sera usada na arvore sintatica, conforme implementacao Aho (2006)
}
