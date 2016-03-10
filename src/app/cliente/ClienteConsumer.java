package app.cliente;

import java.awt.Color;
import java.io.ObjectInputStream;
import java.net.Socket;

import app.janelas.Ponto;
import app.janelas.TelaApp;
import server.GenericConsumer;
import server.GenericResource;

public class ClienteConsumer<S extends Socket> extends GenericConsumer<S> {
	
	TelaApp janela;
	
	public ClienteConsumer(GenericResource<S> re, TelaApp janela) {
		super(re);
		this.janela = janela;
	}

	@Override
	protected void doSomething(S str) {
		try{
			// TODO Auto-generated method stub
			ObjectInputStream in = new ObjectInputStream(str.getInputStream());
			
			//Ponto ponto = (Ponto) in.readObject();
			
			String entrada = (String) in.readObject();
			
			String[] ponto = entrada.toString().split(" ");
	     	Color c = new Color(Integer.parseInt(ponto[2]));
	     	janela.addPonto(new Ponto(Integer.parseInt(ponto[0]), Integer.parseInt(ponto[1]), c));
			
			//System.err.println("Server: " + ponto.toString());
			
			str.close();
				
		}catch (Exception e){
			e.printStackTrace();
			
		}
	}

}
