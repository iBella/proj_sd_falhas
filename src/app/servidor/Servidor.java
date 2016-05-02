package app.servidor;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import app.Mensagem;
import app.ObservablePattern.Observable;
import app.ObservablePattern.Observer;
import app.ObservablePattern.ServidorObservable;
import app.ObservablePattern.ServidorObserver;
import app.janelas.Ponto;

public class Servidor {
	
	private ServidorListener listener;
	private Observable observable;
	private ServidorObservable servidores;
	public static boolean primario;
	private Properties prop;
	public static int timeout = 2000;
	public static ServidorInfo info;
	
	//public static String ip;
	//public static int porta;
	public static ServidorObserver servidorPrimario;
	
	private HashMap<Ponto, Integer> listaNova = new HashMap<Ponto, Integer>();
	private static int qtPonto = 0;
	
	public boolean isPrimario() {
		return primario;
	}

	public void setPrimario(boolean primario) {
		Servidor.primario = primario;
	}

	public Servidor(){
		super();
		this.prop = getNewProp();
		info = new ServidorInfo();
		pegarConfiguracoes();
		System.err.println("Porta : "+ info.getPorta());
		this.setPrimario(false);
		servidorPrimario = null;
		this.servidores = new ServidorObservable();
		this.observable = new Observable(this.servidores);
		this.listener = new ServidorListener(info.getPorta(), observable, servidores);
		Thread tListener = new Thread(listener);
		tListener.start();
		this.verificaPrimario();
		System.err.println("Primario: "+isPrimario());
	}
	
	public void pegarConfiguracoes(){
		for (Object s : prop.keySet()) {
			String server = prop.getProperty((String)s);
			String[] configServer = server.split(" ");
			try {
				if( configServer[0].equals(InetAddress.getLocalHost().getHostAddress())){
					info.setPorta(Integer.parseInt(configServer[1]));
					info.setIp(configServer[0]);
					info.setTempo(System.currentTimeMillis());
				}
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void verificaPrimario(){
		int cont = 0;
		ServidorInfo aux = new ServidorInfo();
		for (Object s : prop.keySet()) {
			String server = prop.getProperty((String)s);
			String[] configServer = server.split(" ");
			try {
				if( !configServer[0].equals(InetAddress.getLocalHost().getHostAddress())){
					System.err.println("IP Prop: "+server);
					Socket skt = new Socket();
					skt.connect(new InetSocketAddress(configServer[0], Integer.parseInt(configServer[1])), Servidor.timeout);
					ObjectOutputStream out = new ObjectOutputStream(skt.getOutputStream());
					if(cont == 0){
						aux.setIp(configServer[0]);
						aux.setPorta(Integer.parseInt(configServer[1]));
					}
					//Tipo um para conexao e verificacao com os servidores
					out.writeObject(new Mensagem(1,new ServidorObserver(info.getIp(),info.getPorta())));
					out.flush();
					out.close();
					skt.close();
					cont++;
				}
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("Nao foi possivel conectar ao servidor "+configServer[0]);
				//e.printStackTrace();
			}
		}
		if(cont == 0)
			this.setPrimario(true);
		else{
			try {
				Socket skt = new Socket(aux.getIp(), aux.getPorta());
				ObjectOutputStream out = new ObjectOutputStream(skt.getOutputStream());
				out.writeObject(new Mensagem(6,info));
				out.flush();
				
				
				ObjectInputStream in = new ObjectInputStream(skt.getInputStream());
				List<Observer> list = (List<Observer>) in.readObject();
				
				for(Observer o : list){
					observable.register(o);
				}
				
				in.close();
				out.close();
				skt.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public ServidorListener getListener() {
		return listener;
	}

	public void setListener(ServidorListener listener) {
		this.listener = listener;
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
	
	public int gerarCoordenadas(){
		Random rand = new Random();
		return Math.abs(rand.nextInt()%650);
	}
	
	public Ponto gerarNovoPonto(){
		Random r = new Random();
		return new Ponto(gerarCoordenadas(),gerarCoordenadas(),new Color(r.nextInt()));
	}
	
	public String gerarNovoPacote(){ //mudou 1
		String pacote = "";
		for(int i = 0; i < 10; i++){
			pacote += gerarNovoPonto().toString();
		}
		return pacote;
	}
	
	public void enviarPontos(){
		Thread thread = new Thread() {
	        @Override
	        public void run() {
	            while (true) {
	                try {
	                    Thread.sleep(1 * 500);
	                } catch (InterruptedException e) {
	                    // ignore
	                }
	                observable.setChanged(true);
	                observable.notifyObservers(gerarNovoPacote());
	                //System.err.println(observable.toString());
	            }
	        };
	    };
	    thread.start();
	}
	
	public static void main(String[] args){
		Servidor servidor = new Servidor();
		System.err.println("Server ok!");
		while(!servidor.isPrimario()){
			System.err.print("");
		}
		System.err.println("Enviando Pontos!");
		servidor.enviarPontos();
	}
}
