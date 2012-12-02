import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class MapleJuiceThread implements Runnable {
    Socket sock;
	public MapleJuiceThread(Socket s) {
		// TODO Auto-generated constructor stub
		sock = s;
	}
	public void start()
	{
		Thread thread  = new Thread(this);
		thread.start();
	}

	public void run(){
		  try {
			  ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
			  
				MapleJuicePayload mjPayload  = (MapleJuicePayload)(ois.readObject());
				mjPayload.parseByteArray();
				//ois.close();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  
			
			
			try {
				String myName = InetAddress.getLocalHost().getHostName();
				//WriteLog.writelog(myName, "sourceFN: "+sourceFN);
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		  
	}
	

}
