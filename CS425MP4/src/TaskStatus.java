import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;


 public class TaskStatus extends GenericPayload implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -220435675068678063L;
	String messageType;
	int taskId;
	
	HashMap<String, String> taskStatus;
	public void processPayload(Socket socket) {
		if (messageType.equals("get")) {
			try {
				WriteLog.writelog(Machine.stName, "Received task status request for task ID " + taskId);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			TaskStatus response = new TaskStatus();
		   	response.messageType = new String("response");
		   	response.taskStatus = new HashMap<String, String>();
			if (MapleJuiceListener.task_map.containsKey(new Integer(taskId))) {
		   		HashMap <String, Process> temp = MapleJuiceListener.task_map.get(taskId);
		   		String taskState = null;
		   		for (String fileName : temp.keySet()) {
		   			Process tempProcess = temp.get(fileName);		   			
		   			try {
		   			    int val = tempProcess.exitValue();
		   			    if (val == 0) {
		   			    	taskState = new String("Success");
		   			    }else {
		   			    	taskState = new String("Failed");
		   			    }
		   				response.taskStatus.put(fileName, taskState);
		   			}catch (IllegalThreadStateException a){
		   				response.taskStatus.put(fileName, "In progress");
		   			}
		   		}
		   	
			}
			MapleJuicePayload statusResponse = new MapleJuicePayload("TaskStatus"); 
			statusResponse.setByteArray(response);
			statusResponse.sendMapleJuicePacket(socket);
			//for (Integer task_id: MapleJuiceListener.task_map.keySet()) {
			//}
		}
	}
	

}
