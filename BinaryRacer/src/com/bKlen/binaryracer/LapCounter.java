package com.bKlen.binaryracer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
	TextView label;
	TextView answerTextView;
	
	Button binZeroButton;
	Button binOneButton;
	Button backButton;
	Button answerButton;
	
	String laps;
	String answer;
	String trackPos;
	String dataS;
	public List<String> dataList = new ArrayList<String>();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lap_counter);
        
        youCountTextView = (TextView) findViewById(R.id.youCountTextView);
        oppCountTextView = (TextView) findViewById(R.id.oppCountTextView);
        numberToConvertTextView = (TextView) findViewById(R.id.numberTextView);
        label = (TextView) findViewById(R.id.label);
        answerTextView = (TextView) findViewById(R.id.answerTextView);
        
        binZeroButton = (Button) findViewById(R.id.binZeroButton);
        binOneButton = (Button) findViewById(R.id.binOneButton);
        backButton = (Button) findViewById(R.id.backButton);
        answerButton = (Button) findViewById(R.id.answerButton);
        
        laps = ((RacerApplication)LapCounter.this.getApplication()).laps;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
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
    			
    			if (answerTextView.getText().toString().equals(answer))
    			{
    				//label.setText("correct");
    				label.setText("");
    				answerButton.setEnabled(false);
    				binZeroButton.setEnabled(false);
            		binOneButton.setEnabled(false);
            		backButton.setEnabled(false);
    				String dataS = trackPos + ",C";
    				try {
						((RacerApplication)LapCounter.this.getApplication()).sendData(dataS);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    			else
    			{
    				label.setText("incorrect");
    			}
    		}
    	});
    	
    	backButton.setOnClickListener(new OnClickListener()
    	{
    		@Override
    		public void onClick(View v)
    		{
    			String editAnswer = answerTextView.getText().toString();
    			if (editAnswer.length() > 0)
    				editAnswer = editAnswer.substring(0, editAnswer.length() - 1);
    			answerTextView.setText(editAnswer);
    		}
    	});
    	
    	binZeroButton.setOnClickListener(new OnClickListener()
    	{
    		@Override
    		public void onClick(View v)
    		{
    			String editAnswer = answerTextView.getText().toString();
    			editAnswer = editAnswer + "0";
    			answerTextView.setText(editAnswer);
    		}
    	});
    	
    	binOneButton.setOnClickListener(new OnClickListener()
    	{
    		@Override
    		public void onClick(View v)
    		{
    			String editAnswer = answerTextView.getText().toString();
    			editAnswer = editAnswer + "1";
    			answerTextView.setText(editAnswer);
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
        		binZeroButton.setEnabled(true);
        		binOneButton.setEnabled(true);
        		backButton.setEnabled(true);
        		answerTextView.setText("");
        		label.setText("");
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
        	else if(dataList.get(0).equals("C"))
        	{
        		answerButton.setEnabled(false);
        		binZeroButton.setEnabled(false);
        		binOneButton.setEnabled(false);
        		backButton.setEnabled(false);
        		if (dataList.get(1).equals(trackPos))
        		{
        			//Toast.makeText(LapCounter.this, "ROUND WON", Toast.LENGTH_LONG).show();
        			label.setText("correct!");
        		}
        		else
        		{
        			//Toast.makeText(LapCounter.this, "ROUND LOST", Toast.LENGTH_LONG).show();
        			label.setText("Sorry, Your opponent answered correctly first!");
        		}
        	}
        	else if (dataList.get(0).equals("HB"))
        	{
        		dataS = (((RacerApplication)LapCounter.this.getApplication()).trackPos);
        		try {
					((RacerApplication)LapCounter.this.getApplication()).sendData(dataS);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		(((RacerApplication)LapCounter.this.getApplication()).newData) = false;
        	}
        	else if (dataList.get(0).equals("W"))
        	{
        		if (trackPos.equals(dataList.get(1)))
        			won(true);
        		else
        			won(false);
        		(((RacerApplication)LapCounter.this.getApplication()).newData) = false;
        	}
        	else if (dataList.get(0).equals("EM"))
        	{
        		label.setText(dataList.get(1));
        		(((RacerApplication)LapCounter.this.getApplication()).newData) = false;
        	}
        	else if(dataList.get(0).equals("RESTART"))
        	{
        		dataS = (((RacerApplication)LapCounter.this.getApplication()).trackPos) + ",R,OK";
        		try {
					((RacerApplication)LapCounter.this.getApplication()).sendData(dataS);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		(((RacerApplication)LapCounter.this.getApplication()).newData) = false;
        		Intent binaryRacer;
            	binaryRacer = new Intent(getApplicationContext(), BinaryRacer.class);
            	startActivity(binaryRacer);
            	thread.interrupt();
	        	finish();
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
    
    void won(boolean won)
    {
    	if (won)
    	{
    		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
 
			// set title
			alertDialogBuilder.setTitle("Game Over");
 
			// set dialog message
			alertDialogBuilder
				.setMessage("You Won!")
				.setCancelable(false)
				.setPositiveButton("Restart",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						
		            	dataS = trackPos + ",RESET";
		            	try {
							((RacerApplication)LapCounter.this.getApplication()).sendData(dataS);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            	
					}
				  });
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
    	}
    	else
    	{
    		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

    		// set title
    		alertDialogBuilder.setTitle("Game Over");

    		// set dialog message
    		alertDialogBuilder.setMessage("You Lost!").setCancelable(false);

    		// create alert dialog
    		AlertDialog alertDialog = alertDialogBuilder.create();

    		// show it
    		alertDialog.show();

    		// After some action
    		//alertDialog.dismiss();
    	}
    }
}