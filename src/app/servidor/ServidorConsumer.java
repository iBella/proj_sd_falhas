package app.servidor;

import java.io.ObjectInputStream;
import java.net.Socket;

import app.Mensagem;
import app.ObservablePattern.ClienteObserver;
import app.ObservablePattern.Observable;
import app.ObservablePattern.Observer;
import server.GenericConsumer;
import server.GenericResource;

public class ServidorConsumer<S extends Socket> extends GenericConsumer<S> {
	
	Observable observable;
	
	public ServidorConsumer(GenericResource<S> re, Observable o) {
		super(re);
		this.observable = o;
	}
	
	@Override
	protected void doSomething(S str) {
		try{
			ObjectInputStream in = new ObjectInputStream(str.getInputStream());
			
			Mensagem msg = (Mensagem) in.readObject();
			
			switch (msg.getTipo()) {
			case 0:
				observable.register((Observer)msg.getObjeto());
				System.err.println("Cliente: "+((ClienteObserver)msg.getObjeto()).getIp()+" conectou ao Servidor.");
				break;

			default:
				break;
			}
			
			str.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}
