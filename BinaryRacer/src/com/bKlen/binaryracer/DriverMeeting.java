package com.bKlen.binaryracer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
 
public class DriverMeeting extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_meeting);
        
        Button startButton = (Button) findViewById(R.id.startRaceButton);
 
        startButton.setOnClickListener(new View.OnClickListener() {
        	 
            public void onClick(View arg0)
            {
                //Starting a new Intentt
                //Intent lapCounter = new Intent(getApplicationContext(), LapCounter.class);
            	Intent lapCounter = new Intent(getApplicationContext(), LapCounter.class);
 
                //Sending data to another Activity
                //nextScreen.putExtra("name", inputName.getText().toString());
                //nextScreen.putExtra("email", inputEmail.getText().toString());
 
                //Log.e("n", inputName.getText()+"."+ inputEmail.getText());
 
                //startActivity(lapCounter);
            	startActivity(lapCounter);
 
            }
        });
 
    }
    
    @Override
    public void onBackPressed() {
    	try {
			this.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}