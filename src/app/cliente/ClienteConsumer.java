package app.cliente;

import java.awt.Color;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;

import app.Mensagem;
import app.ObservablePattern.ClienteObserver;
import app.ObservablePattern.ServidorObserver;
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
			ObjectInputStream in = new ObjectInputStream(str.getInputStream());
			
			Object o = in.readObject();
			if(o instanceof String){
				//Ponto ponto = (Ponto) in.readObject();
				
				//String entrada = (String) in.readObject();
				
//				String[] ponto = o.toString().split(" ");
//		     	Color c = new Color(Integer.parseInt(ponto[2]));
//		     	janela.addPonto(new Ponto(Integer.parseInt(ponto[0]), Integer.parseInt(ponto[1]), c));
				
				LinkedList<String> pacote = new LinkedList<>();
				String[] pontos = o.toString().split(";");
		     	for(String ponto: pontos){
		     		System.err.println(ponto);
		     		pacote.add(ponto);
		     	}
				
		     	janela.addPacote(pacote);
				
				//System.err.println("Recebendo Ponto: " + o + );
			}else{
				switch (((Mensagem)o).getTipo()) {
				case 1:
					System.err.println("Passei no cliente consumer");
					ServidorObserver servidor = (ServidorObserver)((Mensagem)o).getObjeto();
					Socket skt = new Socket(servidor.getIp(),servidor.getPorta());
					ObjectOutputStream out = new ObjectOutputStream(skt.getOutputStream());
					out.writeObject(new Mensagem(0,new ClienteObserver(Cliente.ip)));
					out.flush();
					out.close();
					skt.close();
					break;

				default:
					break;
				}
			}
			str.close();
				
		}catch (Exception e){
			e.printStackTrace();
			
		}
	}

}
