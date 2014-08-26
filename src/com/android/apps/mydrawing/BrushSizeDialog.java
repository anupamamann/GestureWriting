package com.android.apps.mydrawing;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class BrushSizeDialog extends DialogFragment {
	ImageButton small, medium, large;
	
	public interface BrushSizeDialogListener {
		void passSize(DrawingCanvas.BRUSH_SIZE size);
	}
	
	public BrushSizeDialog() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.brush_size, container);
		getDialog().setTitle("Brush Size:");
		small = (ImageButton) v.findViewById(R.id.btnSmallBrush);
		medium = (ImageButton) v.findViewById(R.id.btnMediumBrush);
		large = (ImageButton) v.findViewById(R.id.btnLargeBrush);
		small.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BrushSizeDialogListener listener = (BrushSizeDialogListener) getActivity();
				listener.passSize(DrawingCanvas.BRUSH_SIZE.SMALL);
				dismiss();
			}
		});
		medium.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BrushSizeDialogListener listener = (BrushSizeDialogListener) getActivity();
				listener.passSize(DrawingCanvas.BRUSH_SIZE.MEDIUM);
				dismiss();
			}
		});
		large.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BrushSizeDialogListener listener = (BrushSizeDialogListener) getActivity();
				listener.passSize(DrawingCanvas.BRUSH_SIZE.LARGE);
				dismiss();
			}
		});
		return v;
	}

}
