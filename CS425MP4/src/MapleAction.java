

import java.util.*;

//Payload for sending Maple Action Payload to worker nodes
public class MapleAction extends GenericPayload{
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
	
	
}