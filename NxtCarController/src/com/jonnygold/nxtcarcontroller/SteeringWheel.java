package com.jonnygold.nxtcarcontroller;

import android.hardware.SensorEvent;

public class SteeringWheel {

	private static final String TAG = "SteeringWheel";
	
	private static final float LEFT_THRESHOLD = -3; //TODO set threshold value
	
	private static final float RIGHT_THRESHOLD = 3; //TODO set threshold value
	
	
	private Command previous;
	
	public SteeringWheel(){
		previous = Command.NOTHING;
	}
	
	public Command getTurn(SensorEvent event){
		float y = event.values[1];
		
		Command command = null;
		
		if(y > LEFT_THRESHOLD && y < RIGHT_THRESHOLD){
			command = Command.DIRECT;
		}	
		else if(y < LEFT_THRESHOLD){
			command = Command.LEFT;
		}
		else if(y > RIGHT_THRESHOLD){
			command = Command.RIGHT;
		}	
		else{
			command = Command.NOTHING;
		}
		if(command != previous){
			previous = command;
			return command;
		}
		return Command.NOTHING;
		
	}
	
}
