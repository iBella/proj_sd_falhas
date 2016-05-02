package app.servidor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Properties;

import app.Mensagem;
import app.ObservablePattern.ClienteObserver;
import app.ObservablePattern.Observable;
import app.ObservablePattern.Observer;
import app.ObservablePattern.ServidorObservable;
import app.ObservablePattern.ServidorObserver;
import app.cliente.Cliente;
import server.GenericConsumer;
import server.GenericResource;

public class ServidorConsumer<S extends Socket> extends GenericConsumer<S> {
	
	private Observable observable;
	private ServidorObservable servidores;
	private Properties prop;
	
	public ServidorConsumer(GenericResource<S> re, Observable o, ServidorObservable s) {
		super(re);
		this.observable = o;
		this.servidores = s;
		this.prop = getNewProp();
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
	
	@Override
	protected void doSomething(S str) {
		try{
			// TODO Auto-generated method stub
			ObjectInputStream in = new ObjectInputStream(str.getInputStream());
			
			Mensagem msg = (Mensagem) in.readObject();
			
			switch (msg.getTipo()) {
			case 0:
				observable.register((Observer)msg.getObjeto());
				System.err.println("Cliente: "+((ClienteObserver)msg.getObjeto()).getIp()+" conectou ao Servidor.");
				if(Servidor.primario == true){
					System.err.println("Notifica servidores!");
					servidores.setChanged(true);
					servidores.notifyObservers(0,(Observer)msg.getObjeto());
				}
				
				break;
			case 1:
				servidores.register((ServidorObserver)msg.getObjeto());
				System.err.println("O servidor "+((ServidorObserver)msg.getObjeto()).getIp()+" foi adicionado na lista.");
				if(Servidor.primario == true){
					ServidorObserver s = (ServidorObserver) msg.getObjeto();
					Socket skt = new Socket(s.getIp(),s.getPorta());
					ObjectOutputStream out = new ObjectOutputStream(skt.getOutputStream());
					out.writeObject(new Mensagem(3,new ServidorObserver(Servidor.info.getIp(), Servidor.info.getPorta())));
					out.flush();
					out.close();
					skt.close();
				}
				break;
			case 2:
				if(Servidor.primario == true){
					ClienteObserver c = (ClienteObserver) msg.getObjeto();
					Socket skt = new Socket(c.getIp(),Cliente.PORTA);
					ObjectOutputStream out = new ObjectOutputStream(skt.getOutputStream());
					out.writeObject(new Mensagem(1,new ServidorObserver(Servidor.info.getIp(), Servidor.info.getPorta())));
					out.flush();
					out.close();
					skt.close();
				}
				break;
			case 3:
				ServidorObserver s = (ServidorObserver) msg.getObjeto();
				Servidor.servidorPrimario = s;
				Thread testeConexao = new Thread() {
					@Override
					public void run(){
						while (!Servidor.primario) {
							try {
								Thread.sleep(5000);
								if(!Servidor.info.getIp().equals(Servidor.servidorPrimario.getIp())){
									Socket skt = new Socket();
									skt.connect(new InetSocketAddress(Servidor.servidorPrimario.getIp(), Servidor.servidorPrimario.getPorta()), Servidor.timeout);
									System.err.println("Conectei");
									ObjectOutputStream out = new ObjectOutputStream(skt.getOutputStream());
									out.writeObject(new Mensagem(-1));
									out.flush();
									skt.close();
								}
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								System.err.println("O servidor primario esta off!");
								ServidorInfo proxPrimario = new ServidorInfo(Servidor.info);
								System.err.println("Meu tempo: "+proxPrimario.toString());
								for (Object s : prop.keySet()) {
									String server = prop.getProperty((String)s);
									String[] configServer = server.split(" ");
									try{
										if( !configServer[0].equals(Servidor.info.getIp())){
											System.err.println("IP Prop: "+server);
											Socket skt = new Socket();
											skt.connect(new InetSocketAddress(configServer[0], Integer.parseInt(configServer[1])), Servidor.timeout);
											ObjectOutputStream out = new ObjectOutputStream(skt.getOutputStream());
											out.writeObject(new Mensagem(4));
											out.flush();
											
											ObjectInputStream in = new ObjectInputStream(skt.getInputStream());
											ServidorInfo resposta = (ServidorInfo) in.readObject();
											System.err.println("Resposta : "+resposta.toString());
											if(proxPrimario.getTempo() >= resposta.getTempo()){
												proxPrimario = new ServidorInfo(resposta);
											}
											
											in.close();
											out.close();
											skt.close();
										}
									} catch(IOException f){
										System.err.println("Servidor secundario off - proximoPrimario");
									} catch (ClassNotFoundException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
								if(Servidor.info.getIp().equals(proxPrimario.getIp())){
									Servidor.servidorPrimario = null;
									Servidor.primario = true;
									System.err.println("Sou primario");
								}else{
									Servidor.servidorPrimario.setIp(proxPrimario.getIp());
									Servidor.servidorPrimario.setPorta(proxPrimario.getPorta());
									System.err.println("O servidor primario eh "+proxPrimario.toString());
								}
							}
							
						}
					}
				};
				testeConexao.start();
				break;
			case 4:
				ObjectOutputStream out = new ObjectOutputStream(str.getOutputStream());
				out.writeObject(new ServidorInfo(Servidor.info));
				out.flush();
				out.close();
				break;
			case 5:
				observable.unregister((Observer)msg.getObjeto());
				System.err.println("Cliente: "+((ClienteObserver)msg.getObjeto()).getIp()+" saiu do Servidor.");
				break;
			case 6:
				ObjectOutputStream o = new ObjectOutputStream(str.getOutputStream());
				System.err.println(observable.getObservers().toString());
				o.writeObject(observable.getObservers());
				o.flush();
				o.close();
				break;
			default:
				break;
			}
			
			str.close();
		}catch (Exception e){
			e.printStackTrace();
			
		}
		
	}

	

}
