package com.bKlen.binaryracer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DriverMeeting extends Activity
{
	
	private RadioGroup radioDiffGroup;
	private RadioButton diffButton;
	private RadioGroup radioLapsGroup;
	private RadioButton lapsButton;
	
	private Button startButton;
	public List<String> dataList = new ArrayList<String>();
	String dataS;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_meeting);
        
        addListenerOnButton();
        thread.start();
    }
    
    public void addListenerOnButton()
    {
    	radioDiffGroup = (RadioGroup) findViewById(R.id.radioDiffGroup);
    	radioLapsGroup = (RadioGroup) findViewById(R.id.radioLapsGroup);
    	startButton = (Button) findViewById(R.id.answerButton);
     
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
    		    
    		    ((RacerApplication)DriverMeeting.this.getApplication()).laps = Integer.toString(laps);
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
        	dataList = ((RacerApplication)DriverMeeting.this.getApplication()).dataList;
        	if (dataList.get(0).equals("MR") && dataList.get(1).equals("OK"))
        	{
        		(((RacerApplication)DriverMeeting.this.getApplication()).newData) = false;
        		Intent lapCounter = new Intent(getApplicationContext(), LapCounter.class);
        		startActivity(lapCounter);
        		thread.interrupt();
            	finish();
        	}
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
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}