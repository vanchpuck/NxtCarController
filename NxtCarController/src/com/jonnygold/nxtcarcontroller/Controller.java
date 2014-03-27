package com.jonnygold.nxtcarcontroller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public final class Controller extends Observable{

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
	        	if(getState() == ConnectionState.SEARCHING){
	        		updateState(ConnectionState.DISCONNECTED);
	        	}
            }
	    }
	};
	
	private BluetoothAdapter btAdapter;
	
	private Context context;
	
	private volatile ConnectionState state;
	
	/*
	 * Inner classes
	 */
	
	/**
	 * Contains possible states of the connection to an NXT device.
	 * @author Vanchpuck
	 *
	 */
	public enum ConnectionState{
		DISCONNECTED,
		SEARCHING,
		CONNECTING,
		CONNECTED;
	}
	
	/**
	 * Provide low-level mechanism for communicating with NXT device.
	 * @author Vanchpuck
	 *
	 */
	private final class Connector {

		private static final String TAG = "Connector";
		
		private static final String UUID_STRING = "00001101-0000-1000-8000-00805F9B34FB";
				
				
		private DataOutputStream outStream;
		
		private ConnectThread connectThread;
		
		private class ConnectThread extends Thread{
			private BluetoothSocket socket;
//			private BluetoothDevice device;
			
			ConnectThread(BluetoothDevice device){
				try {
					socket = device.createRfcommSocketToServiceRecord(UUID.fromString(UUID_STRING));
				} catch (IOException e) {
					Log.e(TAG, "Unable to create bluetooth connection socket", e);
				}
//				this.device = device;
			}
			
			@Override
			public void run() {
				updateState(ConnectionState.CONNECTING);
				
				btAdapter.cancelDiscovery();
				
				try {
					socket.connect();
				} catch (IOException e) {
					Log.e(TAG, "Unable to establish the connection", e);
				}
				
				try {
					outStream = new DataOutputStream(socket.getOutputStream());
				} catch (IOException e) {
					Log.e(TAG, "Unable to create output stream", e);
				}
				
				updateState(ConnectionState.CONNECTED);
			}
			
			public void close(){
				try {
					socket.close();
				} catch (IOException e) {
					Log.w(TAG, "Unable to close connection socket", e);
				}
				updateState(ConnectionState.DISCONNECTED);
//				state = ConnectionState.DISCONNECTED;
			}
		}
				
		public void connect(BluetoothDevice device){
			if(connectThread != null){
				connectThread.close();
			}
			connectThread = new ConnectThread(device);
			connectThread.start();
		}
		
		public void disconnect(){
			connectThread.close();
		}
		
		public void sendSignal(int signal) throws IOException{
			outStream.write(signal);
			outStream.flush();
		}
				
	}
	
	/*
	 * Constructors
	 */
	
	public Controller(Context context) {
		this.context = context;
		this.btAdapter = BluetoothAdapter.getDefaultAdapter();
		this.connector = new Connector();
		updateState(ConnectionState.DISCONNECTED);
	}
	
	/*
	 * Methods
	 */
	
	public void connect(){
		if(state != ConnectionState.DISCONNECTED)
			return;
		
		updateState(ConnectionState.SEARCHING);
		
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
		// Send disconnect command to NXT
		sendCommand(Command.DISCONNECT);
		// Close the connection
		connector.disconnect();
	}
	
	public ConnectionState getState(){
		return state;
	}
	
	public BluetoothAdapter getAdapter(){
		return btAdapter;
	}
	
	public boolean sendCommand(Command command) {
		try {
			connector.sendSignal(command.getCode());
			return true;
		} catch (IOException e) {
			Log.w(TAG, "Command hasn't been sent.");
			updateState(ConnectionState.DISCONNECTED);
			e.printStackTrace();
		}
		return false;
	}
		
	private BluetoothDevice findBoundedNxt(){
    	Log.w(TAG, "Searching in bounded devices...");
    	for(BluetoothDevice boundedDevice : btAdapter.getBondedDevices() ){
    		Log.w(TAG, boundedDevice.getName());
    		if("NXT".equals(boundedDevice.getName())){
    			Log.w(TAG, "NTX has been found in bounded devices!");
    			return boundedDevice;
    		}
    	}
    	Log.w(TAG, "NTX has NOT been found in bounded devices!");
    	return null;
    }
	
	private void updateState(ConnectionState newState){
		if(newState == null){
			throw new NullPointerException("Connection state can't be null");
		}
		state = newState;
		setChanged();
		notifyObservers();
		clearChanged();
	}
	
}
