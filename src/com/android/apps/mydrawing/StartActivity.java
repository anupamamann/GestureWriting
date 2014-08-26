package com.android.apps.mydrawing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
	}
	
	public void onBegin(View v) {
		Intent i = new Intent(this, GestureActivity.class);
		startActivity(i);
		overridePendingTransition(R.anim.slide_up_entry, R.anim.slide_up_exit);
	}
}
