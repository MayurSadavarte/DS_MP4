import java.io.*;
import java.net.*;

public class MapleJuiceClient implements Runnable {
	private String target;
	private MapleJuicePayload payload;
	public Socket sock;
	boolean ResponseRequired;
	String myName;

	public MapleJuiceClient(MapleJuicePayload payload, String target, boolean responseRequired){
		this.target = target;
		this.payload = payload;
		sock = null;
		ResponseRequired = responseRequired;
		if (sock == null) {
			try {
				myName = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			};

			try {
				sock = new Socket(target, Machine.MAPLE_JUICE_PORT);
				responseRequired = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Connecting...");
		}
	}
	public MapleJuiceClient(Socket s){

		sock = s;
		ResponseRequired = true;

	}

	public void send() {
		Thread thread = new Thread(this);
		thread.start();
		return;
	}

	public void run(){

		//Socket sock;
		
        //boolean responseRequired = true;

		// receive file


		try {
			WriteLog.writelog(myName, "Sending mapleJuicePayload to : " + target);
		} catch (IOException e1) {
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
			if(oos != null)
				try {
					oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			if (sock != null && !ResponseRequired)
				try {
					sock.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
		}




	}
}

