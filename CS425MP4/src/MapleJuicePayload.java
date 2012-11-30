import java.io.Serializable;
import java.util.*;
//Basic Payload
public class MapleJuicePayload implements Serializable{
	int           messageType;
	int           messageLength;
	byte[]        payload;
	
	public MapleJuicePayload(int mt, int ml, byte[] data) {
		messageType = mt;
		messageLength = ml;
		payload = data;
	}
	

}
