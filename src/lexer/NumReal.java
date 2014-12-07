package lexer;

public class NumReal extends Token {
	
	public final float m_value;
	
	public NumReal(float m_value) {
		super(Tag.REAL);
		this.m_value = m_value;
	}
	
	@Override
	public String toString() {
		return "" + m_value;
	}

}
