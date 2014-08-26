package com.android.apps.mydrawing;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.lang.SerializationUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AsyncNetworkClient extends AsyncTask<String, Void, PaintObject>{
	Socket socket;
	DataOutputStream dos = null;
	DataInputStream dis = null;
    boolean isConnected = false;
    PaintObject po;
    Activity activity;
    
    public AsyncNetworkClient(Activity a, PaintObject po) {
    	this.activity = a;
    	this.po = po;
    }
	
	@Override
	protected void onPreExecute() {
		//Toast.makeText(GestureActivity.class,"Starting Connection", Toast.LENGTH_LONG).show();
		super.onPreExecute();
	}

	@Override
	protected PaintObject doInBackground(String... params) {
		PaintObject rcvdPO = null;
		try{
			InetAddress serverAddr = InetAddress.getByName(params[0]);
			Log.d("ClientActivity", "C: Connecting ...");
			socket = new Socket(serverAddr, 9002);
			try{
				dos = new DataOutputStream(socket.getOutputStream());
				isConnected = true;

				byte[] mArray = SerializationUtils.serialize(po);
				//	String msg = "Hey";
				//byte[] mArray = msg.getBytes();
				dos.writeInt(mArray.length);
				dos.write(mArray);
				dos.flush();
				
				dis = new DataInputStream(socket.getInputStream());
				int length = dis.readInt();
				byte[] bArray = new byte[length];
				if(length>0) {
				    dis.readFully(bArray, 0, bArray.length); // read the message
				}
				
				rcvdPO = (PaintObject)SerializationUtils.deserialize(bArray);
				//Log.d("Received Object:" , rcvdPO.toString());
				//String rcvdMessage = new String(bArray);
				//GestureActivity.showToast("Received Messages:"+ rcvdPO.toString());
				//Log.d("Received Messages:", rcvdMessage);
				Log.d("Client", "Received Messages:"+ rcvdPO.toString());
			} catch (Exception e) {
				Log.e("Client", "S: Error", e);
			}

		} catch (UnknownHostException e) {
			//showToast("Don't know about host: ");
			Log.e("", e.getMessage());
		} catch (IOException e) {
			//showToast("Couldn't get I/O for the connection to: ");
			Log.e("", e.getMessage());
		}catch (Exception e) {
			//showToast(GestureActivity.this, "Exception: ");
			Log.e("", e.getMessage());
		}
		return rcvdPO;
	}

	@Override
	protected void onPostExecute(PaintObject result) {
		if (isConnected) {
			Toast.makeText(activity, "done", Toast.LENGTH_SHORT).show();
			((GestureActivity)activity).drawReceived(result);
		}        
		super.onPostExecute(result);
	}
	
	/*//method to send data to connected device
	protected boolean sendDataToDevice(String cmd){
		 if ( isConnected ) 
			 out.println(cmd);
		 else{
			 Log.e("Connection:", "Connection Lost....");
			 //Notify app, re-initiate connection 
		 }
		return true;
	}*/
	
	/*public void disconnect()
	{
	    if ( isConnected )
	    {
	        try {
	            out.close();
	            socket.close();
	            isConnected = false;
	        } catch (IOException e) {
	        	Toast.makeText(GestureActivity.this, "Couldn't get I/O for the connection",Toast.LENGTH_LONG).show();
	            Log.e("Error", e.getMessage());
	        }            
	    }
	}*/

}



