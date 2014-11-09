package lexer;

public class Literal extends Token{
	
	public final String m_value;
	
	public Literal(String m_value) {
		super(Tag.LITERAL);
		this.m_value = m_value;
	}
	
	@Override
	public String toString() {
		return m_value;
	}
}