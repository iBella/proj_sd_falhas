package app.ObservablePattern;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class Observable {
	
	private List<Observer> observers;
	private boolean changed;
	private ServidorObservable servidores;
    private final Object MUTEX= new Object();
    
    public Observable( ServidorObservable s){
    	observers = new ArrayList<>();
    	changed = false;
    	this.servidores = s;
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
    public void notifyObservers(Object p){
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
			} catch(ConnectException | SocketTimeoutException e){
				observers.remove(obj);
				servidores.setChanged(true);
				servidores.notifyObservers(5, obj);
				System.err.println("O Cliente: "+obj.toString()+" foi removido!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

	@Override
	public String toString() {
		return "Observable [observers=" + observers.toString() + "]";
	}

	
}
