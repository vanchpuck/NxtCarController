package com.jonnygold.nxtcarcontroller;

public class Ignition implements IsSelector {

	private Controller controller;
	
	public Ignition(Controller controller){
		this.controller = controller;
	}
	
	@Override
	public void shift() {
		switch(controller.getState()){
		case CONNECTED :
			controller.disconnect();
			break;
		case CONNECTING :
			controller.disconnect();
			break;
		case DISCONNECTED :
			controller.connect();
			break;
		default :
			break;
		}
	}

}
