package app.ObservablePattern;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class Observable {
	
	private List<Observer> observers;
	private boolean changed;
    private final Object MUTEX= new Object();
    
    public Observable(){
    	observers = new ArrayList<>();
    	changed = false;
    }
    
	public List<Observer> getObservers() {
		return observers;
	}

	public void setObservers(List<Observer> observers) {
		this.observers = observers;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	//methods to register and unregister observers
    public void register(Observer obj){
    	synchronized (MUTEX) {
			observers.add(obj);
		}
    }
    
    public void unregister(Observer obj){
    	synchronized (MUTEX) {
			observers.remove(obj);
		}
    }
     
    //method to notify observers of change
//    public void notifyObservers(Ponto p){
    public void notifyObservers(String p){
    	List<Observer> observersLocal = null;
    	synchronized (MUTEX) {
    		if (!changed)
                return;
            observersLocal = new ArrayList<>(this.observers);
            this.changed=false;
		}
    	for (Observer obj : observersLocal) {
            try {
				obj.update(null, p);
			} 
            catch(ConnectException | SocketTimeoutException e){
				observers.remove(obj);
				System.err.println("Cliente: "+obj.toString()+" foi removido!");
			} 
            catch (IOException e) {
				e.printStackTrace();
			}
            
        }
    }

	
}
