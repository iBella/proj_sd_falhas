package app.cliente;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

import app.Mensagem;
import app.ObservablePattern.ClienteObserver;
import app.janelas.TelaApp;

public class Cliente {
	
	private TelaApp janela;
	private ClienteListener listener;
	private Properties prop;
	
	public Cliente(int porta) {
		super();
		this.setProp(getNewProp());
		this.janela = new TelaApp();
		this.listener = new ClienteListener(porta, janela);
		Thread tJanela = new Thread(janela);
		tJanela.start();
		Thread tListener = new Thread(listener);
		tListener.start();
		//conectando com o server
		conexaoServer(prop.getProperty("primario"));
	}
	
	public static Properties getNewProp() { 
		Properties props = new Properties(); 
		FileInputStream file;
		try {
			file = new FileInputStream( "config.properties");
			props.load(file); 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return props; 
	}

	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}

	public void conexaoServer(String ipServidor){
		try {
			//Porta do servidor 6000
			Socket socket = new Socket(ipServidor, 6000);
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(new Mensagem(0,new ClienteObserver(InetAddress.getLocalHost().getHostAddress())));
			out.flush();
			out.close();
			socket.close();
			
		} catch (ConnectException e) {
			System.err.println("Error ao conectar com o Servidor "+ipServidor+" !");
			conexaoServer(prop.getProperty("secundario"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		new Cliente(6001);
		
		InetAddress i;
		try {
			i = InetAddress.getLocalHost();
			System.err.println(i.getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}    
	     
	}
	
}
