package com.android.apps.mydrawing;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class ClientActivity extends Activity {


	EditText textOut;
	TextView textIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client);

		textOut = (EditText)findViewById(R.id.textout);
		Button buttonSend = (Button)findViewById(R.id.send);
		textIn = (TextView)findViewById(R.id.textin);

		buttonSend.setOnClickListener(buttonSendOnClickListener);

	}

	Button.OnClickListener buttonSendOnClickListener
	= new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			AsyncClient myClientTask = new AsyncClient(
					"192.168.1.12",9876);
				     myClientTask.execute();
				     
		
		}
	};

}
