package lexer;

public class Literal extends Token{
	
	public final String m_value;
	
	public Literal(String p_value) {
		super(Tag.LITERAL);
		m_value = p_value;
	}
	
	@Override
	public String toString() {
		return m_value;
	}
}