package com.jonnygold.nxtcarcontroller;

public class Transmission implements IsSelector {

//	public enum State{
//		FORWARD,
//		BACKWARD
//	}
	
//	private State currentState = State.FORWARD;
	
	private Controller controller;
	
	public Transmission(Controller controller){
		this.controller = controller;
	}

	@Override
	public void shift() {
		controller.sendCommand(Command.CHANGE_DIRECTION);		
	}
	
}
