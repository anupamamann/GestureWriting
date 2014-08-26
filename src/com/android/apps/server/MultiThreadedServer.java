package com.android.apps.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MultiThreadedServer implements Runnable{

    protected int          serverPort   = 8080;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;
    protected ArrayList<Socket> connList = new ArrayList<Socket>() ;

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
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
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
                    clientSocket, "Multithreaded Server", this)
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
    	new Thread(server).start();

    	/*try {
    	   // Thread.sleep(20 * 1000);
    	} catch (InterruptedException e) {
    	    e.printStackTrace();
    	}*/
    	//System.out.println("Stopping Server");
    	//server.stop();
    	
    }

}
