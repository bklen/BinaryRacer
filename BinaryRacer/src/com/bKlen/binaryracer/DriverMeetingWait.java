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
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DriverMeetingWait extends Activity
{
	
	String dataR="";
	String dataS="";
	public List<String> dataList = new ArrayList<String>();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_meeting_wait);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        thread.start();
    }

    Handler h = new Handler();
    Runnable r=new Runnable()
    {
        public void run() 
        {
        	dataList = ((RacerApplication)DriverMeetingWait.this.getApplication()).dataList;
        	if (dataList.get(0).equals("MR") && dataList.get(1).equals("OK"))
        	{
        		(((RacerApplication)DriverMeetingWait.this.getApplication()).newData) = false;
        		Intent lapCounter = new Intent(getApplicationContext(), LapCounter.class);
        		startActivity(lapCounter);
        		thread.interrupt();
            	finish();
        	}
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
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}