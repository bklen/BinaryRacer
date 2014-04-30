package com.bKlen.binaryracer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

public class RacerApplication extends Application
{
	BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;				
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;
    
    public String trackPos;				// Tablet track position (inner or outer)
    public String bluetoothDevice;		// Name of Bluetooth device the tablet will connect to
    public boolean newData;				// Boolean that signals if there is newData waiting to be read
    public List<String> dataList;		// ArrayList that holds parsed data read in from Bluetooth
    public boolean firstRestart;		// Boolean to keep track of restarts
    String[] fileData;					// String array to hold data read in from file

	@Override
    public void onCreate() 
    {
        super.onCreate();
        
        // Initialize variables
        trackPos = "";
        bluetoothDevice = "";
        newData = false;
        dataList = new ArrayList<String>();
        firstRestart = true;
        fileData = new String[2];

        // Read from file to get track position and Bluetooth device to connect too
        // then find the bluetooth device, connect to it and start listening for data
        readFromFile();
        findBT();
        try {
			openBT();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	/**
	   * Finds Specified Bluetooth device.
	   * @param args Unused.
	*/
	void findBT()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        {
            Log.d("bluetooth", "No bluetooth adapter available");
        }
        
        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBluetooth);
        }
        
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals(bluetoothDevice))
                {
                    mmDevice = device;
                    break;
                }
            }
        }
        Log.d("bluetooth", "Bluetooth Device Found");
    }
	
	/**
	   * Opens bluetooth connection and begins listening for data
	   * @param args Unused.
	   * @see IOException
	*/
	void openBT() throws IOException
    {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);        
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();
        
        beginListenForData();
        
        Log.d("bluetooth", "Bluetooth Opened");
        String dataS = trackPos + ",RESET";
        sendData(dataS);
    }
    
	/**
	   * Begins listening to data from connected Bluetooth device.
	   * Stores new data in dataList and sets newData to true when there is new data
	   * @param args Unused.
	*/
    void beginListenForData()
    {
        final Handler handler = new Handler(); 
        final byte delimiter = 10; //This is the ASCII code for a newline character
        
        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            @Override
			public void run()
            {                
               while(!Thread.currentThread().isInterrupted() && !stopWorker)
               {
                    try 
                    {
                        int bytesAvailable = mmInputStream.available();                        
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;
                                    
                                    handler.post(new Runnable()
                                    {
                                        @Override
										public void run()
                                        {
                                        	// Parses data and stores it in dataList
                                        	Log.d("bluetooth", data);
                                            dataList = Arrays.asList(data.split(","));
                                            newData = true;
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    } 
                    catch (IOException ex) 
                    {
                        stopWorker = true;
                    }
               }
            }
        });
        workerThread.start();
    }
    
    /**
	   * Sends data to the connected bluetooth device
	   * @param msg This is the the message to be sent.
	   * @see IOException
	*/
    void sendData(String msg) throws IOException
    {
        msg += "\n";
        mmOutputStream.write(msg.getBytes());
        Log.d("bluetooth", "Data Sent");
    }
    
    /**
	   * Closes Bluetooth connection
	   * @param args Unused.
	   * @see IOException
	*/
    void closeBT() throws IOException
    {
        stopWorker = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
        Log.d("bluetooth", "Bluetooth Closed");
    }

    /**
	   * Reads from file the track position of the tablet and what Bluetooth device to 
	   * connect too
	   * @param args Unused.
	*/
    public void readFromFile()
    {
        try {
        	final File file;
        	
    		file = new File(Environment.getExternalStorageDirectory()
    				.getAbsolutePath(), "BinaryRacer.txt");

        	FileInputStream inputStream = new FileInputStream(file);
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                
                int i = 0;
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    fileData[i++] = receiveString;
                }
                bluetoothDevice = fileData[0];
                trackPos = fileData[1];
                
                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }
    
    /**
	   * Gets the current version of the app
	   * @param args Unused.
	*/
    public String getVersion()
    {
        String v = "0";
        try {
            v = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            // Huh? Really?
        }
        return v;
    }
}
