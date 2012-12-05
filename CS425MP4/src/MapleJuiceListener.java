import java.io.IOException;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class MapleJuiceListener implements Runnable {

	private Machine m;
	public static int task_id;
	public static HashMap<Integer , HashMap<String, Process>> task_map; 
	public static HashMap<String, ArrayList<String>> master_task_map;
	Vector<String> freeNodeList;
	Vector<String> pendingFileList;
	
	public void start()
	{
		Thread server_thread = new Thread(this);

		server_thread.start();
	}
	public MapleJuiceListener(Machine machine) {
		task_map = new HashMap<Integer, HashMap<String, Process>>();
		master_task_map = new HashMap<String, ArrayList<String>>();
		m = machine;
		task_id = 0;
	}
	@SuppressWarnings("unchecked")
	public void processMapleCommand(String mapleExe, Vector<String> filesToProcess, String outputFilePrefix) {


		freeNodeList = new Vector<String>(m.memberList);
		pendingFileList = new Vector<String>(filesToProcess);
		boolean tasksComplete = false;
		task_id++;
		@SuppressWarnings("rawtypes")
		ArrayList[] nodeFileList = null;
		while (!tasksComplete && pendingFileList != null && pendingFileList.size() > 0) {


			if (pendingFileList.size() > 0 && freeNodeList.size() > 0) {

				nodeFileList = new ArrayList[freeNodeList.size()];
				for (int i = 0 ; i < pendingFileList.size() && i < freeNodeList.size(); i++) {				
					nodeFileList[i] = new ArrayList<String>();
				}

				int i = 0;	
				for (String fileName : pendingFileList) {

					nodeFileList[i].add(fileName);
					i = (i + 1) % freeNodeList.size();
					pendingFileList.remove(fileName);

				}
				for (int j = 0 ; ((j < freeNodeList.size()) && (nodeFileList[j].size() > 0)); j++) {
					MapleAction temp = new MapleAction();
					temp.mapleTaskId = task_id;
					temp.machineId = j + 1;
					temp.mapleExe = mapleExe;
					temp.inputFileInfo = nodeFileList[j];
					temp.outputFilePrefix = outputFilePrefix;
					MapleJuicePayload mj_payload = new MapleJuicePayload("MapleTask");
					mj_payload.setByteArray(temp);
					mj_payload.sendMapleJuicePacket(freeNodeList.elementAt(j), false);
					if (!master_task_map.containsKey(freeNodeList.elementAt(j))) {
						master_task_map.put(freeNodeList.elementAt(j), nodeFileList[j]);
					}
					freeNodeList.remove(j);
					//master_task_map.get(m.memberList.elementAt(j)).add(nodeFileList[j]);
				}
			}

			TaskStatus status = new TaskStatus();
			status.taskId = task_id;
			status.messageType = new String("get");
			//Monitor the progress on the node every 10 seconds
			tasksComplete = true;
			for (String nodeName : master_task_map.keySet() ) {
               
				MapleJuicePayload mj_payload = new MapleJuicePayload("TaskStatus");

				mj_payload.setByteArray(status);
				
				Socket sendSocket = mj_payload.sendMapleJuicePacket(nodeName, true);
				mj_payload.receiveMapleJuicePacket(sendSocket);
				TaskStatus receivedStatus = (TaskStatus) mj_payload.parseByteArray();
				//Print the obtained results
				boolean mapsCompletedOnNode = true;
				System.out.println("Status on node " + nodeName +  " :");
				for (String fileName : receivedStatus.taskStatus.keySet())
				{
					System.out.println("Filename : " + fileName + " Status : " + receivedStatus.taskStatus.get(fileName));
					if (receivedStatus.taskStatus.get(fileName).equals("In progress")) {
						mapsCompletedOnNode = false;
						tasksComplete = false;
					}else if (receivedStatus.taskStatus.get(fileName).equals("Failed")) {
						pendingFileList.add(fileName);
					}
				}
				if (mapsCompletedOnNode) {
					freeNodeList.add(nodeName);
					master_task_map.remove(nodeName);
				}
				try {
					Thread.sleep(10 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
