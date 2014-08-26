package com.android.apps.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

/**

 */
public class WorkerRunnable implements Runnable{



	protected Socket clientSocket = null;
	protected String serverText   = null;
	MultiThreadedServer parentServer = null;
	protected ArrayList<Socket> connList = null;

	public WorkerRunnable(Socket clientSocket, String serverText, MultiThreadedServer parent) {
		this.clientSocket = clientSocket;
		this.serverText   = serverText;
		this.parentServer = parent;
	}

	public void run() {
		try {

			InputStream input  = clientSocket.getInputStream();
			DataInputStream dis = new DataInputStream(input);
			// OutputStream output = clientSocket.getOutputStream();
			//DataOutputStream dos = new DataOutputStream(output);

			DataOutputStream dos = null;
			long time = System.currentTimeMillis();

			if(dis.available()>0){
				int length = dis.readInt();

				byte[] bArray = new byte[length];
				dis.readFully(bArray, 0, length);
				//fromClientSocket.getInputStream().read(bArray);
				//ByteArrayInputStream bis = new ByteArrayInputStream(bArray);
				//fromClient = new ObjectInputStream(bis);

				//PaintObject po = (PaintObject) SerializationUtils.deserialize(bArray);
				System.out.println("Connection size Object : "+ connList.size());
				for(Socket clientSocket : this.parentServer.getClients())
				{
					dos = new DataOutputStream(clientSocket.getOutputStream());
					dos.writeInt(length);
					dos.write(bArray);
				}
			}
			if(input!=null)
				input.close();
			if(dis!=null)
				dis.close();
			if(dos!=null)
				dos.close();
			System.out.println("Request processed: " + time);



		} catch (IOException e) {
			//report exception somewhere.
			e.printStackTrace();
		}

	}
}
