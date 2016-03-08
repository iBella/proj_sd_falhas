import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteObserver extends Remote {

    void update(Object o, String p) throws RemoteException;

}
