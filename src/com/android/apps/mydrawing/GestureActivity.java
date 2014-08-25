package com.android.apps.mydrawing;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.commons.lang.SerializationUtils;

import android.app.Activity;
import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class GestureActivity extends Activity implements OnGesturePerformedListener{
	GestureLibrary mLibrary;
	AsynNetworkClient tcpClient;

	private String serverIpAddress = "10.73.212.112";
	PrintWriter out;
	ObjectOutputStream oos;
	//ObjectInputStream ois = null;
	private boolean isConnected = false;
	Context context=null;
	DataOutputStream dos = null;
	DataInputStream dis = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture);
		
		context = getApplicationContext();
		mLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!mLibrary.load()) {
			finish();
		}

		GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestures);
		gestures.addOnGesturePerformedListener(this);
		
		//standard swipe gestures
		gestures.setOnTouchListener(new OnSwipeTouchListener(this) {
			@Override
			public void onSwipeLeft() {
				Toast.makeText(GestureActivity.this, "Swipe left", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSwipeRight() {
				Toast.makeText(GestureActivity.this, "Swipe right", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onDTap() {
				Toast.makeText(GestureActivity.this, "Double tap", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSwipeUp() {
				tcpClient.sendDataToDevice("key:up");
			}
			@Override
			public void onSwipeDown() {
				tcpClient.sendDataToDevice("key:down");
			}
		});
		//start connection 
		tcpClient = new AsynNetworkClient();
		tcpClient.execute(serverIpAddress);
	}

	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = mLibrary.recognize(gesture);
		String cmd;

		if (predictions.size() > 0 && predictions.get(0).score > 1.0) {
			String result = predictions.get(0).name;

			if ("open".equalsIgnoreCase(result)) {
				Toast.makeText(this, "Gesture 'O' -> opening text editor", Toast.LENGTH_LONG).show();
				//call send Data
				tcpClient.sendDataToDevice("msg:opening text editor");
				cmd = "mate";
				tcpClient.sendDataToDevice(cmd);
			} else if ("save".equalsIgnoreCase(result)) {
				Toast.makeText(this, "Gesture 'S' -> new email", Toast.LENGTH_LONG).show();
				tcpClient.sendDataToDevice("msg:new email");
				cmd = "open mailto:abc@yahoo.com";
				tcpClient.sendDataToDevice(cmd);
			} else if ("next line".equalsIgnoreCase(result)) {
				Toast.makeText(this, "Gesture 'next line'", Toast.LENGTH_LONG).show();
				tcpClient.sendDataToDevice("msg:next line");
				//cmd = "open mailto:abc@yahoo.com";
				//tcpClient.sendDataToDevice(cmd);
			} else if ("prev line".equalsIgnoreCase(result)) {
				Toast.makeText(this, "Gesture 'prev line'", Toast.LENGTH_LONG).show();
				tcpClient.sendDataToDevice("msg:prev line");
				//cmd = "open mailto:abc@yahoo.com";
				//tcpClient.sendDataToDevice(cmd);
			}
		}
	}

	private class AsynNetworkClient extends AsyncTask<String, Void,Boolean>{
		Socket socket;
		
		@Override
		protected void onPreExecute() {
			Toast.makeText(GestureActivity.this,"Starting Connection", Toast.LENGTH_LONG).show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			try{
				
				InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
				Log.d("ClientActivity", "C: Connecting...");
				socket = new Socket(serverAddr, 9876);
				try{
					dos = new DataOutputStream(socket.getOutputStream());
					//out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
					//out.println("msg:Hey Server!");
					PaintObject po = new PaintObject("testObject");
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
					
					
					
					PaintObject rcvdPO = (PaintObject)SerializationUtils.deserialize(bArray);
					//Log.d("Received Object:" , rcvdPO.toString());
					//String rcvdMessage = new String(bArray);
					showToast("Received Messages:"+ rcvdPO.toString());
					//Log.d("Received Messages:", rcvdMessage);
					Log.d("ClientActivity", "C: Sent.");
				
				} catch (Exception e) {
					Log.e("ClientActivity", "S: Error", e);
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
			isConnected = true;

			return isConnected;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (isConnected) {
				Toast.makeText(GestureActivity.this,"Connection successfull", Toast.LENGTH_LONG).show();
			}        
			super.onPostExecute(result);
		}

		
		//method to send data to connected device
		protected boolean sendDataToDevice(String cmd){

			 if ( isConnected ) 
				 out.println(cmd);
			 else{
				 Log.e("Connection:", "Connection Lost....");
				 //Notify app, re-initiate connection
				 
			 }
			return true;

			
		}
		
		//
		public void disconnect()
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
		}

		
		private void showToast(final String message) {
		    new Handler(context.getMainLooper()).post(new Runnable() {

		        @Override
		        public void run() {
		            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		        }
		    });
		}
	}



}
