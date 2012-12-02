

import java.util.*;
import java.io.*;


//Payload for sending Maple Action Payload to worker nodes
public class MapleAction extends GenericPayload {
	int             mapleTaskId;
	int             machineId;
	String              mapleExe;
	ArrayList<String[]> inputFileInfo;
	String              outputFilePrefix; 
	@Override
	public void printContents()
	{
		System.out.println(mapleTaskId);
	}
	public void processMapleActionPayload() {
		//TODO : Mayur Get the exe and the files from SDFS
		ArrayList<Process> processList = new ArrayList<Process>();
		
		for (String[] fileInfo : inputFileInfo) {
			Process temp;
			try {
				temp = Runtime.getRuntime().exec(mapleExe + " " + fileInfo[0]);
				processList.add(temp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	
}