package com.mitch.framework.implementation;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build.VERSION;
import android.view.View;
import android.util.Log;

import com.mitch.framework.Input;

public class AndroidInput implements Input {    
    TouchHandler touchHandler;
    private float tiltX, tiltY;

    public AndroidInput(Context context, View view, float scaleX, float scaleY) {
        if(Integer.parseInt(VERSION.SDK) < 5) 
            touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
        else
            touchHandler = new MultiTouchHandler(view, scaleX, scaleY); 
        
        ((SensorManager) context.getSystemService(Context.SENSOR_SERVICE)).registerListener(
		new SensorEventListener() {    
			@Override  
			public void onSensorChanged(SensorEvent event) {  
				tiltX = event.values[0];
				tiltY = event.values[1]; 
			}
    		@Override  
    		public void onAccuracyChanged(Sensor sensor, int accuracy) { } //ignore this event
    	},
    	((SensorManager) context.getSystemService(Context.SENSOR_SERVICE))
    	.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0), SensorManager.SENSOR_DELAY_NORMAL);
    }
    
    public float GetTiltY() {
    	return tiltY;
    }
    public float GetTiltX() {
    	return tiltX;
    }
    @Override
    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }
    @Override
    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }
    @Override
    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }
    @Override
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }
}
