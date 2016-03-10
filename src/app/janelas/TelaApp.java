package app.janelas;

import javax.swing.JFrame;


public class TelaApp extends JFrame implements Runnable{
	private static final long serialVersionUID = 1L;
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
	
	public void addPonto(Ponto p){
		ponto.updateGraphics(p);
	}

	@Override
	public void run() {
		setVisible(true);
	}
}