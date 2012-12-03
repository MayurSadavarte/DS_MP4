

import java.util.*;
import java.io.*;


//Payload for sending Maple Action Payload to worker nodes
public class MapleAction extends GenericPayload implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int             mapleTaskId;
	int             machineId;
	String              mapleExe;
	ArrayList<String> inputFileInfo;
	String              outputFilePrefix; 
	//@Override
	/*public MapleAction(Machine machine) {
		m = machine;
	}*/
	@Override
	public void printContents()
	{
		System.out.println(mapleTaskId);
	}
	public void processMapleActionPayload(Machine machine) {
		//TODO : Mayur Get the exe and the files from SDFS
		machine.FileReplicator.sendSDFSGetMessage(mapleExe);
		for (String fileInfo : inputFileInfo) {
			machine.FileReplicator.sendSDFSGetMessage(fileInfo);
		}
		//TODO : Synchronization
		ArrayList<Process> processList = new ArrayList<Process>();

		for (String fileInfo : inputFileInfo) {
			Process temp;
			try {
				temp = Runtime.getRuntime().exec("java " + mapleExe + " " + fileInfo);
				processList.add(temp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		int index = 0;
		for (Process temp : processList) {
			try {
				index++;
				temp.waitFor();
				int result = temp.exitValue();
				WriteLog.writelog(machine.myName, "Maple Task  " + index + "exited with code " + result);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}


}