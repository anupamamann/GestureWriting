package com.android.apps.mydrawing;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.util.Log;

public class AsyncClientOld extends AsyncTask {

	String dstAddress;
	  int dstPort;
	  String response = "";
	
	  
	 AsyncClientOld(String addr, int port){
		   dstAddress = addr;
		   dstPort = port;
		  }
	 
	@Override
	protected Object doInBackground(Object... params) {
		  Socket socket = null;
		  DataOutputStream dataOutputStream = null;
			DataInputStream dataInputStream = null;
		try{
			socket = new Socket("192.168.1.12", 9876);
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			dataInputStream = new DataInputStream(socket.getInputStream());
			dataOutputStream.writeUTF("hello");
			Log.d("Response:" , dataInputStream.readUTF());
			
		}catch (UnknownHostException e) {
			e.printStackTrace();
			response = "UnknownHostException: " + e.toString();
			Log.d("Response:", response);
		} catch (IOException e) {
			e.printStackTrace();
			response = "IOException: " + e.toString();
			Log.d("Response:", response);
		}finally{
			if (socket != null){
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		// textResponse.setText(response);
		Log.d("Response:", response);

		super.onPostExecute(result);
	}
	

	
}
