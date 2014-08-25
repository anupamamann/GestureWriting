package com.android.apps.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
//import android.view.KeyEvent;

public class GestureWritingServer{

	
	
	public static void main(String args[])  throws IOException,
			ClassNotFoundException {
		System.setProperty("java.awt.headless", "false");
		int cTosPortNumber = 9876;
		String str = null;
	
		ServerSocket servSocket = new ServerSocket(cTosPortNumber);
		System.out.println("Waiting for a connection on " + cTosPortNumber);

//		ObjectOutputStream toClient=null; 
	//	ObjectInputStream fromClient = null;
		
		DataInputStream dis = null;
		DataOutputStream dos = null;
		
		String message="";		
		while(true){
			
			Socket fromClientSocket = servSocket.accept();
			 dis = new DataInputStream(fromClientSocket.getInputStream());
			try{
				int length = dis.readInt();
				byte[] bArray = new byte[length];
				dis.readFully(bArray, 0, length);
				System.out.println("Received Object");
				//fromClientSocket.getInputStream().read(bArray);
				//ByteArrayInputStream bis = new ByteArrayInputStream(bArray);
				//fromClient = new ObjectInputStream(bis);
				//	String msg = new String(bArray);
					//System.out.println("Received Data:" + msg);
				
				//String msg = "Hey";
				//byte[] mArray = msg.getBytes();
				dos = new DataOutputStream(fromClientSocket.getOutputStream());
				dos.writeInt(bArray.length);
				dos.write(bArray);
				//toClient = new ObjectOutputStream(bos);
				//fromClientSocket.getOutputStream());
				/*Object obj = fromClient.readObject();
				if(obj != null){
					System.out.println("Object not null");
				}
				toClient.writeObject(obj);*/
			//	String message = (String)fromClient.readObject();
				if(message == "bye"){
					break;
				}
			
			}catch(Exception ex){
				ex.printStackTrace();
				if(dos !=null)
					dos.close();
				if(dis != null)
					dis.close();
			}
		}
		dos.close();
		dis.close();
		
		servSocket.close();
		
	}
	
	
		/*	
		PrintWriter pw = new PrintWriter(fromClientSocket.getOutputStream(),true);
		
		Process process = null;

		BufferedReader br = new BufferedReader(new InputStreamReader(fromClientSocket.getInputStream()));
		 
		while ((str = br.readLine()) != null) {
		
		
		
			if (str.equals("bye")) {
				pw.println("bye");
				break;
			} else if (str.trim().equals("key:down")) {
				Robot robot;
				try {
					robot = new Robot();
					// Simulate a mouse click
			        robot.keyPress(KeyEvent.VK_DOWN);
			        robot.keyRelease(KeyEvent.VK_DOWN);
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (str.trim().equals("key:up")) {
				Robot robot;
				try {
					robot = new Robot();
					// Simulate a mouse click
			        robot.keyPress(KeyEvent.VK_UP);
			        robot.keyRelease(KeyEvent.VK_UP);
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (!str.trim().startsWith("msg:")) {
				// time to do some action
				List<String> commands = new ArrayList<String>();
				String[] cmdArgs = str.trim().split(" ");
				for (String cmdArg : cmdArgs) {
					commands.add(cmdArg);
				}
				ProcessBuilder builder = new ProcessBuilder(commands);

				try {
					process = builder.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
				OutputStream stdin = process.getOutputStream();
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(stdin));

				try {
					writer.write("Android!\nAndroid\n\n\nAndroid\nAndroid");
					writer.flush();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				//System.out.println("The message: " + str);
			}
		}
		pw.close();
		br.close();

		fromClientSocket.close();
		servSocket.close();
		
	} */

	
}
