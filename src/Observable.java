import java.util.ArrayList;

public class Observable {
	private ArrayList<Observer> observers;
	private boolean changed;
	
	
	public Observable(){
		observers = new ArrayList<>();
		setChanged(false);
	}
	
	public void addObserver(Observer o) {
		observers.add(o);
	}
	
	public void deleteObserver(Observer o) {
		observers.remove(o);
	}
	
	public void deleteObservers() {
		observers.clear();
	}
	
	public void notifyObservers(Ponto p) {
		if(isChanged()){
			setChanged(false);
			for (Observer observer : observers) {
				observer.update(this, p);
			}
		}
		
	}
	
	public boolean isChanged() {
		return changed;
	}
	public void setChanged(boolean changed) {
		this.changed = changed;
	}
	
}
