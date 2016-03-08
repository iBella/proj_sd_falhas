import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.ConnectException;
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
        	
        	
            RmiService remoteService = (RmiService) Naming.lookup("//localhost:9999/RmiService");
            RmiClient client = new RmiClient();
            remoteService.addObserver(client);
            thread.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static Thread thread = new Thread() {
        @Override
        public void run() {
            while (true) {
                try {
                    testarConexao();
                    Thread.sleep(5*1000);
                } catch (Exception e) {
                    System.out.println("Pegou!!");
                }
            }
        };
    };
    
    public static void testarConexao(){
    	try {
    	       InetAddress address = InetAddress.getByName("localhost");
    	       System.out.println("Name: " + address.getHostName());
    	       System.out.println("Addr: " + address.getHostAddress());
    	       System.out.println("Reach: " + address.isReachable(3000));
    	     }
    	     catch (UnknownHostException e) {
    	       System.err.println("Unable to lookup web.mit.edu");
    	     }
    	     catch (IOException e) {
    	       System.err.println("Unable to reach web.mit.edu");
    	     }
    }
    
    
    @Override
    public void update(Object o, String p)
            throws RemoteException {
    	
    	
    	System.out.println("client");
    	String[] ponto = p.toString().split(" ");
    	Color c = new Color(Integer.parseInt(ponto[2]));
    	frame.addPonto(new Ponto(Integer.parseInt(ponto[0]), Integer.parseInt(ponto[1]), c));
        System.out.println("got message:" + p.toString());
    }

	@Override
	public boolean isAlive() throws RemoteException, ConnectException {
		// TODO Auto-generated method stub
		return false;
	}
}