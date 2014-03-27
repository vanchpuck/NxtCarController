package com.jonnygold.nxtcarcontroller;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class PedalView extends ImageView {

	private IsPedal pedal;
	
	public PedalView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setPedal(IsPedal pedal){
		this.pedal = pedal;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int action = MotionEventCompat.getActionMasked(event);
		
		switch(action){
		case MotionEvent.ACTION_DOWN : 
			pedal.push();
			return true;
		case MotionEvent.ACTION_UP :
			pedal.release();
			return true;
		case MotionEvent.ACTION_CANCEL :
			pedal.release();
			return true;
		default :
			return super.onTouchEvent(event);
		}
	}

}
