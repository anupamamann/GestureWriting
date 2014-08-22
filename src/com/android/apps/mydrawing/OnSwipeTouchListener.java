package com.android.apps.mydrawing;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/*
 
Usage:
 
  myView.setOnTouchListener(new OnSwipeTouchListener(this) {
    @Override
    public void onSwipeDown() {
      Toast.makeText(MainActivity.this, "Down", Toast.LENGTH_SHORT).show();
    }
  }
 
*/
public class OnSwipeTouchListener implements OnTouchListener {
 
    private GestureDetector gestureDetector;
    
    public OnSwipeTouchListener(Context c) {
      gestureDetector = new GestureDetector(c, new GestureListener());
    }
 
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }
 
    private final class GestureListener extends SimpleOnGestureListener {
 
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
 
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        
        // Determines the fling velocity and then fires the appropriate swipe event accordingly
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > 50) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeDown();
                        } else {
                            onSwipeUp();
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
        
        @Override
        public boolean onDoubleTap(MotionEvent e) {
        	onDTap();
        	return false;
        }
    }
    
    public void onDTap() {
    }
 
    public void onSwipeRight() {
    }
 
    public void onSwipeLeft() {
    }
 
    public void onSwipeUp() {
    }
 
    public void onSwipeDown() {
    }
}