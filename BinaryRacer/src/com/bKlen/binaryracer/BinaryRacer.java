package com.bKlen.binaryracer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BinaryRacer extends Activity
{
	TextView label;					// Information label i.e. MCU version, waiting for inner to connect
	TextView appVersionTextView;	// Android application label
	String dataS;					// Data to be sent too Bluetooth device(MCU)
	public List<String> dataList;	// ArrayList that holds parsed data read in from Bluetooth
	Button loginButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		// Initialize variables
		dataS = "";
		dataList = new ArrayList<String>();
		loginButton = (Button) findViewById(R.id.loginButton);
		label = (TextView) findViewById(R.id.label);
		appVersionTextView = (TextView) findViewById(R.id.appVersionTextView);
		
		// Set android version text view to current version
		String text = "Android App Version: " + ((RacerApplication)BinaryRacer.this.getApplication()).getVersion();
		appVersionTextView.setText(text);
		
		// Disable login button when app is first started
		if ((((RacerApplication)BinaryRacer.this.getApplication()).firstRestart))
			loginButton.setEnabled(false);
		
		// Start thread to start reading and writing to the MCU over Bluetooth
		thread.start();
		
		//Listening to button event
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
            	// Navigates to driver meeting if outer track otherwise it goes to the driver meeting wait screen
            	// then closes the activity and stops the thread
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
	}

	Handler h = new Handler();
    Runnable r=new Runnable()
    {
        public void run() 
        {
        	// Stores read in data in dataList then processes the data
        	dataList = ((RacerApplication)BinaryRacer.this.getApplication()).dataList;
        	
        	// Sends MCU <T>,R,OK and enables the loginButton
        	if (dataList.get(0).equals("RESTART"))
        	{
        		dataS = (((RacerApplication)BinaryRacer.this.getApplication()).trackPos) + ",R,OK";
        		try {
					((RacerApplication)BinaryRacer.this.getApplication()).sendData(dataS);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		(((RacerApplication)BinaryRacer.this.getApplication()).newData) = false;
        		loginButton.setEnabled(true);
        	}
        	// Sends MCU <T> as a heart beat
        	else if (dataList.get(0).equals("HB"))
        	{
        		dataS = (((RacerApplication)BinaryRacer.this.getApplication()).trackPos);
        		try {
					((RacerApplication)BinaryRacer.this.getApplication()).sendData(dataS);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		(((RacerApplication)BinaryRacer.this.getApplication()).newData) = false;
        	}
        	// Sends MCU the app's version number
        	else if (dataList.get(0).equals("V"))
        	{
        		String version;
        		version = (((RacerApplication)BinaryRacer.this.getApplication()).getVersion());
        		
        		dataS = (((RacerApplication)BinaryRacer.this.getApplication()).trackPos) + ",V," + version;
        		try {
					((RacerApplication)BinaryRacer.this.getApplication()).sendData(dataS);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		(((RacerApplication)BinaryRacer.this.getApplication()).newData) = false;
        	}
        	// Error Message; set label to error message
        	else if (dataList.get(0).equals("EM"))
        	{
        		label.setText(dataList.get(1));
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
            	// If new data is read in, parse the data
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
