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

public class GestureActivityCombined extends Activity implements OnGesturePerformedListener{
	GestureLibrary mLibrary;
	AsynNetworkClient connectToServer = null;
	AsynNetworkSend sendToServer = null;
	Socket socket = null;

	private String serverIpAddress = "10.73.212.112";
	//PrintWriter out;
	//ObjectOutputStream oos;
	//ObjectInputStream ois = null;
	private boolean isConnected = false;
	Context context=null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture);
		context = getApplicationContext();
		connectToServer = new AsynNetworkClient(this);
		connectToServer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverIpAddress);
	
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sendDrawing();
		
		/*mLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
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
				sendToServer = new AsynNetworkSend();
				sendToServer.execute();
				//tcpClient.sendDataToDevice("key:up");
			}
			@Override
			public void onSwipeDown() {
				//tcpClient.sendDataToDevice("key:down");
			}
		});
		 */


	}

	public void setSocket(Socket socket){
		this.socket = socket;
	}

	public void sendDrawing(){
		sendToServer = new AsynNetworkSend(socket);
		sendToServer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"some data");
	}

	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = mLibrary.recognize(gesture);
		String cmd;

	/*	if (predictions.size() > 0 && predictions.get(0).score > 1.0) {
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
		}*/
	}

	private class AsynNetworkClient extends AsyncTask<String, Void,Boolean>{

		GestureActivityCombined parentActivity =null;
		
		DataInputStream dis = null;

		public AsynNetworkClient(Activity a) {
			this.parentActivity = (GestureActivityCombined)a;
		}

		@Override
		protected void onPreExecute() {
			Toast.makeText(GestureActivityCombined.this,"Starting Connection", Toast.LENGTH_LONG).show();
			super.onPreExecute();
		}


		@Override
		protected Boolean doInBackground(String... params) {
			try{

				InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
				Log.d("ClientActivity", "C: Connecting...");
				socket = new Socket(serverAddr, 9002);

				try{
					parentActivity.setSocket(socket);
					showToast("Waiting for a msg");
					dis = new DataInputStream(socket.getInputStream());
					while(true){
						if(dis.available()>0){
							int length = dis.readInt();
							if(length >0){
								byte[] bArray = new byte[length];
								dis.readFully(bArray, 0, bArray.length); // read the message
								PaintObject rcvdPO = (PaintObject)SerializationUtils.deserialize(bArray);
								showToast("Received Messages:"+ rcvdPO.toString());
							}else{
								showToast("no message");
							}
						}else{
							//System.out.println("listening");
						}
					}
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
			}finally{
				showToast("Came to Finally");
					try {
					if(dis!=null)
						dis.close();
					if(socket!=null)
						socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			isConnected = true;
			return isConnected;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (isConnected) {
				Toast.makeText(GestureActivityCombined.this,"Connection successfull", Toast.LENGTH_LONG).show();
			}        
			super.onPostExecute(result);
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

	private class AsynNetworkSend extends AsyncTask<String, Void,Boolean>{

		DataOutputStream dos = null;
		Socket socket = null;
	
		public AsynNetworkSend(Socket socket){
			this.socket = socket;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			showToast("pre sending");
			super.onPreExecute();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			showToast("post sending");
			super.onPostExecute(result);
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			showToast("Trying to send data");
			return send();
			
		}

		//method to send data to connected device
		protected boolean send(){
			showToast("sending data");
			if ( socket != null ){
				try{
					dos = new DataOutputStream(socket.getOutputStream());
					//out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
					//out.println("msg:Hey Server!");
					PaintObject po = new PaintObject("testObject ");
					byte[] mArray = SerializationUtils.serialize(po);
					//	String msg = "Hey";
					//byte[] mArray = msg.getBytes();
					dos.writeInt(mArray.length);
					dos.write(mArray);
					dos.flush();
					showToast("msg sent");

				}catch(Exception ex){
					Log.e("Send Error:", ex.getMessage());
					showToast("Error sending message");
				}finally{
					if(dos!=null){
						try{
							//dos.close();
						}catch(Exception ex){
							Log.e("Dos Close Error:", ex.getMessage());
						}
					}
				}
			}else{
				Log.e("Socket Error:", "Null Socket");
				showToast("Null Socket");
			}

			return true;	

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
