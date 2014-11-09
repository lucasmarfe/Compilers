package lexer;

public class Token {
	public final int m_tag;
	public final String m_desc;

	public Token(int m_tag) {
		this.m_tag = m_tag;
		m_desc = "";
	}

	public Token(int p_tag, String desc) {
		this.m_tag = p_tag;
		this.m_desc = desc;
	}

	@Override
	public String toString() {
		if (this.m_tag == Tag.COMENTARIO || this.m_tag == Tag.ERRO) {
			return m_desc;
		}

		return "" + (char) m_tag;
	}

}
