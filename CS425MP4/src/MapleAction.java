

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;


//Payload for sending Maple Action Payload to worker nodes
public class MapleAction extends GenericPayload implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int                 mapleTaskId;
	int                 machineId;
	String              mapleExe;
	ArrayList<String>   inputFileInfo;
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
		HashMap<String, Process> temp2 = new HashMap<String, Process>();

		for (String mapleInputFile : inputFileInfo) {
			temp2.put(mapleInputFile, (Process)null);
		}
		synchronized (MapleJuiceListener.task_map) {
			MapleJuiceListener.task_map.put(new Integer(mapleTaskId), new HashMap<String, Process>(temp2));
		}
		try {
			WriteLog.writelog(machine.myName, "Received Maple Task Payload");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//TODO : Mayur Get the exe and the files from SDFS
		if (!machine.myFileList.contains(mapleExe) /*&& ! (new File(mapleExe).isFile())*/) {
			machine.FileReplicator.sendSDFSGetMessage(mapleExe);
		}
		for (String fileInfo : inputFileInfo) {
			if (!machine.myFileList.contains(fileInfo)/* && ! (new File(fileInfo).isFile())*/) {
				machine.FileReplicator.sendSDFSGetMessage(fileInfo);
			}
			//machine.FileReplicator.sendSDFSGetMessage(fileInfo);
		}
		//TODO : Synchronization
		HashMap<String, Process> processList = new HashMap<String, Process>();
		int i = 0;
		for (String fileInfo : inputFileInfo) {
			Process temp;
			try {
				i ++;

				temp = Runtime.getRuntime().exec("java -jar " + mapleExe + " " + fileInfo + " " + mapleTaskId);
				//MapleJuiceListener.task_map.get(mapleTaskId).remove(fileInfo);
				synchronized (MapleJuiceListener.task_map) {
					MapleJuiceListener.task_map.get(mapleTaskId).put(fileInfo, temp);
				}

				processList.put(fileInfo, temp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if ((i % 50) == 0) {
				for (String fileName  : processList.keySet()) {
					try {
						Process temp1 = processList.get(fileName);
						//index++;
						temp1.waitFor();

						int result = temp1.exitValue();
						WriteLog.writelog(machine.myName, "Maple Task  " + fileName + " exited with code " + result);

						if(result == 0) {
							//Process exited successfully.
							System.out.println("Process completed successfully ");
							//We need to iterate over the files in the directory to find the list of files of the form
							//prefix_inter_k, and


						} else {
							//Process exited abnormally.
							//Do nothing. All the required exit data is available in the process structure. 
						}				
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				processList.clear();
			}
		}
		MapleJuiceListener.task_map.put(new Integer(mapleTaskId), new HashMap<String, Process>(processList));
		//int index = 0;
		for (String fileName  : processList.keySet()) {
			try {
				Process temp = processList.get(fileName);
				//index++;
				temp.waitFor();
				int result = temp.exitValue();
				WriteLog.writelog(machine.myName, "Maple Task  " + fileName + " exited with code " + result);

				if(result == 0) {
					//Process exited successfully.
					System.out.println("Process completed successfully ");
					//We need to iterate over the files in the directory to find the list of files of the form
					//prefix_inter_k, and


				} else {
					//Process exited abnormally.
					//Do nothing. All the required exit data is available in the process structure. 
				}				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		//Now it is time to put the outputs of the juices over SDFS.
		String path = "./";
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		Pattern pattern = Pattern.compile("inter_" + mapleTaskId);

		for(File file : listOfFiles) {
			Matcher matcher = pattern.matcher(file.getName());						
			if(matcher.find()) {
				String fileName = file.getName();
				String[] tokens = fileName.split("_");
				String newFileName = outputFilePrefix + "_" + tokens[tokens.length - 1];

				try {
					WriteLog.writelog(machine.myName, "Sending PUT msg for file " + file.getName());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				machine.FileReplicator.sendSDFSPutMessage(file.getName(), newFileName , true);
				matcher.reset();
				try {
					Thread.sleep(25);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
		}
	}
}
