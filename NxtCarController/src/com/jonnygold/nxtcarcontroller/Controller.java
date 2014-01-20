package com.jonnygold.nxtcarcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class Controller {

	private static String TAG = "Controller";
	
	private Connector connector; 
	
	private final BroadcastReceiver receiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        // When discovery finds a device
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            // Get the BluetoothDevice object from the Intent
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            Log.w(TAG, "Something has been founded: "+device.getName());
	            if("NXT".equals(device.getName())){
	            	Log.w(TAG, "NTX has been found!!!");
		            btAdapter.cancelDiscovery();
		            connector.connect(device);
//		            
//					connect(device);
	            }
	        }
	        else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
	        	Log.w(TAG, "Searching completed");
            }
	    }
	};
	
	private BluetoothAdapter btAdapter;
	
	private Context context;
	
	public Controller(Context context) {
		this.context = context;
		this.btAdapter = BluetoothAdapter.getDefaultAdapter();
		this.connector = new Connector(btAdapter);
	}
	
	public void connect(){
		BluetoothDevice device = findBoundedNxt();
		if(device != null){
			connector.connect(device);
		}
		else{
			IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
			context.registerReceiver(receiver, filter);
			btAdapter.startDiscovery();
			Log.w(TAG, "Trying to find an NXT...");
		}
	}
	
	public void disconnect(){
		
	}
	
	public ConnectionState getState(){
		// TODO Make inheritance scheme for State
		return connector.getState();
	}
	
	public BluetoothAdapter getAdapter(){
		return btAdapter;
	}
	
	private BluetoothDevice findBoundedNxt(){
    	Log.w(TAG, "Searching in bounded devices...");
    	BluetoothDevice device = null;
    	for(BluetoothDevice boundedDevice : btAdapter.getBondedDevices() ){
    		Log.w(TAG, boundedDevice.getName());
    		if("NXT".equals(boundedDevice.getName())){
    			Log.w(TAG, "NTX has been found in bounded devices!");
    			return device;
    		}
    	}
    	Log.w(TAG, "NTX has NOT been found in bounded devices!");
    	return null;
    }
	
}
