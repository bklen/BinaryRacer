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
	TextView convertTextView;
	
	Button zeroButton;
	Button oneButton;
	Button twoButton;
	Button threeButton;
	Button fourButton;
	Button fiveButton;
	Button sixButton;
	Button sevenButton;
	Button eightButton;
	Button nineButton;
	Button aButton;
	Button bButton;
	Button cButton;
	Button dButton;
	Button eButton;
	Button fButton;
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
        convertTextView = (TextView) findViewById(R.id.convertTextView);
        
        zeroButton = (Button) findViewById(R.id.zeroButton);
        oneButton = (Button) findViewById(R.id.oneButton);
        twoButton = (Button) findViewById(R.id.twoButton);
    	threeButton = (Button) findViewById(R.id.threeButton);
    	fourButton = (Button) findViewById(R.id.fourButton);
    	fiveButton = (Button) findViewById(R.id.fiveButton);
    	sixButton = (Button) findViewById(R.id.sixButton);
    	sevenButton = (Button) findViewById(R.id.sevenButton);
    	eightButton = (Button) findViewById(R.id.eightButton);
    	nineButton = (Button) findViewById(R.id.nineButton);
    	aButton = (Button) findViewById(R.id.aButton);
    	bButton = (Button) findViewById(R.id.bButton);
    	cButton = (Button) findViewById(R.id.cButton);
    	dButton = (Button) findViewById(R.id.dButton);
    	eButton = (Button) findViewById(R.id.eButton);
    	fButton = (Button) findViewById(R.id.fButton);
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
    				zeroButton.setEnabled(false);
            		oneButton.setEnabled(false);
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
    				label.setText("INCORRECT...");
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
    	
    	zeroButton.setOnClickListener(new OnClickListener()
    	{
    		@Override
    		public void onClick(View v)
    		{
    			String editAnswer = answerTextView.getText().toString();
    			editAnswer = editAnswer + "0";
    			answerTextView.setText(editAnswer);
    		}
    	});
    	
    	oneButton.setOnClickListener(new OnClickListener()
    	{
    		@Override
    		public void onClick(View v)
    		{
    			String editAnswer = answerTextView.getText().toString();
    			editAnswer = editAnswer + "1";
    			answerTextView.setText(editAnswer);
    		}
    	});
    	
    	twoButton.setOnClickListener(new OnClickListener()
    	{
    		@Override
    		public void onClick(View v)
    		{
    			String editAnswer = answerTextView.getText().toString();
    			editAnswer = editAnswer + "2";
    			answerTextView.setText(editAnswer);
    		}
    	});
    	
    	threeButton.setOnClickListener(new OnClickListener()
    	{
    		@Override
    		public void onClick(View v)
    		{
    			String editAnswer = answerTextView.getText().toString();
    			editAnswer = editAnswer + "3";
    			answerTextView.setText(editAnswer);
    		}
    	});
    	
    	fourButton.setOnClickListener(new OnClickListener()
    	{
    		@Override
    		public void onClick(View v)
    		{
    			String editAnswer = answerTextView.getText().toString();
    			editAnswer = editAnswer + "4";
    			answerTextView.setText(editAnswer);
    		}
    	});
    	
    	fiveButton.setOnClickListener(new OnClickListener()
    	{
    		@Override
    		public void onClick(View v)
    		{
    			String editAnswer = answerTextView.getText().toString();
    			editAnswer = editAnswer + "5";
    			answerTextView.setText(editAnswer);
    		}
    	});
    	
    	sixButton.setOnClickListener(new OnClickListener()
    	{
    		@Override
    		public void onClick(View v)
    		{
    			String editAnswer = answerTextView.getText().toString();
    			editAnswer = editAnswer + "6";
    			answerTextView.setText(editAnswer);
    		}
    	});
    	
    	sevenButton.setOnClickListener(new OnClickListener()
    	{
    		@Override
    		public void onClick(View v)
    		{
    			String editAnswer = answerTextView.getText().toString();
    			editAnswer = editAnswer + "7";
    			answerTextView.setText(editAnswer);
    		}
    	});
    	
    	eightButton.setOnClickListener(new OnClickListener()
    	{
    		@Override
    		public void onClick(View v)
    		{
    			String editAnswer = answerTextView.getText().toString();
    			editAnswer = editAnswer + "8";
    			answerTextView.setText(editAnswer);
    		}
    	});
    	
    	nineButton.setOnClickListener(new OnClickListener()
    	{
    		@Override
    		public void onClick(View v)
    		{
    			String editAnswer = answerTextView.getText().toString();
    			editAnswer = editAnswer + "9";
    			answerTextView.setText(editAnswer);
    		}
    	});
    	
    	aButton.setOnClickListener(new OnClickListener()
    	{
    		@Override
    		public void onClick(View v)
    		{
    			String editAnswer = answerTextView.getText().toString();
    			editAnswer = editAnswer + "A";
    			answerTextView.setText(editAnswer);
    		}
    	});
    	
    	bButton.setOnClickListener(new OnClickListener()
    	{
    		@Override
    		public void onClick(View v)
    		{
    			String editAnswer = answerTextView.getText().toString();
    			editAnswer = editAnswer + "B";
    			answerTextView.setText(editAnswer);
    		}
    	});
    	
    	cButton.setOnClickListener(new OnClickListener()
    	{
    		@Override
    		public void onClick(View v)
    		{
    			String editAnswer = answerTextView.getText().toString();
    			editAnswer = editAnswer + "C";
    			answerTextView.setText(editAnswer);
    		}
    	});
    	
    	dButton.setOnClickListener(new OnClickListener()
    	{
    		@Override
    		public void onClick(View v)
    		{
    			String editAnswer = answerTextView.getText().toString();
    			editAnswer = editAnswer + "D";
    			answerTextView.setText(editAnswer);
    		}
    	});
    	
    	eButton.setOnClickListener(new OnClickListener()
    	{
    		@Override
    		public void onClick(View v)
    		{
    			String editAnswer = answerTextView.getText().toString();
    			editAnswer = editAnswer + "E";
    			answerTextView.setText(editAnswer);
    		}
    	});
    	
    	fButton.setOnClickListener(new OnClickListener()
    	{
    		@Override
    		public void onClick(View v)
    		{
    			String editAnswer = answerTextView.getText().toString();
    			editAnswer = editAnswer + "F";
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
        		backButton.setEnabled(true);
        		answerTextView.setText("");
        		label.setText("");
        		String base;
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
				
				base = dataList.get(3);
				if (base.equals("B"))
				{
					convertTextView.setText("To Binary: ");
					zeroButton.setEnabled(true);
	        		oneButton.setEnabled(true);
					twoButton.setEnabled(false);
					threeButton.setEnabled(false);
					fourButton.setEnabled(false);
					fiveButton.setEnabled(false);
					sixButton.setEnabled(false);
					sevenButton.setEnabled(false);
					eightButton.setEnabled(false);
					nineButton.setEnabled(false);
					aButton.setEnabled(false);
					bButton.setEnabled(false);
					cButton.setEnabled(false);
					dButton.setEnabled(false);
					eButton.setEnabled(false);
					fButton.setEnabled(false);
				}
				else if (base.equals("D"))
				{
					convertTextView.setText("To Decimal: ");
					zeroButton.setEnabled(true);
	        		oneButton.setEnabled(true);
					twoButton.setEnabled(true);
					threeButton.setEnabled(true);
					fourButton.setEnabled(true);
					fiveButton.setEnabled(true);
					sixButton.setEnabled(true);
					sevenButton.setEnabled(true);
					eightButton.setEnabled(true);
					nineButton.setEnabled(true);
					aButton.setEnabled(false);
					bButton.setEnabled(false);
					cButton.setEnabled(false);
					dButton.setEnabled(false);
					eButton.setEnabled(false);
					fButton.setEnabled(false);
				}
				else if (base.equals("O"))
				{
					convertTextView.setText("To Octal: ");
					zeroButton.setEnabled(true);
	        		oneButton.setEnabled(true);
					twoButton.setEnabled(true);
					threeButton.setEnabled(true);
					fourButton.setEnabled(true);
					fiveButton.setEnabled(true);
					sixButton.setEnabled(true);
					sevenButton.setEnabled(true);
					eightButton.setEnabled(false);
					nineButton.setEnabled(false);
					aButton.setEnabled(false);
					bButton.setEnabled(false);
					cButton.setEnabled(false);
					dButton.setEnabled(false);
					eButton.setEnabled(false);
					fButton.setEnabled(false);
				}
				else if (base.equals("H"))
				{
					convertTextView.setText("To Hex: ");
					zeroButton.setEnabled(true);
	        		oneButton.setEnabled(true);
					twoButton.setEnabled(true);
					threeButton.setEnabled(true);
					fourButton.setEnabled(true);
					fiveButton.setEnabled(true);
					sixButton.setEnabled(true);
					sevenButton.setEnabled(true);
					eightButton.setEnabled(true);
					nineButton.setEnabled(true);
					aButton.setEnabled(true);
					bButton.setEnabled(true);
					cButton.setEnabled(true);
					dButton.setEnabled(true);
					eButton.setEnabled(true);
					fButton.setEnabled(true);
				}
				
				numberToConvertTextView.setText(dataList.get(4));
				answer = dataList.get(5);	
        	}
        	else if(dataList.get(0).equals("C"))
        	{
        		answerButton.setEnabled(false);
        		backButton.setEnabled(false);
        		zeroButton.setEnabled(false);
        		oneButton.setEnabled(false);
				twoButton.setEnabled(false);
				threeButton.setEnabled(false);
				fourButton.setEnabled(false);
				fiveButton.setEnabled(false);
				sixButton.setEnabled(false);
				sevenButton.setEnabled(false);
				eightButton.setEnabled(false);
				nineButton.setEnabled(false);
				aButton.setEnabled(false);
				bButton.setEnabled(false);
				cButton.setEnabled(false);
				dButton.setEnabled(false);
				eButton.setEnabled(false);
				fButton.setEnabled(false);
				
        		if (dataList.get(1).equals(trackPos))
        		{
        			//Toast.makeText(LapCounter.this, "ROUND WON", Toast.LENGTH_LONG).show();
        			label.setText("CORRECT!");
        		}
        		else
        		{
        			//Toast.makeText(LapCounter.this, "ROUND LOST", Toast.LENGTH_LONG).show();
        			String text = "Your opponent answered first. The correct answer is: " + "\n" + answer; 
        			label.setText(text);
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
        		(((RacerApplication)LapCounter.this.getApplication()).firstRestart) = false;
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