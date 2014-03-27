package com.jonnygold.nxtcarcontroller;

import java.util.Observable;
import java.util.Observer;

import com.jonnygold.nxtcarcontroller.Controller.ConnectionState;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class ControllerActivity extends Activity implements SensorEventListener, Observer{

	private static final String TAG = "Activity";
	
	private static final int BT_REQUEST = 1;
	
	private Controller controller;
	
	private SensorManager sensorManager;
	private Sensor sensor;
	
	private SteeringWheel wheel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controller);
		
		this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
		if(sensor == null){
			Log.e(TAG, "Сенсор не поддерживается");
		}
		
		controller = new Controller(this);
		if(controller.getAdapter() != null && !controller.getAdapter().isEnabled()){
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, BT_REQUEST);
		}
		controller.addObserver(this);
		
		wheel = new SteeringWheel();
		
	}

	@Override
	protected void onStart() {
		super.onStart();
//		controller.connect();
		((PedalView)findViewById(R.id.view_accelerator_pedal)).setPedal(new Accelerator(controller));
		((PedalView)findViewById(R.id.view_break_pedal)).setPedal(new Brake(controller));
		((SelectorView)findViewById(R.id.view_ignition)).setSelector(new Ignition(controller));
		((SelectorView)findViewById(R.id.view_shifter)).setSelector(new Transmission(controller));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(controller.getState() == ConnectionState.CONNECTED){
			releaseWheel();
		}
	}
	
	@Override
	protected void onPause() {
	    super.onPause();
	    sensorManager.unregisterListener(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.controller, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_diconnect : 
			controller.disconnect();
			break;
		}
		return true;
	}
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() != Sensor.TYPE_GRAVITY)
            return;
		Command command = wheel.getTurn(event);
		if(command != Command.NOTHING){
			controller.sendCommand(command);
		}
		
//		float[] values = event.values;
//		Log.w(TAG, "x:" + values[0]);
//		Log.w(TAG, "y:" + values[1]);
//		Log.w(TAG, "z:" + values[2]);
//		if(values[1] < 0){
//			controller.sendCommand(Command.LEFT);
//		}
//		if(values[1] > 0){
//			controller.sendCommand(Command.RIGHT);
//		}
//		controller.sendCommand(Command.LEFT);
	}
	
	@Override
	public void update(Observable obs, Object data) {
		ConnectionState state = ((Controller)obs).getState();
		switch(state){
		case CONNECTED :
			releaseWheel();
			break;
		case DISCONNECTED :
			blockWheel();
			break;
		default :
			break;
		}
	}
	
	private void releaseWheel(){
		sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	private void blockWheel(){
		sensorManager.unregisterListener(this);
	}

	

}
