import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;


//Payload for sending Maple Action Payload to worker nodes
public class JuiceAction extends GenericPayload implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	int                 juiceTaskId;
	int                 machineId;
	String              juiceExe;
	ArrayList<String> 	juiceInputFileList;
	String              juiceOutputFile; 
	//@Override
	/*public MapleAction(Machine machine) {
		m = machine;
	}*/
	@Override
	public void printContents()
	{
		System.out.println(juiceTaskId);
	}
	public void processJuiceActionPayload(Machine machine) {
		//TODO : Mayur Get the exe and the files from SDFS
		if (!machine.myFileList.contains(juiceExe)) {
			machine.FileReplicator.sendSDFSGetMessage(juiceExe);
		}
		for (String juiceInputFile : juiceInputFileList) {
			if (!machine.myFileList.contains(juiceInputFile)) {
				machine.FileReplicator.sendSDFSGetMessage(juiceInputFile);
			}
			//machine.FileReplicator.sendSDFSGetMessage(fileInfo);
		}
		//TODO : Synchronization
		HashMap<String, Process> processList = new HashMap<String, Process>();

		for (String juiceInputFile : juiceInputFileList) {
			Process juiceProcess;
			try {
				juiceProcess = Runtime.getRuntime().exec("java -jar" + juiceExe + " " + juiceInputFile);
				processList.put(juiceInputFile, juiceProcess);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		//TODO : the task_map will contain the IDs of the maple task they executed. Now we don't need them. 
		//Needs to be removed
		MapleJuiceListener.task_map.put(new Integer(juiceTaskId), new HashMap<String, Process>(processList));
		int index = 0;
		for (String juiceInputFile  : processList.keySet()) {
			try {
				Process process = processList.get(juiceInputFile);
				index++;
				process.waitFor();
				int result = process.exitValue();
				WriteLog.writelog(machine.myName, "Juice Task  " + juiceInputFile + "exited with code " + result);
				
				if(result == 0) { //If process exited successfully
					
				}else {
					//Do nothing.
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String path = "./bin/";
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		Pattern pattern = Pattern.compile("juice_inter_" + juiceTaskId);
		
		for(File file : listOfFiles) {
			Matcher matcher = pattern.matcher(file.getName());						
			if(matcher.find()) {
				
				try {
					WriteLog.writelog(machine.myName, "Sending PUT msg for file " + file.getName());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				machine.FileReplicator.sendSDFSPutMessage(file.getName(), juiceOutputFile, true);
				matcher.reset();
				continue;
			}
		}
	}
}