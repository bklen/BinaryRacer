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

public class DriverMeetingWait extends Activity
{
	String dataS;					// Data to be sent too Bluetooth device(MCU)
	public List<String> dataList; 	// ArrayList that holds parsed data read in from Bluetooth
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_meeting_wait);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        // Initialize variables
        dataS = "";
        dataList = new ArrayList<String>();
        
        // Start thread to start reading and writing to the MCU over Bluetooth
        thread.start();
    }

    Handler h = new Handler();
    Runnable r=new Runnable()
    {
        public void run() 
        {
        	// Stores read in data in dataR then processes the data
        	dataList = ((RacerApplication)DriverMeetingWait.this.getApplication()).dataList;
        	
        	// Closes the activity, stops the thread, and navigates to the lap counter screen
        	if (dataList.get(0).equals("MR") && dataList.get(1).equals("OK"))
        	{
        		(((RacerApplication)DriverMeetingWait.this.getApplication()).newData) = false;
        		Intent lapCounter = new Intent(getApplicationContext(), LapCounter.class);
        		startActivity(lapCounter);
        		thread.interrupt();
            	finish();
        	}
        	// Sends MCU <T> as a heart beat
        	else if (dataList.get(0).equals("HB"))
        	{
        		dataS = (((RacerApplication)DriverMeetingWait.this.getApplication()).trackPos);
        		try {
					((RacerApplication)DriverMeetingWait.this.getApplication()).sendData(dataS);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		(((RacerApplication)DriverMeetingWait.this.getApplication()).newData) = false;
        	}
        	else
        	{
        		Log.d("MCU stream", "invalid message from MCU:" + dataList.toString());
        		(((RacerApplication)DriverMeetingWait.this.getApplication()).newData) = false;
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
            	if ((((RacerApplication)DriverMeetingWait.this.getApplication()).newData) == true)
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