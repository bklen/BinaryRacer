package com.bKlen.binaryracer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
 
public class LapCounter extends Activity
{
	TextView scoreOne;
	int score = 0;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lap_counter);
        
        scoreOne = (TextView) findViewById(R.id.textView3);
        
        thread.start();
        //String blah = Convert.ToString(score);
        //scoreOne.setText(Integer.toString(score));
 
        
 
    }
    
    Handler h = new Handler();
    Runnable r=new Runnable()
    {
        public void run() 
        {
        	scoreOne.setText(Integer.toString(score++)); 			
        }
    };
    Thread thread = new Thread()
    {
        @Override
        public void run() {
            try {
                while(true) {
                    sleep(100);
                    h.post(r);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
}