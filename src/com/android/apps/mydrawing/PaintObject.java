package com.android.apps.mydrawing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.util.Pair;

/**
 * Serialized Class for transferring Path object for a canvas drawing over the network
 *
 */
public class PaintObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6278748503477966696L;
	
	class PathObject implements Serializable {

		private static final long serialVersionUID = 1L;
		MySerializablePath path;
		int brushSize;
		int color;
		
		public PathObject(MySerializablePath path, Integer brushSize, Integer color) {
			this.path = path;
			this.brushSize = brushSize;
			this.color = color;
		}
		
		public MySerializablePath getPath() {
			return path;
		}
		
	}

	List<PathObject> pathO;
	private String description;

	public PaintObject(String descString, List<Pair<MySerializablePath, Pair<Integer, Integer>>> paths){
		this.description =  descString;
		this.pathO = new ArrayList<PathObject>();
		for (Pair<MySerializablePath, Pair<Integer, Integer>> path : paths) {
			PathObject temp = new PathObject(path.first, path.second.first, path.second.second);
			pathO.add(temp);
		}
	}
	
	public PaintObject (String desc) {
		this.description = desc;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return this.description;
	}
	
	public List<PathObject> getPaths() {
		return pathO;
	}
	

}