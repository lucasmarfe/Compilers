package lexer;

public class NumInteger extends Token{
	
	public final int m_value;
	
	public NumInteger(int p_value) {
		super(Tag.INTEGER_CONST);
		m_value = p_value;
	}
	
	@Override
	public String toString() {
		return "" + m_value;
	}
}
