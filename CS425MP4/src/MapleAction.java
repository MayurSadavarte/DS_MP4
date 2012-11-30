
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

//Payload for sending Maple Action Payload to worker nodes
public class MapleAction {
	Integer             mapleTaskId;
	Integer             machineId;
	String              mapleExe;
	ArrayList<String[]> inputFileInfo;
	String              outputFilePrefix; 
	

	
	public byte[] getByteArray() {
		byte[] op = new byte[1016];
		ByteArrayOutputStream baos=null;
		ObjectOutputStream oos=null;
		baos = new ByteArrayOutputStream();
		
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return(baos.toByteArray());
	}
	
	public void parseByteArray(byte[] ip) {
		ByteArrayInputStream bais = new ByteArrayInputStream(ip);
		try {
			ObjectInputStream oos = new ObjectInputStream(bais);
			MapleAction dummy=null;
			try {
				dummy = (MapleAction)oos.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mapleTaskId = dummy.mapleTaskId;
			machineId = dummy.machineId;
			mapleExe = dummy.mapleExe;
			inputFileInfo = dummy.inputFileInfo;
			outputFilePrefix = dummy.outputFilePrefix;
			
			System.out.println(mapleTaskId);
			System.out.println(machineId);
			System.out.println(mapleExe);
			System.out.println(inputFileInfo);
			System.out.println(outputFilePrefix);
			
			//this = new MapleAction();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
