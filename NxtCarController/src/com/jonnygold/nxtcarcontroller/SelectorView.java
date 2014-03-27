package com.jonnygold.nxtcarcontroller;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class SelectorView extends ImageView {

	private IsSelector selector;
	
	public SelectorView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setSelector(IsSelector selector){
		this.selector = selector;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = MotionEventCompat.getActionMasked(event);
		
		switch(action){
		case MotionEvent.ACTION_DOWN : 
			selector.shift();
			return true;
		default :
			return super.onTouchEvent(event);
		}
	}

}
