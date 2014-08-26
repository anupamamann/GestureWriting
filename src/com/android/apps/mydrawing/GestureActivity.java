package com.android.apps.mydrawing;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.gesture.GestureLibrary;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	public void onClear(MenuItem mi){
		shakingStarted();
	}
	
	public void setSocket(Socket socket){
		this.socket = socket;
	}
	
	public void sendDrawing() {
		PaintObject po = new PaintObject("DrawConnect-Test ", drawingBoard.getPaths());
		// execute fresh async task
		new AsyncNetworkSend(socket, this, po).execute(serverIpAddress);
	}

	public void drawReceived(PaintObject po) {
		if (po != null) {
			if (po.getDescription().equalsIgnoreCase("shake")) {
				saveAndRedraw();
			} else {
				List<PathObject> paths = po.getPaths();
				if (paths != null) {
					for (PathObject pathObj : paths)
						drawingBoard.addToPaths(pathObj);
				}
			}
			
		}
	}

	@Override
	public void shakingStarted() {
	//send signal to connected devices
		Toast.makeText(GestureActivity.this, "shake", Toast.LENGTH_LONG).show();
		PaintObject po = new PaintObject("shake");
		saveAndRedraw();
		new AsyncNetworkSend(socket, this, po).execute(serverIpAddress);		
	}

	
	@Override
	public void shakingStopped() {
		Toast.makeText(GestureActivity.this, "redrawing", Toast.LENGTH_LONG).show();
		saveAndRedraw();
	}
	
	public void saveAndRedraw(){
		drawingBoard.clearCanvas();
	}
	
	public void onBlack(View v) {
		drawingBoard.setColor(Color.BLACK);
	}

	public void onRed(View v) {
		drawingBoard.setColor(Color.RED);
	}

	public void onGreen(View v) {
		drawingBoard.setColor(Color.GREEN);
	}

	public void onBlue(View v) {
		drawingBoard.setColor(Color.BLUE);
	}

	public void onYellow(View v) {
		drawingBoard.setColor(Color.YELLOW);
	}

	public void onWhite(View v) {
		drawingBoard.setColor(Color.WHITE);
	}
	
	@Override
	protected void onDestroy() {
		if(this.socket!=null){
			
			try {
				this.socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		super.onDestroy();
	}
}

