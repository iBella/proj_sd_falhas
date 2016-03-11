package app.cliente;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.LinkedList;

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
			ObjectInputStream in = new ObjectInputStream(str.getInputStream());
			
			String entrada = (String) in.readObject();
			
			LinkedList<String> pacote = new LinkedList<>();
			String[] pontos = entrada.toString().split(";");
			System.err.println(pontos[0]);
	     	for(String ponto: pontos)
	     		pacote.add(ponto);
			
	     	janela.addPacote(pacote);
			
			str.close();
				
		}
		catch (Exception e){ e.printStackTrace(); }
	}

}
