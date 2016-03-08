import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteObserver extends Remote {

    void update(Observable o, Ponto p) throws RemoteException;

}
