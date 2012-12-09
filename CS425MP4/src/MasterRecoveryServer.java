import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class MasterRecoveryServer implements Runnable {
	Machine machine=null;
	
	public MasterRecoveryServer(Machine m) {
		machine = m;
	}
	
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
			servsock = new ServerSocket(Machine.MASTER_RECOVERY_PORT);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while(true){
		
			try{
			      System.out.println("Waiting...");
	
			      Socket sock = servsock.accept();
			      System.out.println("Accepted connection : " + sock);  
			      
			    
			      MasterRecoveryPacket recPacket = new MasterRecoveryPacket(machine);
			      recPacket.sendPacket(sock);
			      
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
