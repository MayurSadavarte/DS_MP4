import java.io.*;
import java.net.*;

public class MapleJuiceClient implements Runnable {
	private String target;
	private MapleJuicePayload payload;
	
	public MapleJuiceClient(MapleJuicePayload payload, String target){
		this.target = target;
		this.payload = payload;
	}
	
	public void send() {
		Thread thread = new Thread(this);
		thread.start();
		return;
	}
	
	public void run(){
		
	    Socket sock;
	    String myName=null;

	    try {
			myName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		};
	    
		try {
			sock = new Socket(target, Machine.MAPLE_JUICE_PORT);
			System.out.println("Connecting...");
			// receive file
			
			try {
				WriteLog.writelog(myName, "Sending mapleJuicePayload to : " + target);
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
						
			ObjectOutputStream oos = null;
			try {
				oos = new ObjectOutputStream(sock.getOutputStream());
				oos.writeObject(this.payload);
				//oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
			finally {
				if(oos != null) oos.close();
				if (sock != null) sock.close(); 
			}
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    		
	}
}
