import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Vector;


public class MasterRecoveryClient {
	Machine machine=null;
	
	public MasterRecoveryClient(Machine m) {
		machine = m;
	}
	
	public void RecoverMasterInfo() {
		try {
			WriteLog.writelog(machine.myName, "Sending MasterRecoveryPackets to all the nodes in the memberList");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Socket sock=null;
		MasterRecoveryPacket mrPacket = null;
		String indMember=null;
		for(String member: machine.memberList) {
			indMember=member;
			try {
				sock = new Socket(member, Machine.MASTER_RECOVERY_PORT);
				//responseRequired = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Connecting...");
			
			ObjectInputStream ois = null;
			InputStream sim = null;
			try {
				sim = sock.getInputStream();
				ois = new ObjectInputStream(sim);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				//mjPayload = new MapleJuicePayload();
				
				mrPacket = ((MasterRecoveryPacket)(ois.readObject()));
				
				for(String fileName: mrPacket.myFileList)
				{
					if(!machine.file_node_map.containsKey(fileName))
					{
						Vector<String> tempNodeLst = new Vector<String>();
						tempNodeLst.add(member);
						machine.file_node_map.put(fileName, tempNodeLst);
					}
					else
						machine.file_node_map.get(fileName).add(member);
				}
				
				machine.node_file_map.put(member, mrPacket.myFileList);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
			try {
				ois.close();
				sim.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		

			try {
				WriteLog.writelog(machine.myName, "MasterRecovery: received - "+mrPacket.myFileList+" from "+indMember);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			WriteLog.writelog(machine.myName, "Master Failure Recovery Done!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
