package app.ObservablePattern;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

public interface Observer {
	
	public void update(Object arg0, Object arg1) throws ConnectException, UnknownHostException, IOException;
	
}
