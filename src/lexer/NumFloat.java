package lexer;

public class NumFloat extends Token {
	
	public final float m_value;
	
	public NumFloat(float p_value) {
		super(Tag.INTEGER_CONST);
		m_value = p_value;
	}
	
	@Override
	public String toString() {
		return "" + m_value;
	}

}
