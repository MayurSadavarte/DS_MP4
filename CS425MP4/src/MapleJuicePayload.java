import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;



//Payload types


//Basic Payload
public class MapleJuicePayload implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5737470900117994737L;
	int           messageType;
	//int           messageLength;
	byte[]        payload;
	
	public static final int MapleActionType = 1 ;
	public MapleJuicePayload(int mt, byte[] data) {
		messageType = mt;
		messageLength = ml;
		payload = data;
		
	}
	
	public byte[] getByteArray(Object originalObject) {
		//byte[] op = new byte[1016];
		ByteArrayOutputStream baos=null;
		ObjectOutputStream oos=null;
		baos = new ByteArrayOutputStream();
		
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(originalObject);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return(baos.toByteArray());
	}
	
	public void parseByteArray() {
		ByteArrayInputStream bais = new ByteArrayInputStream(payload);
		try {
			ObjectInputStream oos = new ObjectInputStream(bais);
			GenericPayload dummy=null;
			MapleAction maple_action=null;
			try {
				//switch messageType:
					//case MapleActionType:
						//dummy 
				maple_action  = (MapleAction)oos.readObject();
				dummy = maple_action;
				dummy.printContents();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*mapleTaskId = dummy.mapleTaskId;
			machineId = dummy.machineId;
			mapleExe = dummy.mapleExe;
			inputFileInfo = dummy.inputFileInfo;
			outputFilePrefix = dummy.outputFilePrefix;
			
			System.out.println(mapleTaskId);
			System.out.println(machineId);
			System.out.println(mapleExe);
			System.out.println(inputFileInfo);
			System.out.println(outputFilePrefix);*/
			
			//this = new MapleAction();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
