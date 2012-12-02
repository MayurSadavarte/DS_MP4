import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class MapleJuiceListener implements Runnable {

	public void start()
	{
		Thread server_thread = new Thread(this);
		server_thread.start();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ServerSocket servsock=null;
		try {
			servsock = new ServerSocket(Machine.MAPLE_JUICE_PORT);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while(true){
		
			try{
				
			    
			      System.out.println("Waiting...");
	
			      Socket sock = servsock.accept();
			      System.out.println("Accepted connection : " + sock);
			      
			      
			      
			      MapleJuiceThread mj_thread = new MapleJuiceThread(sock);
			      mj_thread.start();
			      // sendfile
			     
			      
			    
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		
	}

}
