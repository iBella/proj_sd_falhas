import java.rmi.ConnectException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteObserver extends Remote {

    void update(Object o, String p) throws RemoteException;
    
    boolean isAlive() throws RemoteException, ConnectException;

}
