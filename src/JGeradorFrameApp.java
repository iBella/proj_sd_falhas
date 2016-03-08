import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Panel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
public class JGeradorFrameApp extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		JGeradorFrameApp frame = new JGeradorFrameApp();
		frame.setVisible(true);
	}
	
	/**
	 * Create the frame.
	 */
	public JGeradorFrameApp() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 650);
		
		setTitle("JANELA");
		
		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		Graphics g = contentPane.getGraphics();
		contentPane.setBackground(Color.WHITE);

		contentPane.paint(g);
		contentPane.updateUI();
	}
	
	public void desenhaPonto(Ponto p){
		Graphics g = contentPane.getGraphics();

		Ponto ponto = new Ponto(342, 342, Color.BLACK);
		
		g.setColor(ponto.getColor());
		g.fillOval(ponto.getCoordX(), ponto.getCoordY(), 7, 7);
		
		contentPane.paint(g);
		contentPane.updateUI();
	}
	
	public void paint(Graphics g) {  
		super.paint(g);
		//x = 0/640 e y = 22/640
		Ponto ponto = new Ponto(342, 342, Color.BLACK);
		
		g.setColor(ponto.getColor());
		g.fillOval(ponto.getCoordX(), ponto.getCoordY(), 7, 7);
    } 
}