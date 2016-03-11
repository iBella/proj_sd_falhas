package app;

import java.io.Serializable;

public class Mensagem implements Serializable{

	private static final long serialVersionUID = 4835047122198330641L;
	private int tipo;
	private Object objeto;
	
	public Mensagem(int tipo){
		this.tipo = tipo;
		this.objeto = null;
	}
	
	public Mensagem(int tipo, Object objeto) {
		super();
		this.tipo = tipo;
		this.objeto = objeto;
	}

	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public Object getObjeto() {
		return objeto;
	}
	public void setObjeto(Object objeto) {
		this.objeto = objeto;
	}
	
}
