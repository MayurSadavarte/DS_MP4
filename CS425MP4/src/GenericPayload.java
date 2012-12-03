import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


public class GenericPayload {

	public void printContents() 
	{
	
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
}
