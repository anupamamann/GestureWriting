package com.android.apps.mydrawing;

import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.gesture.GestureLibrary;
import android.graphics.Paint;
import android.graphics.Path.Direction;
import android.os.AsyncTask;
import android.os.Bundle;

import com.android.apps.mydrawing.PaintObject.PathObject;

public class GestureActivity extends Activity implements ShakeListener.Callback{
	GestureLibrary mLibrary;
	Socket socket;

	private String serverIpAddress = "10.73.212.112";
	PrintWriter out;
	ObjectOutputStream oos;
	Context context=null;
	DrawingCanvas drawingBoard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture);
		
		context = getApplicationContext();
		
		new AsyncNetworkReceive(this).executeOnExecutor(
				AsyncTask.THREAD_POOL_EXECUTOR, serverIpAddress);
		
		drawingBoard = (DrawingCanvas) findViewById(R.id.drawingCanvas1);
		drawingBoard.setActivity(this);

		
	}
	
	public void setSocket(Socket socket){
		this.socket = socket;
	}
	
	public void sendDrawing(Paint paint) {
		PaintObject po = new PaintObject("DrawConnect-Test", drawingBoard.getPaths());
		// execute fresh async task
		new AsyncNetworkSend(socket, this, po).execute(serverIpAddress);
	}

	public void drawReceived(PaintObject po) {
		if (po != null) {
			if(po.getDescription().equalsIgnoreCase("shake")){
				saveAndRedraw();
			}else{
				List<PathObject> paths = po.getPaths();
				// temp tweak for testing
				MySerializablePath firstPath = null;
				firstPath = (MySerializablePath) paths.get(0).getPath();
				if (firstPath != null) {
					firstPath.addCircle(200, 300, 20, Direction.CCW);
					drawingBoard.addToPaths(firstPath);
				}
			}
		}
	}

	@Override
	public void shakingStarted() {
	//send signal to connected devices
		PaintObject po = new PaintObject("shake");
		new AsyncNetworkSend(socket, this, po).execute(serverIpAddress);
		
	}

	@Override
	public void shakingStopped() {
		
	}
	
	public void saveAndRedraw(){
		drawingBoard.clearCanvas();
	}

	
}

