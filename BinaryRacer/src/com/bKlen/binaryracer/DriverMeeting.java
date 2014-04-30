package com.bKlen.binaryracer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class DriverMeeting extends Activity
{
	
	private RadioGroup radioDiffGroup;	// Difficulty radio button group
	private RadioButton diffButton;		// Difficulty radio button
	private RadioGroup radioLapsGroup;	// Number of laps radio button group
	private RadioButton lapsButton;		// Number of laps radio button
	
	private Button startButton;			// Start Button
	public List<String> dataList;		// ArrayList that holds parsed data read in from Bluetooth
	String dataS;						// Data to be sent too Bluetooth device(MCU)
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_meeting);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        // Initialize variables
        dataList = new ArrayList<String>();
        dataS = "";
        
        // Add button listener and start thread to listen for and send data to MCU via Bluetooth
        addListenerOnButton();
        thread.start();
    }
    
    public void addListenerOnButton()
    {
    	radioDiffGroup = (RadioGroup) findViewById(R.id.radioDiffGroup);
    	radioLapsGroup = (RadioGroup) findViewById(R.id.radioLapsGroup);
    	startButton = (Button) findViewById(R.id.answerButton);
     
    	// Sets difficulty, number of laps, and sends this data to the MCU
    	startButton.setOnClickListener(new OnClickListener()
    	{
    		@Override
    		public void onClick(View v)
    		{
    			// get selected radio button from radioGroup
    			int diffID = radioDiffGroup.getCheckedRadioButtonId();
    			int lapsID = radioLapsGroup.getCheckedRadioButtonId();
     
    			// find the radiobutton by returned id
    		    diffButton = (RadioButton) findViewById(diffID);
    		    lapsButton = (RadioButton) findViewById(lapsID);
    		    
    		    // Get difficulty and number of laps
    		    int diff, laps = 0;
    		    if (diffButton.getText().equals("Gold"))
    		    	diff = 3;
    		    else if (diffButton.getText().equals("Silver"))
    		    	diff = 2;
    		    else
    		    	diff = 1;
    		    if (lapsButton.getText().equals("5 Laps"))
    		    	laps = 5;
    		    else if (lapsButton.getText().equals("10 Laps"))
    		    	laps = 10;
    		    else
    		    	laps = 15;
    		    
    		    // Send difficulty and number of laps to MCU
    		    String dataS = "O,MR," + diff + "," + laps;
    		    try {
					((RacerApplication)DriverMeeting.this.getApplication()).sendData(dataS);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	});  
    }
    
    Handler h = new Handler();
    Runnable r=new Runnable()
    {
        public void run() 
        {
        	// Stores read in data in dataR then processes the data
        	dataList = ((RacerApplication)DriverMeeting.this.getApplication()).dataList;
        	
        	// Closes the activity, stops the thread, and navigates to the lap counter screen
        	if (dataList.get(0).equals("MR") && dataList.get(1).equals("OK"))
        	{
        		(((RacerApplication)DriverMeeting.this.getApplication()).newData) = false;
        		Intent lapCounter = new Intent(getApplicationContext(), LapCounter.class);
        		startActivity(lapCounter);
        		thread.interrupt();
            	finish();
        	}
        	// Sends MCU <T> as a heart beat
        	else if (dataList.get(0).equals("HB"))
        	{
        		dataS = (((RacerApplication)DriverMeeting.this.getApplication()).trackPos);
        		try {
					((RacerApplication)DriverMeeting.this.getApplication()).sendData(dataS);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		(((RacerApplication)DriverMeeting.this.getApplication()).newData) = false;
        	}
        	else
        	{
        		Log.d("MCU stream", "invalid message from MCU:" + dataList.toString());
        		(((RacerApplication)DriverMeeting.this.getApplication()).newData) = false;
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
            	if ((((RacerApplication)DriverMeeting.this.getApplication()).newData) == true)
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