import java.io.IOException;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class MapleJuiceListener implements Runnable {

	private Machine m;
	public void start()
	{
		Thread server_thread = new Thread(this);
		server_thread.start();
	}
	public MapleJuiceListener(Machine machine) {
		m = machine;
	}
	@SuppressWarnings("unchecked")
	public void processMapleCommand(String mapleExe, Vector<String> filesToProcess, String outputFilePrefix) {
		@SuppressWarnings("rawtypes")
		ArrayList[] nodeFileList = new ArrayList[m.memberList.size()];
		for (int i = 0 ; i < m.memberList.size(); i++) {
			nodeFileList[i] = new ArrayList<String>();

		}
		int i = 0;
		for (String fileName : filesToProcess) {
			nodeFileList[i].add(fileName);
			i = (i + 1) % m.memberList.size();

		}


	}


	public void processJuiceCommand(String sdfsExe, int num_reducers, String outputsdfsFileName, String outputFilePrefix) {
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ServerSocket servsock = null;
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
