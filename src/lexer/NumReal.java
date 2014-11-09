package lexer;

public class NumReal extends Token {
	
	public final float m_value;
	
	public NumReal(float m_value) {
		super(Tag.FLOAT_CONST);
		this.m_value = m_value;
	}
	
	@Override
	public String toString() {
		return "" + m_value;
	}

}
