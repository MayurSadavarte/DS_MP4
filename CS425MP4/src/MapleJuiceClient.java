import java.io.*;
import java.net.*;

public class MapleJuiceClient {
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
				//responseRequired = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Connecting...");
		}
	}
	public MapleJuiceClient(Socket s){

		sock = s;
		ResponseRequired = false;

	}

	//public void send() {
		//Thread thread = new Thread(this);
		//thread.start();
		//return;
		
	//}

	public void send(){

		//Socket sock;

		//boolean responseRequired = true;

		// receive file


		try {
			WriteLog.writelog(myName, "Sending mapleJuicePayload to : " + target);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("in MapleJuiceClient run - responseRequired - "+this.ResponseRequired);

		ObjectOutputStream oos = null;
		OutputStream som = null;
		try {
			som = sock.getOutputStream();
			oos = new ObjectOutputStream(som);
			System.out.println(payload.toString());
			oos.writeObject(this.payload);
			//oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		finally {
			if (!ResponseRequired) {
				if(oos != null)
					try {
						oos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				try {
					som.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (sock != null)
					System.out.println("Closing socket, ResponseRequired = " + ResponseRequired);
				try {

					sock.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}

		}


	}
}

