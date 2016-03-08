
import java.util.ArrayList;

public class Observable{

	private ArrayList<Observer> observers;
	private boolean changed;
	private final Object MUTEX = new Object();
	
	
	public Observable(){
		observers = new ArrayList<>();
		setChanged(false);
	}
	
	public void addObserver(Observer o) {
		synchronized (MUTEX) {
			observers.add(o);
		}
		
	}
	
	public void deleteObserver(Observer o) {
		synchronized (MUTEX) {
			observers.remove(o);
		}
		
	}
	
	public void deleteObservers() {
		synchronized (MUTEX) {
			observers.clear();
		}
		
	}
	
	public void notifyObservers(String p) {
		
		synchronized (MUTEX) {
			if(isChanged()){
				setChanged(false);
				for (Observer observer : observers) {
					System.out.println("antes " + observer.toString());
					observer.update(this, p);
					System.out.println("depois " + observer.toString());
				}
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
