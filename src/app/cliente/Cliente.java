package app.cliente;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

import app.Mensagem;
import app.ObservablePattern.ClienteObserver;
import app.ObservablePattern.ServidorObserver;
import app.janelas.TelaApp;
import app.servidor.Servidor;

public class Cliente {
	
	private TelaApp janela;
	private ClienteListener listener;
	private Properties prop;
	public static String ip;
	public static int PORTA = 5999; 
	
	public Cliente(String ip) {
		super();
		Cliente.ip = ip;
		this.setProp(getNewProp());
		this.janela = new TelaApp();
		this.listener = new ClienteListener(PORTA, janela);
		Thread tJanela = new Thread(janela);
		tJanela.start();
		Thread tListener = new Thread(listener);
		tListener.start();
		//conectando com o server
		novaConexaoComServidores();
	}
	
	public static Properties getNewProp() { 
		Properties props = new Properties(); 
		FileInputStream file;
		try {
			file = new FileInputStream("config.properties");
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

	public void novaConexaoComServidores(){
		for (Object s : prop.keySet()) {
			String server = prop.getProperty((String)s);
			String[] configServer = server.split(" ");
			try {
				Socket skt = new Socket(configServer[0], Integer.parseInt(configServer[1]));
				ObjectOutputStream out = new ObjectOutputStream(skt.getOutputStream());
				out.writeObject(new Mensagem(2,new ClienteObserver(ip)));
				out.flush();
				out.close();
				skt.close();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("Nao foi possivel conectar ao servidor "+configServer[0]);
				//e.printStackTrace();
			}
		}
			
	}
	
	public static void main(String[] args){
		new Cliente(args[0]);
		System.err.println("Ip: "+args[0]);    
	     
	}
	
}
