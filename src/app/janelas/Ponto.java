package app.janelas;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.sun.javafx.collections.MappingChange.Map;
public class Ponto extends JPanel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4313519667361553991L;
	private int coordX;
	private int coordY;
	private Color color;
	
	private LinkedList<Ponto> lista = new LinkedList<>();

//	private HashMap<Ponto, Integer> listaNova = new HashMap<Ponto, Integer>();
//	private static int qtPonto = 0;
	
	
	public Ponto(){
		super();
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new BorderLayout(0, 0));
		setBackground(Color.WHITE);
	}
	
	public Ponto(int coordX, int coordY, Color color) {
		super();
		this.coordX = coordX;
		this.coordY = coordY;
		this.color = color;
	}
	
	public int getCoordX() {
		return coordX;
	}
	public void setCoordX(int x) {
		this.coordX = x;
	}
	public int getCoordY() {
		return coordY;
	}
	public void setCoordY(int y) {
		this.coordY = y;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(Ponto p: lista){
	        Graphics2D g2 = (Graphics2D) g.create();
	        g2 = (Graphics2D) g;
	        g2.setColor(p.getColor());
	        g2.fillOval(p.getCoordX(), p.getCoordY(), 7, 7); 
        }
    }

    public void updatePacote(LinkedList<Ponto> pacote) { //mudou 3
    	for(Ponto p: pacote){
    		//if(!listaNova.containsKey(p)){
    			lista.add(p);
    			//listaNova.put(p, qtPonto);
    			//qtPonto++;
    			//System.err.println("Quantidade de pontos inseridos: "+qtPonto);
    		//}
    	}
        repaint();
    }

	@Override
	public String toString() {
		return coordX+" "+coordY+" "+color.getRGB()+";"; //mudou 4
	}
    
    
}