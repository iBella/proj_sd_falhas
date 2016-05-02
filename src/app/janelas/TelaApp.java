package app.janelas;

import java.awt.Color;
import java.util.LinkedList;

import javax.swing.JFrame;


public class TelaApp extends JFrame implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 603601293727369475L;
	private Ponto ponto = new Ponto();

	/**
	 * Create the frame.
	 */
	public TelaApp() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 650, 650);
		setTitle("Janela de Pontos");
		
		setContentPane(ponto);
	}
	public void addPacote(LinkedList<String> p){  //mudou 2
		LinkedList<Ponto> pacote = new LinkedList<>();
		for(String ponto : p){
			String[] parte = ponto.toString().split(" ");
	     	Color c = new Color(Integer.parseInt(parte[2]));
	     	pacote.add(new Ponto(Integer.parseInt(parte[0]), Integer.parseInt(parte[1]), c));
		}
			
		ponto.updatePacote(pacote);
	}

	@Override
	public void run() {
		setVisible(true);
	}
}