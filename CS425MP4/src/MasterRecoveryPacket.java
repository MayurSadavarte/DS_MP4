import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Vector;


public class MasterRecoveryPacket implements Serializable {
	Vector<String> myFileList=null;
	
	public MasterRecoveryPacket(Machine m) {
		myFileList = m.myFileList;
	}
	
	public void sendPacket(Socket sock) {
		ObjectOutputStream oos = null;
		OutputStream som = null;
		try {
			som = sock.getOutputStream();
			oos = new ObjectOutputStream(som);
			
			oos.writeObject(this);
			//oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		finally {
				if(oos != null)
					try {
						oos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				try {
					som.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {

					sock.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}

	}
}
