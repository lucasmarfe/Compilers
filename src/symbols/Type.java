package symbols;

import lexer.Tag;
import lexer.Word;

public class Type extends Word{
	
	public int m_tamanho = 0 ; //Tamanho da alocação de armazenamento
	
	public Type(String p_cadeia, int p_tag, int p_tamanho) {
		super(p_cadeia, p_tag);
		m_tamanho = p_tamanho;
	}
	
	public static final Type Int= new Type( "integer", Tag.BASIC, 4 );
	public static final Type Real = new Type( "real", Tag.BASIC, 8 );
}
