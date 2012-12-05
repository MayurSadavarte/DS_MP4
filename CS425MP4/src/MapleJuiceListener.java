import java.io.IOException;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MapleJuiceListener implements Runnable {

	private Machine m;
	public static int task_id;
	public static HashMap<Integer , HashMap<String, Process>> task_map; 
	public void start()
	{
		Thread server_thread = new Thread(this);
		
		server_thread.start();
	}
	public MapleJuiceListener(Machine machine) {
		task_map = new HashMap<Integer, HashMap<String, Process>>();
		m = machine;
		task_id = 0;
	}
	
	@SuppressWarnings("unchecked")
	public void processMapleCommand(String mapleExe, Vector<String> filesToProcess, String outputFilePrefix) {
		@SuppressWarnings("rawtypes")		
		ArrayList[] nodeFileList = new ArrayList[m.memberList.size()];
		for (int i = 0 ; i < m.memberList.size(); i++) {
			nodeFileList[i] = new ArrayList<String>();

		}
		int i = 0;
		task_id++;
		for (String fileName : filesToProcess) {
			nodeFileList[i].add(fileName);
			i = (i + 1) % m.memberList.size();

		}
		for (int j = 0 ; ((j < m.memberList.size()) && (nodeFileList[j].size()>0)); j++) {
			MapleAction temp = new MapleAction();
			temp.mapleTaskId = task_id;
			temp.machineId = j + 1;
			temp.mapleExe = mapleExe;
			temp.inputFileInfo = nodeFileList[j];
			temp.outputFilePrefix = outputFilePrefix;
			MapleJuicePayload mj_payload = new MapleJuicePayload("MapleTask");
			mj_payload.setByteArray(temp);
			mj_payload.sendMapleJuicePacket(m.memberList.elementAt(j));
		}
	}


	
	public void processJuiceCommand(String juiceExe, int num_juices, String outputsdfsFileName, String sdfsFilePrefix) {
		
		//TODO : This will borrow heavily from the way maple tasks are assigned. 
		
		
		ArrayList<ArrayList<String>> nodeFileList = new ArrayList<ArrayList<String>>();
		for (int i = 0 ; i < num_juices; i++) {
			nodeFileList.add(i, new ArrayList<String>());

		}
		
		int i = 0;
		task_id++;
		
		Iterator<String> iter = m.file_node_map.keySet().iterator();
		
		//I should only find files with names of the form : prefix_inter_key. Search for _inter_
		Pattern pattern = Pattern.compile(sdfsFilePrefix + "_inter_" + "[.]+");
		while(iter.hasNext()) {
			Vector<String> filesOfNode = m.file_node_map.get(iter.next());
			
			for(String file : filesOfNode) {
				Matcher matcher = pattern.matcher(file);
				
				if(matcher.find()) {
					nodeFileList.get(i).add(file);
					i = (i + 1) % num_juices;
					matcher.reset();
					continue;
				}				
			}			
		}
		
		
		for (int j = 0 ; ((j < m.memberList.size()) && ( j < num_juices)); j++) {
			JuiceAction juiceAction = new JuiceAction();
			juiceAction.juiceTaskId= task_id;
			juiceAction.machineId = j + 1;
			juiceAction.juiceExe = juiceExe;
			juiceAction.juiceInputFileList = nodeFileList.get(j);
			juiceAction.juiceOutputFile = outputsdfsFileName;
			MapleJuicePayload mj_payload = new MapleJuicePayload("JuiceTask");
			mj_payload.setByteArray(juiceAction);
			mj_payload.sendMapleJuicePacket(m.memberList.elementAt(j));
		} 
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



				MapleJuiceThread mj_thread = new MapleJuiceThread(sock, m);
				mj_thread.start();
				// sendfile



			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}

}
