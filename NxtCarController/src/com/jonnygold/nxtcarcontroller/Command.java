package com.jonnygold.nxtcarcontroller;

public enum Command {
	LEFT(10),
	RIGHT(11),
	DIRECT(12),
	
//	LEFT_SMALL_PRESSED(1),
//	LEFT_SMALL_RELEASED(2),
//	LEFT_FULL_PRESSED(3),
//	LEFT_FULL_RELEASED(4),
//	
//	RIGHT_SMALL_PRESSED(10),
//	RIGHT_SMALL_RELEASED(11),
//	RIGHT_FULL_PRESSED(12),
//	RIGHT_FULL_RELEASED(13),
	
	ACCELERATOR_PRESSED(20),
	ACCELERATOR_RELEASED(21),
	
	BRAKE_PRESED(30),
	
	CHANGE_DIRECTION(40),
	
	FORWARD_PRESSED(20),
	FORWARD_RELEASED(21),
	
	BACKWARD_PRESSED(30),
	BACKWARD_RELEASED(31),
	
	STOP_PRESSED(40),
	STOP_RELEASED(41),
	
	DISCONNECT(50),
	
	NOTHING(255);
	
	private int code;
	
	private Command(int code){
		this.code = code;
	}
	
	public int getCode(){
		return this.code;
	}
}
