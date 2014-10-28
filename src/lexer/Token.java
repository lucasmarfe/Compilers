package lexer;

public class Token 
{
	public final int m_tag;
	
	public Token(int p_tag) {
		this.m_tag = p_tag;
	}

	@Override
	public String toString() {
		return "" + (char)m_tag;
	}
	
}
