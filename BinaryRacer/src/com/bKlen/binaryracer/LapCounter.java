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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
 
public class LapCounter extends Activity
{
	TextView youCountTextView;
	TextView oppCountTextView;
	TextView numberToConvertTextView;
	EditText answerBox;
	Button answerButton;
	
	String answer;
	String trackPos;
	public List<String> dataList = new ArrayList<String>();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lap_counter);
        
        youCountTextView = (TextView) findViewById(R.id.youCountTextView);
        oppCountTextView = (TextView) findViewById(R.id.oppCountTextView);
        numberToConvertTextView = (TextView) findViewById(R.id.numberTextView);
        answerBox = (EditText) findViewById(R.id.answerEditText);
        answerButton = (Button) findViewById(R.id.answerButton);
        
        addListenerOnButton();
        trackPos= ((RacerApplication)LapCounter.this.getApplication()).trackPos;
        thread.start();
        
    }
    
    public void addListenerOnButton()
    {    
    	answerButton.setOnClickListener(new OnClickListener()
    	{
    		@Override
    		public void onClick(View v)
    		{
    			if (answerBox.getText().toString().equals(answer))
    			{
    				String dataS = trackPos + ",C";
    				try {
						((RacerApplication)LapCounter.this.getApplication()).sendData(dataS);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    		}
    	});  
    }
    
    Handler h = new Handler();
    Runnable r=new Runnable()
    {
        public void run() 
        {
        	(((RacerApplication)LapCounter.this.getApplication()).newData) = false;
        	dataList = ((RacerApplication)LapCounter.this.getApplication()).dataList;
        	if (dataList.get(0).equals("Q"))
        	{
        		answerButton.setEnabled(true);
        		String dataS = trackPos + ",Q,OK";
				try {
					((RacerApplication)LapCounter.this.getApplication()).sendData(dataS);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (trackPos.equals("O"))
				{
					youCountTextView.setText(dataList.get(2));
					oppCountTextView.setText(dataList.get(1));
				}
				else
				{
					youCountTextView.setText(dataList.get(1));
					oppCountTextView.setText(dataList.get(2));
				}
				
				//TODO: set binary input restriction
				
				numberToConvertTextView.setText(dataList.get(4));
				answer = dataList.get(5);	
        	}
        	else if(dataList.get(1).equals("C"))
        	{
        		answerButton.setEnabled(false);
        		if (dataList.get(0).equals(trackPos))
        		{
        			Toast.makeText(LapCounter.this, "ROUND WON", Toast.LENGTH_LONG).show();
        		}
        		else
        		{
        			Toast.makeText(LapCounter.this, "ROUND LOST", Toast.LENGTH_LONG).show();
        		}
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
            	if ((((RacerApplication)LapCounter.this.getApplication()).newData) == true)
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
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_restart:
                //newGame();
            	Intent binaryRacer = new Intent(getApplicationContext(), BinaryRacer.class);
            	startActivity(binaryRacer);
            	finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}