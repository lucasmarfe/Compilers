package symbols;

import java.util.Hashtable;

import lexer.Token;

public class Env {
	private Hashtable table;

	public Env(Env n) { table = new Hashtable();}

	public void put(Token w, Id i) { table.put(w, i); }

	public Id get(Token w) {
		return (Id) table.get(w);
	}
}
