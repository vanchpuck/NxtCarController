package com.jonnygold.nxtcarcontroller;

public class Accelerator implements IsPedal{

	private Controller controller;
	
	public Accelerator(Controller controller){
		this.controller = controller;
	}
	
	public void push(){
		controller.sendCommand(Command.ACCELERATOR_PRESSED);
	}
	
	public void release(){
		controller.sendCommand(Command.ACCELERATOR_RELEASED);
	}

}
