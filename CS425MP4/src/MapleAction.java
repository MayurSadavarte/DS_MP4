
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
}
