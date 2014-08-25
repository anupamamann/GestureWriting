package com.android.apps.mydrawing;

import java.io.Serializable;

import android.graphics.Path;


/**
 * Serialized Class for transferring Path object for a canvas drawing over the network
 * @author anupamam
 *
 */
public class PaintObject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6278748503477966696L;

	private Path drawingPath;
	
	private String description;

	public PaintObject(String descString){
		this.description =  descString;
	}
	public Path getDrawingPath() {
		return drawingPath;
	}

	public void setDrawingPath(Path drawingPath) {
		this.drawingPath = drawingPath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.description;
	}
	

}