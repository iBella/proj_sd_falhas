package app.ObservablePattern;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import app.janelas.Ponto;

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
	
	public void enviarMensagem(Ponto p, String ip) throws UnknownHostException,ConnectException, IOException{
		//Porta do cliente 6001
		Socket socket = new Socket(ip, 6001);
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.writeObject(p.toString());				///////////////////
		out.flush();
		out.close();
		socket.close();
	}
	
	@Override
	public void update(Observable arg0, Object arg1) throws ConnectException, UnknownHostException, IOException {
		// TODO Auto-generated method stub
		enviarMensagem(((Ponto)arg1), getIp());
	}

}
