package com.android.apps.mydrawing;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.lang.SerializationUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class AsyncNetworkReceive extends AsyncTask<String, Void,Boolean>{

	GestureActivity parentActivity =null;
	DataInputStream dis = null;
	Socket socket = null;
	boolean isConnected = false;

	public AsyncNetworkReceive(Activity a) {
		this.parentActivity = (GestureActivity)a;
	}

	@Override
	protected void onPreExecute() {
		Toast.makeText(parentActivity,"Starting Connection", Toast.LENGTH_LONG).show();
		super.onPreExecute();
	}


	@Override
	protected Boolean doInBackground(String... params) {
		try {
			InetAddress serverAddr = InetAddress.getByName(params[0]);
			Log.d("ClientActivity", "C: Connecting...");
			socket = new Socket(serverAddr, 9002);
			isConnected = true;

			try {
				parentActivity.setSocket(socket);
				//showToast("Waiting for a msg");
				dis = new DataInputStream(socket.getInputStream());
				while (true) {
					if (dis.available() > 0) {
						int length = dis.readInt();
						if (length > 0) {
							byte[] bArray = new byte[length];
							dis.readFully(bArray, 0, bArray.length); // read the
																		// message
							PaintObject rcvdPO = (PaintObject) SerializationUtils
									.deserialize(bArray);
							//showToast("Received Messages:" + rcvdPO.toString());
							showOnDrawingBoard(rcvdPO);
						} else {
							//showToast("no message");
						}
					} else {
						// System.out.println("listening");
					}
				}
			} catch (Exception e) {
				Log.e("ClientActivity", "S: Error", e);
			}

		} catch (UnknownHostException e) {
			// showToast("Don't know about host: ");
			Log.e("", e.getMessage());
		} catch (IOException e) {
			// showToast("Couldn't get I/O for the connection to: ");
			Log.e("", e.getMessage());
		} catch (Exception e) {

			// showToast(GestureActivity.this, "Exception: ");
			Log.e("", e.getMessage());
		} finally {
			//showToast("Came to Finally");
			try {
				if (dis != null)
					dis.close();
				if (this.socket != null)
					this.socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return isConnected;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (isConnected) {
			Toast.makeText(parentActivity,"Connection successfull", Toast.LENGTH_LONG).show();
		}        
		super.onPostExecute(result);
	}
	
	/*@Override
	protected void onPostExecute(PaintObject result) {
		if (isConnected) {
			Toast.makeText(parentActivity,"Connection successfull", Toast.LENGTH_LONG).show();
			((GestureActivity)activity).drawReceived(result);
		}        
		super.onPostExecute(result);
	}*/
	
	private void showToast(final String message) {
		new Handler(parentActivity.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(parentActivity.getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
			}
		});
	}
	
	private void showOnDrawingBoard(final PaintObject po) {
		new Handler(parentActivity.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				parentActivity.drawReceived(po);
			}
		});
	}
}

