package com.jonnygold.nxtcarcontroller;

public class Brake implements IsPedal {

	private Controller controller;
	
	public Brake(Controller controller){
		this.controller = controller;
	}
	
	@Override
	public void push() {
		controller.sendCommand(Command.BRAKE_PRESED);
	}

	@Override
	public void release() {
		controller.sendCommand(Command.NOTHING);
	}

}
