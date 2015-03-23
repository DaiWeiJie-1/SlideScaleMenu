package com.example.slidescalemenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;

public class MainActivity extends Activity {
	private SlideScaleMenu menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		menu = new SlideScaleMenu(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		//make sure whatever menu's children to do,it can invoke menu.dispatchTouchEvent;
		return menu.dispatchTouchEvent(ev);
	}

}
