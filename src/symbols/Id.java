package symbols;

import lexer.Token;
import lexer.Word;

public class Id {
	
	public Token op;
	public Type type;
	public int offset;     // relative address
	
	public Id(Word id, Type p, int b)
	{ 
		op = id; 
		type = p;
		offset = b; 
	}
}
