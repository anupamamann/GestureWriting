package com.android.apps.mydrawing;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import com.android.apps.mydrawing.PaintObject.PathObject;

public class DrawingCanvas extends View {
	Paint paint;
	List<Pair<MySerializablePath, Pair<Integer, Integer>>> paths;
	MySerializablePath currentPath;
	//defaults
	int color = Color.GREEN;
	int brushSize = BRUSH_SIZE.MEDIUM.get();
	Bitmap bitmap;
	//Canvas newCanvas;
	Activity activity = null;
	
	enum BRUSH_SIZE {
		SMALL(10),
		MEDIUM(20),
		LARGE(30);
		
		int size;
		
		private BRUSH_SIZE(int i) {
			this.size = i;
		}
		
		public int get() {
			return size;
		}
	}

	public DrawingCanvas(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setStyle(Style.STROKE);
		currentPath = new MySerializablePath();
		paths = new ArrayList<Pair<MySerializablePath, Pair<Integer, Integer>>>();
		paths.add(Pair.create(currentPath, Pair.create(brushSize, color)));
		setFocusable(true); //needs this explicitly
		setFocusableInTouchMode(true);
		//newCanvas = new Canvas();
	}
	
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setBrushSize(int brushSize) {
		this.brushSize = brushSize;
	}
	
	public List<Pair<MySerializablePath, Pair<Integer, Integer>>> getPaths() {
		return paths;
	}
	
	public void setStyle(Style style) {
		paint.setStyle(style);
	}
	
	public void setBitmap(Bitmap bmp) {
		bitmap = bmp;
	}
	
	public Bitmap getBitMap() {
		return bitmap;
	}
	
	public void addToPaths(PathObject pathObj) {
		paths.add(Pair.create(pathObj.getPath(),
				Pair.create(pathObj.getBrushSize(), pathObj.getColor())));
		invalidate();
	}
	
	public void setActivity(Activity a) {
		activity = a;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (Pair<MySerializablePath, Pair<Integer, Integer>> path : paths) {
			Pair<Integer, Integer> values = path.second;
			paint.setStrokeWidth(values.first);
			paint.setColor(values.second);
			canvas.drawPath(path.first, paint);
		}
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// Defines the extra padding for the shape name text
	    int contentWidth = 500 + getSuggestedMinimumWidth();
	    // Try for a width based on our minimum
	    int minw = contentWidth + getPaddingLeft() + getPaddingRight();
	    int w = resolveSizeAndState(minw, widthMeasureSpec, 0);
	    // Ask for a height that would let the view get as big as it can
	    int minh = 1300 + getPaddingBottom() + getPaddingTop();
	    int h = resolveSizeAndState(minh, heightMeasureSpec, 0);
	    // Calling this method determines the measured width and height
	    setMeasuredDimension(w, h);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		MySerializablePath p = null;
		boolean change = false;
		if (color != paint.getColor()) {
			paint.setColor(color);
			change = true;
		}
		if (brushSize != paint.getStrokeWidth()) {			
			paint.setStrokeWidth(brushSize);
			change = true;
		}
		if (change) {
			p = new MySerializablePath();
			currentPath = p;
			paths.add(Pair.create(p, Pair.create(brushSize, color)));
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			currentPath.moveTo(event.getX(), event.getY());
			postInvalidate();
			return true;
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			currentPath.lineTo(event.getX(), event.getY());
			postInvalidate();
			return true;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (activity == null) {
				Log.e("Drawing", "activity is null");
			}
			else {
			((GestureActivity)activity).sendDrawing();
			}
			return true;
		}
		return false;
	}
	
	@Override
	protected Parcelable onSaveInstanceState() {
		return super.onSaveInstanceState();
	}
	
	public void clearCanvas() {
		currentPath = new MySerializablePath();
		paths = new ArrayList<Pair<MySerializablePath, Pair<Integer, Integer>>>();
		paths.add(Pair.create(currentPath, Pair.create(brushSize, color)));
		invalidate();
	}
	
	public void loadSavedDrawing(List<Pair<MySerializablePath, Pair<Integer, Integer>>> paths) {
		this.paths = paths;
		invalidate();
	}
	
}
