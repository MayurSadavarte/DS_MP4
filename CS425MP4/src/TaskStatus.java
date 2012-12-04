import java.io.Serializable;
import java.util.HashMap;


 public class TaskStatus extends GenericPayload implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -220435675068678063L;
	String messageType;
	int taskId;
	
	HashMap<String, String> taskStatus;
	public void processPayload() {
		if (messageType.equals("get")) {
		   	if (MapleJuiceListener.task_map.containsKey(new Integer(taskId))) {
		   		HashMap <String, Process> temp = MapleJuiceListener.task_map.get(taskId);
		   		
		   		for (String fileName : temp.keySet()) {
		   			Process tempProcess = temp.get(fileName);
		   			//if (tempProcess.exitValue())
		   		}
		   	}
			//for (Integer task_id: MapleJuiceListener.task_map.keySet()) {
			//}
		}
	}
	

}
