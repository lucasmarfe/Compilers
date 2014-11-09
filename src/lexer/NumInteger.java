package lexer;

public class NumInteger extends Token{
	
	public final int m_value;
	
	public NumInteger(int m_value) {
		super(Tag.INTEGER_CONST);
		this.m_value = m_value;
	}
	
	@Override
	public String toString() {
		return "" + m_value;
	}
}
