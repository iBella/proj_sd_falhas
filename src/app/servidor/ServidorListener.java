package app.servidor;

import java.net.Socket;

import app.ObservablePattern.Observable;
import server.GenericConsumer;
import server.GenericResource;
import server.Server;

public class ServidorListener extends Server implements Runnable{
	
	Observable observable;
	
	public ServidorListener(int port, Observable o) {
		super(port);
		this.observable = o;
	}

	@Override
	protected GenericConsumer<Socket> createSocketConsumer(GenericResource<Socket> r) {
		// TODO Auto-generated method stub
		return new ServidorConsumer<>(r, observable);
	}

	@Override
	public void run() {
		this.begin();
	}

}
