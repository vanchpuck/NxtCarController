package com.jonnygold.nxtcarcontroller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

class Connector extends Observable {

	private static final String TAG = "Connector";
	
	protected static final String UUID_STRING = "00001101-0000-1000-8000-00805F9B34FB";
	
	
	private ConnectionState state;
	
	private BluetoothAdapter btAdapter;
	
	private DataOutputStream outStream;
	
	private ConnectThread connectThread;
	
	private class ConnectThread extends Thread{
		private BluetoothSocket socket;
//		private BluetoothDevice device;
		
		ConnectThread(BluetoothDevice device){
			try {
				socket = device.createRfcommSocketToServiceRecord(UUID.fromString(UUID_STRING));
			} catch (IOException e) {
				Log.e(TAG, "Unable to create bluetooth connection socket", e);
			}
//			this.device = device;
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
//			state = ConnectionState.CONNECTED;
		}
		
		public void close(){
			try {
				socket.close();
			} catch (IOException e) {
				Log.w(TAG, "Unable to close connection socket", e);
			}
			updateState(ConnectionState.DISCONNECTED);
//			state = ConnectionState.DISCONNECTED;
		}
	}
	
	public Connector(BluetoothAdapter adapter) {
		this.btAdapter = adapter;
		this.state = ConnectionState.DISCONNECTED;
	}
	
	public void connect(BluetoothDevice device){
		if(connectThread != null){
			connectThread.close();
		}
		connectThread = new ConnectThread(device);
		connectThread.start();
	}
	
	public void disconnect(){
		
	}
	
	public ConnectionState getState(){
		return state;
	}

	private void updateState(ConnectionState newState){
		setChanged();
		state = newState;
		notifyObservers(state);
		clearChanged();
	}
	
}
