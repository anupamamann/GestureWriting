package com.android.apps.mydrawing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class StartActivity extends Activity {

	ImageView ivStart; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		ivStart = (ImageView)findViewById(R.id.ivStart);
		ivStart.setOnTouchListener(new OnSwipeTouchListener(this){
			
			@Override
			public void onSwipeLeft() {
				//start canvas activity with animation
				beginDrawing();
				super.onSwipeLeft();
			}
			
		});
		
	}
	
	
	

	public void beginDrawing(){
		Intent i = new Intent(this, GestureActivity.class);
		startActivity(i);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}
}
