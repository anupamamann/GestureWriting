package com.android.apps.mydrawing;

import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.graphics.Paint;
import android.graphics.Path.Direction;
import android.os.Bundle;

import com.android.apps.mydrawing.PaintObject.PathObject;

public class GestureActivity extends Activity {
	GestureLibrary mLibrary;
	AsyncNetworkClient tcpClient;

	private String serverIpAddress = "10.73.221.110";
	PrintWriter out;
	ObjectOutputStream oos;
	Context context=null;
	DrawingCanvas drawingBoard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture);
		
		context = getApplicationContext();
		mLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!mLibrary.load()) {
			finish();
		}
		
		drawingBoard = (DrawingCanvas) findViewById(R.id.drawingCanvas1);
		drawingBoard.setActivity(this);

		/*GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestures);
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
		});   */
	}
	
	public void sendDrawing(Paint paint, boolean init) {
		if (!init) {
			PaintObject po = new PaintObject("test", drawingBoard.getPaths());
			// execute fresh async task
			new AsyncNetworkClient(this, po).execute(serverIpAddress);
		}
	}
	
	public void drawReceived(PaintObject po) {
		List<PathObject> paths = po.getPaths();
		//temp tweak for testing
		MySerializablePath firstPath = null;
		firstPath = (MySerializablePath) paths.get(0).getPath();
		firstPath.addCircle(200, 300, 10, Direction.CCW);
		drawingBoard.addToPaths(firstPath);
		
	}

	/*@Override
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
	}*/
	
	/*private void showToast(final String message) {
	    new Handler(context.getMainLooper()).post(new Runnable() {

	        @Override
	        public void run() {
	            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	        }
	    });
	}*/

}

