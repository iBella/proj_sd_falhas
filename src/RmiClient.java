import java.awt.Color;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
public class RmiClient extends UnicastRemoteObject implements RemoteObserver {
    
	private static final long serialVersionUID = 933249689699267252L;
	//private static final long serialVersionUID = 1L;
    private static TelaApp frame;
	
	protected RmiClient() throws RemoteException {
        super();
        frame = new TelaApp();
        Thread t = new Thread(frame);
        t.start();
    }
    
    public static void main(String[] args) {
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new RMISecurityManager());
        try {
        	
        	
            RmiService remoteService = (RmiService) Naming
                    .lookup("//200.239.179.185:9999/RmiService");
            RmiClient client = new RmiClient();
            remoteService.addObserver(client);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void update(Object observable, Object updateMsg)
            throws RemoteException {
    	String[] ponto = updateMsg.toString().split(" ");
    	Color c = new Color(Integer.parseInt(ponto[2]));
    	frame.addPonto(new Ponto(Integer.parseInt(ponto[0]), Integer.parseInt(ponto[1]), c));
        System.out.println("got message:" + updateMsg.toString());
    }
}