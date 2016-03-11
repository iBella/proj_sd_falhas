package app.servidor;

import java.awt.Color;
import java.util.Random;

import app.ObservablePattern.Observable;
import app.janelas.Ponto;

public class Servidor {
	
	private ServidorListener listener;
	private Observable observable;
	
	public Servidor(int porta){
		super();
		this.observable = new Observable();
		this.listener = new ServidorListener(porta, observable);
		Thread tListener = new Thread(listener);
		tListener.start();
	}
	
	public ServidorListener getListener() {
		return listener;
	}

	public void setListener(ServidorListener listener) {
		this.listener = listener;
	}

	public int gerarCoordenadas(){
		Random rand = new Random();
		return Math.abs(rand.nextInt()%650);
	}
	
	public Ponto gerarNovoPonto(){
		Random r = new Random();
		return new Ponto(gerarCoordenadas(),gerarCoordenadas(),new Color(r.nextInt()));
	}
	
	///////
	public String gerarNovoPacote(){
		String pacote = "";
		for(int i = 0; i < 10; i++){
			pacote += gerarNovoPonto().toString();
		}
		return pacote;
	}
	
	public void enviarPacotes(){
		Thread thread = new Thread() {
	        @Override
	        public void run() {
	            while (true) {
	                try {
	                    Thread.sleep(1 * 1000);
	                } 
	                catch (InterruptedException e) {
	                    // ignore
	                }
	                observable.setChanged(true);
	                //observable.notifyObservers(gerarNovoPonto());
	                observable.notifyObservers(gerarNovoPacote());
	            }
	        };
	    };
	    thread.start();
	}
	
	public static void main(String[] args){
		Servidor servidor = new Servidor(6000);
		servidor.enviarPacotes();
	}
}
