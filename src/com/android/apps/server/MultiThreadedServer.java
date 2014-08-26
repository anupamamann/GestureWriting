package com.android.apps.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MultiThreadedServer implements Runnable{

	protected int          serverPort   = 9002;
	protected ServerSocket serverSocket = null;
	protected boolean      isStopped    = false;
	protected static ArrayList<Socket> connList = new ArrayList<Socket>() ;

	public MultiThreadedServer(int port){
		this.serverPort = port;
	}

	public ArrayList<Socket> getClients(){
		return connList;
	}

	public synchronized void removeSocket(Socket socket){
		if(connList.contains(socket)){
			connList.remove(socket);
		}
	}

	public void run(){
		openServerSocket();
		System.out.println("Started na");
		while(! isStopped()){
			Socket clientSocket = null;
			try {

				clientSocket = this.serverSocket.accept();
				connList.add(clientSocket);

			} catch (IOException e) {
				if(isStopped()) {
					System.out.println("Server Stopped.") ;
					return;
				}
				throw new RuntimeException(
						"Error accepting client connection", e);
			}
			new Thread(
					new WorkerRunnable(
							clientSocket, "Multithreaded Server")
					).start();
		}
		System.out.println("Server Stopped.") ;
	}


	private synchronized boolean isStopped() {
		return this.isStopped;
	}

	public synchronized void stop(){
		this.isStopped = true;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException("Error closing server", e);
		}
	}

	private void openServerSocket() {
		try {
			this.serverSocket = new ServerSocket(this.serverPort);
		} catch (IOException e) {
			throw new RuntimeException("Cannot open port", e);
		}
	}

	public static void main(String[] args)
	{
		MultiThreadedServer server = new MultiThreadedServer(9002);
		server.run();

	}



	private class WorkerRunnable implements Runnable{

		protected Socket clientSocket = null;
		protected String clientName   = null;
		MultiThreadedServer parentServer = null;
	
		public WorkerRunnable(Socket clientSocket, String clientName) {
			this.clientSocket = clientSocket;
			this.clientName   = clientName;

		}

		@Override
		public void run() {
			InputStream input  = null;
			DataInputStream dis = null;
			DataOutputStream dos = null;

			try {
				input = clientSocket.getInputStream();
				dis = new DataInputStream(input);
				while(true){
					if(dis.available() > 0){
						int length = dis.readInt();
						if(length >0){
							byte[] bArray = new byte[length];
							dis.readFully(bArray, 0, length);
							System.out.println("Connection size Object : "+ connList.size());
							for(Socket clientSocket : connList)
							{
								dos = new DataOutputStream(clientSocket.getOutputStream());
								dos.writeInt(length);
								dos.write(bArray);
							}
						}else{
							System.out.println("listening...");
						}
					}else{
						//System.out.println("listening...");
					}
				}
			} catch (IOException e) {
				//report exception somewhere.
				e.printStackTrace();
			}finally{
				System.out.println("removing socket");
				removeSocket(clientSocket);
				try{
				if(input!=null)
					input.close();
				if(dis!=null)
					dis.close();
				if(dos!=null)
					dos.close();
				if(clientSocket !=null)
					clientSocket.close();
			
				}catch(IOException ex){
					System.out.println("Exception in close");
					ex.printStackTrace();
				}
				
				
			}

		}

	}

}
