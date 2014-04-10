package com.jonnygold.nxtcarcontroller;

public class Transmission implements IsSelector {

	public enum State{
		FORWARD (Command.FORWARD),
		BACKWARD (Command.BACWARD);
		
		public final Command command;
		
		private State(Command command){
			this.command = command;
		}
	}
	
//	private State currentState = State.FORWARD;
	
	private Controller controller;
	
	private Command lastCommand;
	
	public Transmission(Controller controller){
		this.controller = controller;
		this.lastCommand = Command.FORWARD;
	}

	@Override
	public void shift() {
		switch (lastCommand) {
		case FORWARD:
			controller.sendCommand(Command.BACWARD);
			lastCommand = Command.BACWARD;
			return;
		default:
			controller.sendCommand(Command.FORWARD);
			lastCommand = Command.FORWARD;
			return;
		}
	}
	
}
