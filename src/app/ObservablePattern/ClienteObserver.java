package app.ObservablePattern;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import app.cliente.Cliente;
import app.janelas.Ponto;
import app.servidor.Servidor;

public class ClienteObserver implements Observer,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8462252684251280550L;
	private String ip;
	
	public ClienteObserver(String ip) {
		super();
		this.ip = ip;
	}

	@Override
	public String toString() {
		return "ip=" + ip;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public void enviarMensagem(String p, String ip) throws UnknownHostException,ConnectException, IOException{
		//Porta do cliente 6001
		Socket socket = new Socket();
		socket.connect(new InetSocketAddress(ip, Cliente.PORTA), Servidor.timeout);
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
//		out.writeObject(p);
		out.writeObject(p.toString());
		out.flush();
		out.close();
		socket.close();
		//System.out.println(">>>>"+this.ip);
	}
	
	@Override
	public void update(Object arg0, Object arg1) throws ConnectException, UnknownHostException, IOException {
		// TODO Auto-generated method stub
		
		//System.out.println("+++++"+this.ip);
		enviarMensagem(((String)arg1), getIp());
	}

}
