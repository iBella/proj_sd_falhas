package app.cliente;

import java.net.Socket;

import app.janelas.TelaApp;
import server.GenericConsumer;
import server.GenericResource;
import server.Server;

public class ClienteListener extends Server implements Runnable{
	
	TelaApp janela;
	
	public ClienteListener(int port , TelaApp janela) {
		super(port);
		this.janela = janela;
	}

	@Override
	protected GenericConsumer<Socket> createSocketConsumer(GenericResource<Socket> r) {
		return new ClienteConsumer<>(r,janela);
	}

	@Override
	public void run() {
		this.begin();
	}

}
