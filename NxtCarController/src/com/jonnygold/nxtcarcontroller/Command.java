package com.jonnygold.nxtcarcontroller;

public enum Command {
	LEFT(0),
	RIGHT(1),
	DISCONNECT(255);
	
	private int code;
	
	private Command(int code){
		this.code = code;
	}
	
	public int getCode(){
		return this.code;
	}
}
