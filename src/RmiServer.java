import java.awt.Color;
import java.io.Serializable;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class RmiServer extends Observable implements RmiService {

	private class WrappedObserver implements Observer, Serializable {

        //private static final long serialVersionUID = 1L;

        /**
		 * 
		 */
		private static final long serialVersionUID = 2594131831531445736L;
		private RemoteObserver ro = null;

        public WrappedObserver(RemoteObserver ro) {
            this.ro = ro;
        }

        @Override
        public void update(Observable o, String p) {
            try {
            	if(ro.isAlive()==false){
            		System.out.println("wrapper antes");
                    ro.update(o.toString(), p);
                    System.out.println("wrapper depois");
            	}
            	
            } catch (RemoteException e) {
            	
            	
            	
                System.out.println("Remote exception removing observer:" + this);
                o.deleteObserver(this);
                //e.printStackTrace();
            }
        }

    }

    @Override
    public void addObserver(RemoteObserver o) throws RemoteException {
    	
        WrappedObserver mo = new WrappedObserver(o);
        addObserver(mo);
        System.out.println("Added observer:" + mo);
    }

    Thread thread = new Thread() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1 * 500);
                } catch (InterruptedException e) {
                    // ignore
                }
                setChanged(true);
                //System.out.println("entrouNotify");
                notifyObservers((new Ponto(gerarCoor(),gerarCoor(),Color.BLACK)).toString());
                //System.out.println("saiuNotify");
            }
        };
    };

    public RmiServer() {
        thread.start();
    }
    
    public int gerarCoor(){
		Random rand = new Random();
    	return Math.abs(rand.nextInt()%650);
    }
    
    //private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new RMISecurityManager());
        try {
            Registry rmiRegistry = LocateRegistry.createRegistry(9999);
            RmiService rmiService = (RmiService) UnicastRemoteObject.exportObject(new RmiServer(), 9999);
            rmiRegistry.bind("RmiService", rmiService);
            System.out.println("Server OK!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}