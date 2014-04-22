package com.bKlen.binaryracer;

import java.io.IOException;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class BinaryRacer extends Activity
{

	private String name = "";
	//data received and data to send
	String dataR="";
	String dataS="";
	
	EditText nameET;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		nameET = (EditText) findViewById(R.id.nameEditText);
		nameET.addTextChangedListener(nameListener);
		nameET.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				nameET.setText(((RacerApplication)BinaryRacer.this.getApplication()).trackPos);
				
			}
		});
		
		nameET.setText("Name");
		
		Button loginButton = (Button) findViewById(R.id.loginButton);
		
		//Listening to button event
        loginButton.setOnClickListener(new View.OnClickListener()
        {
 
            public void onClick(View arg0)
            {
                //Starting a new Intent
            	//Intent driverMeeting = new Intent(getApplicationContext(), DriverMeeting.class);
 
                //Sending data to another Activity
                //nextScreen.putExtra("name", inputName.getText().toString());
                //nextScreen.putExtra("email", inputEmail.getText().toString());
 
                //Log.e("n", inputName.getText()+"."+ inputEmail.getText());
 
            	//Globals.USER_NAME = nameET.getText().toString();
            	//startActivity(driverMeeting);
            	//finish();
            	
            	/*try {
					((RacerApplication)BinaryRacer.this.getApplication()).sendData(nameET.getText().toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/

            	Intent driverMeeting;
            	if (((RacerApplication)BinaryRacer.this.getApplication()).trackPos.equals("O"))
		        	driverMeeting = new Intent(getApplicationContext(), DriverMeeting.class);
            	else
            		driverMeeting = new Intent(getApplicationContext(), DriverMeetingWait.class);
            	startActivity(driverMeeting);
            	thread.interrupt();
	        	finish();
 
            }
        });
        thread.start();
	}

	
	private TextWatcher nameListener = new TextWatcher()
	{
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
		}			
	};
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

	    View v = getCurrentFocus();
	    boolean ret = super.dispatchTouchEvent(event);

	    if (v instanceof EditText) {
	        View w = getCurrentFocus();
	        int scrcoords[] = new int[2];
	        w.getLocationOnScreen(scrcoords);
	        float x = event.getRawX() + w.getLeft() - scrcoords[0];
	        float y = event.getRawY() + w.getTop() - scrcoords[1];

	        Log.d("Activity", "Touch event "+event.getRawX()+","+event.getRawY()+" "+x+","+y+" rect "+w.getLeft()+","+w.getTop()+","+w.getRight()+","+w.getBottom()+" coords "+scrcoords[0]+","+scrcoords[1]);
	        if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) { 

	            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
	        }
	    }
	return ret;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	Handler h = new Handler();
    Runnable r=new Runnable()
    {
        public void run() 
        {
        	dataR = (((RacerApplication)BinaryRacer.this.getApplication()).dataList.get(0));
        	if (dataR.equals("RESTART"))
        	{
        		dataS = (((RacerApplication)BinaryRacer.this.getApplication()).trackPos) + ",R,OK";
        		try {
					((RacerApplication)BinaryRacer.this.getApplication()).sendData(dataS);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		(((RacerApplication)BinaryRacer.this.getApplication()).newData) = false;
        	}
        	else
        	{
        		Log.e("MCU stream", "invalid message from MCU");
        		(((RacerApplication)BinaryRacer.this.getApplication()).newData) = false;
        	}
        }
    };
    Thread thread = new Thread()
    {
        @Override
        public void run() 
        {
            while(true)
            {
            	if ((((RacerApplication)BinaryRacer.this.getApplication()).newData) == true)
            	{
            		h.post(r);
            	}
            	if (thread.isInterrupted())
            	{
            		return;
            	}
			}
        }
    };

}
