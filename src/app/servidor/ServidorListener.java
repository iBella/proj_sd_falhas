package app.servidor;

import java.net.Socket;

import app.ObservablePattern.Observable;
import app.ObservablePattern.ServidorObservable;
import server.GenericConsumer;
import server.GenericResource;
import server.Server;

public class ServidorListener extends Server implements Runnable{
	
	Observable observable;
	ServidorObservable servidores;
	
	public ServidorListener(int port, Observable o, ServidorObservable s) {
		super(port);
		this.observable = o;
		this.servidores = s;
	}

	@Override
	protected GenericConsumer<Socket> createSocketConsumer(GenericResource<Socket> r) {
		// TODO Auto-generated method stub
		return new ServidorConsumer<>(r, observable, servidores);
	}

	@Override
	public void run() {
		this.begin();
	}

}
