package com.jonnygold.nxtcarcontroller;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class ControllerActivity extends Activity {

	private static final String TAG = "Activity";
	
	private static final int BT_REQUEST = 1;
	
	private Controller controller;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controller);
		
		controller = new Controller(this);
		if(controller.getAdapter() != null && !controller.getAdapter().isEnabled()){
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, BT_REQUEST);
		}
		
	}

	@Override
	protected void onStart() {
		super.onStart();
		controller.connect();
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

}
