package com.android.apps.mydrawing;

import java.io.DataOutputStream;
import java.net.Socket;

import org.apache.commons.lang.SerializationUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class AsyncNetworkSend extends AsyncTask<String, Void, Boolean> {

	GestureActivity parentActivity = null;
	DataOutputStream dos = null;
	Socket socket = null;
	PaintObject po;

	public AsyncNetworkSend(Socket socket, Activity activity, PaintObject po) {
		this.socket = socket;
		this.parentActivity = (GestureActivity) activity;
		this.po = po;
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

	// method to send data to connected device
	protected boolean send() {
		showToast("sending data");
		if (socket != null) {
			try {
				dos = new DataOutputStream(socket.getOutputStream());
				// out = new PrintWriter(new BufferedWriter(new
				// OutputStreamWriter(socket.getOutputStream())), true);
				// out.println("msg:Hey Server!");
				//po = new PaintObject("testObject");
				byte[] mArray = SerializationUtils.serialize(po);
				// String msg = "Hey";
				// byte[] mArray = msg.getBytes();
				dos.writeInt(mArray.length);
				dos.write(mArray);
				dos.flush();
				showToast("msg sent");

			} catch (Exception ex) {
				Log.e("Send Error:", ex.getMessage());
				showToast("Error sending message");
			} finally {
				if (dos != null) {
					try {
						// dos.close();
					} catch (Exception ex) {
						Log.e("Dos Close Error:", ex.getMessage());
					}
				}
			}
		} else {
			Log.e("Socket Error:", "Null Socket");
			showToast("Null Socket");
		}

		return true;

	}

	private void showToast(final String message) {
		new Handler(parentActivity.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(parentActivity.getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
			}
		});
	}

}

