package com.android.apps.mydrawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class onTouchCircle extends View {
	Paint paint;
	Path path;
	
	public onTouchCircle(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFocusable(true);
		setFocusableInTouchMode(true);
		 paint = new Paint();
			paint.setColor(Color.RED);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(5);
			path = new Path();
			
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawPath(path, paint);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			path.moveTo(event.getX(), event.getY());
			postInvalidate();
			return true;
		}else if(event.getAction() == MotionEvent.ACTION_MOVE){
			//path.addCircle(event.getX(), event.getY(), 5, Direction.CCW);
			path.lineTo(event.getX(), event.getY());
			postInvalidate();
			return true;
		}
		
		return super.onTouchEvent(event);
	}
	

}
