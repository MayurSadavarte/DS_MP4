import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Vector;


public class client {

	/**
	 * @param args
	 */
	public static FileTransferServer server;
	public static DatagramSocket sock = null;
	public static String myName;
	
	public static void main(String[] args) throws IOException {
		
		//args[0] is the ip address connection to 
		String masterIP = args[0];
		
		try {
			sock = new DatagramSocket(Machine.FILE_OPERATIONS_PORT);
		} catch (SocketException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		try {
			myName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Scanner s = new Scanner(System.in);
		String cmd = null;
		
		//Runnable runnable = new FileTransferServer();
		//
		//
		server = new FileTransferServer();
		server.start();
		
		while ((cmd = s.nextLine()) != null) {
			
			WriteLog.writelog(myName, cmd);
			
			if ("exit".equals(cmd)) {
				System.exit(0);
			}
			else if(cmd.startsWith("put ")){
				Scanner lineScanner = new Scanner(cmd);
				String command, localFileName, sdfsFileName;
				
				command = lineScanner.next();
				localFileName = lineScanner.next();
				sdfsFileName = lineScanner.next();
				
				Vector<String> putMsg = new Vector<String>();
				
				putMsg.add("P");
				putMsg.add(localFileName);
				putMsg.add(sdfsFileName);
				putMsg.add(myName);
				
//				putMsg.add(cmd.substring(4, cmd.indexOf(' ', 4)));
//				putMsg.add(cmd.substring(cmd.lastIndexOf(' ')+1));
//				putMsg.add(myName);
				
				WriteLog.writelog(myName, "sendPutMsg:" + putMsg.elementAt(1)+putMsg.elementAt(2));
/*				byte[] mList = null;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
			    try {
			    	ObjectOutputStream oos = new ObjectOutputStream(baos);
			    	oos.writeObject(putMsg);
			    	oos.flush();
			    	//TODO - need to decide whether we need to send length also in first packet and then actual packet
			    	// get the byte array of the object
			    	mList = baos.toByteArray();
			    } catch(IOException e) {
			    	e.printStackTrace();
			    }
				
				sendMsg(sock, masterIP, mList, Machine.FILE_OPERATIONS_PORT); */
				
				sendMsgToMaster(putMsg, masterIP);
				//String sourceFN = cmd.substring(4, cmd.indexOf(' ', 4));
				//server.setSource(sourceFN);
			}
			else if(cmd.startsWith("get ")){
				Vector<String> getMsg = new Vector<String>();
				Scanner lineScanner = new Scanner(cmd);
				String command, localFileName, sdfsFileName;
				
				command = lineScanner.next();
				sdfsFileName = lineScanner.next();
				localFileName = lineScanner.next();
				
				getMsg.add("G");
//				getMsg.add(cmd.substring(4, cmd.indexOf(' ', 4)));
				getMsg.add(sdfsFileName);
				getMsg.add(myName);
				
				WriteLog.writelog(myName, "sendPutMsg:"+getMsg.elementAt(1));
/*				byte[] mList = null;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
			    try {
			    	ObjectOutputStream oos = new ObjectOutputStream(baos);
			    	oos.writeObject(getMsg);
			    	oos.flush();
			    	
			    	// get the byte array of the object
			    	mList = baos.toByteArray();
			    } catch(IOException e) {
			    	e.printStackTrace();
			    }
				
				sendMsg(sock, masterIP, mList, Machine.FILE_OPERATIONS_PORT); */
				sendMsgToMaster(getMsg, masterIP);
				Vector<String> serverIP = recvListMsg();
				//String copyFN = cmd.substring(cmd.lastIndexOf(' ')+1);
				
				//Runnable runnable = new FileTransferClient(copyFN, cmd.substring(4, cmd.indexOf(' ', 4)),serverIP.elementAt(0));
				Runnable runnable = new FileTransferClient(localFileName, sdfsFileName,serverIP.elementAt(0));

				Thread thread = new Thread(runnable);
				thread.start();
			}
			else if(cmd.startsWith("delete ")){
				Vector<String> delMsg = new Vector<String>();

				Scanner lineScanner = new Scanner(cmd);
				String command, localFileName, sdfsFileName;
				
				command = lineScanner.next();
				sdfsFileName = lineScanner.next();
				
				delMsg.add("D");
				delMsg.add(sdfsFileName);
				
				WriteLog.writelog(myName, "sendPutMsg: " + delMsg.elementAt(1));
/*				byte[] mList = null;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
			    try {
			    	ObjectOutputStream oos = new ObjectOutputStream(baos);
			    	oos.writeObject(delMsg);
			    	oos.flush();
			    	
			    	// get the byte array of the object
			    	mList = baos.toByteArray();
			    } catch(IOException e) {
			    	e.printStackTrace();
			    }
				
				sendMsg(sock, masterIP, mList, Machine.FILE_OPERATIONS_PORT); */
				sendMsgToMaster(delMsg, masterIP);
			}
			else if(cmd.startsWith("updateMaster ")){
				masterIP = cmd.substring(cmd.lastIndexOf(' ')+1);
				System.out.println("New master - "+masterIP);
			}
			else if(cmd.startsWith("maple ")) {

				Vector<String> mapleMsg = new Vector<String>();
				Vector<String> putMsg = new Vector<String>();
				Scanner lineScanner = new Scanner(cmd);
				String command, jarName, sdfsFilePrefix;
				Vector<String> sdfsFiles = new Vector<String>();
				
 				command = lineScanner.next();
				jarName = lineScanner.next();
				sdfsFilePrefix = lineScanner.next();
				
				putMsg.add("P");
				putMsg.add(jarName);
				putMsg.add(jarName);
				
				sendMsgToMaster(putMsg, masterIP);
				
				while(lineScanner.hasNext()){
					sdfsFiles.add(lineScanner.next());
				}
				
				mapleMsg.add("maple");
				mapleMsg.add(jarName);
				mapleMsg.add(sdfsFilePrefix);
				
				for(String sdfsFile : sdfsFiles) {
					mapleMsg.add(sdfsFile);
				}
				
/*				WriteLog.writelog(myName, "sendMapleMsg: " + mapleMsg.elementAt(1));
				byte[] mList = null;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
			    try {
			    	ObjectOutputStream oos = new ObjectOutputStream(baos);
			    	oos.writeObject(mapleMsg);
			    	oos.flush();
			    	
			    	// get the byte array of the object
			    	mList = baos.toByteArray();
			    } catch(IOException e) {
			    	e.printStackTrace();
			    }
				
				sendMsg(sock, masterIP, mList, Machine.FILE_OPERATIONS_PORT); */
				
				sendMsgToMaster(mapleMsg, masterIP);
				
			}else if(cmd.startsWith("juice ")) {
				Vector<String> juiceMsg = new Vector<String>();
				Scanner lineScanner = new Scanner(cmd);
				String command, jarName, sdfsFilePrefix, juiceFileName;
				Integer numJuices;
				
 				command = lineScanner.next();
				jarName = lineScanner.next();
				numJuices = lineScanner.nextInt();
				sdfsFilePrefix = lineScanner.next();
				juiceFileName = lineScanner.next();
								
				juiceMsg.add("maple");
				juiceMsg.add(jarName);
				juiceMsg.add(numJuices.toString());
				juiceMsg.add(sdfsFilePrefix);
				juiceMsg.add(juiceFileName);
				
				WriteLog.writelog(myName, "sendJuiceMsg: " + juiceMsg.elementAt(1));
				/*byte[] mList = null;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
			    try {
			    	ObjectOutputStream oos = new ObjectOutputStream(baos);
			    	oos.writeObject(juiceMsg);
			    	oos.flush();
			    	
			    	// get the byte array of the object
			    	mList = baos.toByteArray();
			    } catch(IOException e) {
			    	e.printStackTrace();
			    }
				
				sendMsg(sock, masterIP, mList, Machine.FILE_OPERATIONS_PORT); */
				
				sendMsgToMaster(juiceMsg, masterIP);
			}
			
		}

	}
	
	private static void sendMsgToMaster(Vector<String> message, String masterIP) {
		
		byte[] mList = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try {
	    	ObjectOutputStream oos = new ObjectOutputStream(baos);
	    	oos.writeObject(message);
	    	oos.flush();
	    	
	    	// get the byte array of the object
	    	mList = baos.toByteArray();
	    } catch(IOException e) {
	    	e.printStackTrace();
	    }
		
		sendMsg(sock, masterIP, mList, Machine.FILE_OPERATIONS_PORT);

	}
	
	public static void sendMsg(DatagramSocket sock, String ip, byte[] msg, int portN) {
		try {
			InetAddress ipAddr = InetAddress.getByName(ip);
			//byte[] sendData = msg.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(msg,
					msg.length, ipAddr, portN);
			sock.send(sendPacket);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String recvStrMsg() {
		DatagramPacket recvPacket;
		String recvMsg = null;
		byte[] recvData = new byte[1024];
		//recvPacket = new DatagramPacket(recvData,recvData.length);
		
		try {
			recvPacket = new DatagramPacket(recvData,recvData.length);
			
			sock.receive(recvPacket);
			//TODO - need to decide whether we need to define this length or not!!
			ByteArrayInputStream bais = new ByteArrayInputStream(recvData);
		
			ObjectInputStream ois = new ObjectInputStream(bais);
			recvMsg = (String)ois.readObject();
			//WriteLog.writelog(myName, "received from UDP "+recvMsg);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();			
		}
		
		return recvMsg;
	}

	private static Vector<String> recvListMsg()
	{
		DatagramPacket recvPacket;
		byte[] recvData = new byte[1024];
		Vector<String> returnList=new Vector<String>();
		try {
			recvPacket = new DatagramPacket(recvData,recvData.length);
			sock.receive(recvPacket);
			//TODO - need to decide whether we need to define this length or not!!
			ByteArrayInputStream bais = new ByteArrayInputStream(recvData);
		
			ObjectInputStream ois = new ObjectInputStream(bais);
			returnList = (Vector<String>)ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();			
		}
		
		return(returnList);
	}
	
}
