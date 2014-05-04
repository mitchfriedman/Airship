package com.mitch.framework.implementation;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.view.Window;
import android.view.WindowManager;

import com.mitch.framework.Audio;
import com.mitch.framework.FileIO;
import com.mitch.framework.Game;
import com.mitch.framework.Graphics;
import com.mitch.framework.Input;
import com.mitch.framework.Screen;

public abstract class AndroidGame extends Activity implements Game {
    AndroidFastRenderView renderView;
    Graphics graphics;
    Audio audio;
    Input input;
    FileIO fileIO;
    Screen screen;
    WakeLock wakeLock;
	private static final String PREFS_NAME = "AirshipHighScores";
	private ArrayList<String> highScores  = new ArrayList<String>();
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        //boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        //int frameBufferWidth = isPortrait ? 480: 800;
        //int frameBufferHeight = isPortrait ? 800: 480;
        int frameBufferWidth  = 520;
        int frameBufferHeight = 880;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
                frameBufferHeight, Config.RGB_565);
        
        float scaleX = (float) frameBufferWidth
                / getWindowManager().getDefaultDisplay().getWidth();
        float scaleY = (float) frameBufferHeight
                / getWindowManager().getDefaultDisplay().getHeight();

        renderView = new AndroidFastRenderView(this, frameBuffer);
        graphics = new AndroidGraphics(getAssets(), frameBuffer);
        fileIO = new AndroidFileIO(this);
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, renderView, scaleX, scaleY);
        screen = getInitScreen();
        setContentView(renderView);
        
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MyGame");
        
        prefs  = getPreferences(MODE_PRIVATE); 
        editor = getPreferences(MODE_PRIVATE).edit();
    }
    //public void 
    @Override
    public void onResume() {
        super.onResume();
        wakeLock.acquire();
        screen.resume();
        renderView.resume();
    }
    
    @SuppressWarnings("rawtypes")
	public ArrayList GetHighScores() {
    	if(highScores.size()==0) {	
	    	String Score1 = prefs.getString("score1",null);
	    	
	    	if(Score1 == null) {
	    		SetHighScoresZero();
	    		Score1 = prefs.getString("score1",null);
	    	}
	    	
	    	String Score2 = prefs.getString("score2",null);
	    	String Score3 = prefs.getString("score3",null);
	    	String Score4 = prefs.getString("score4",null);
	    	String Score5 = prefs.getString("score5",null);
	    	highScores.add(Score1);
	    	highScores.add(Score2);
	    	highScores.add(Score3);
	    	highScores.add(Score4);
	    	highScores.add(Score5);
    	}
    	return highScores;
    }
    
    @SuppressWarnings("rawtypes")
	public ArrayList GetGearHighScores() {
    	ArrayList<Integer> gearHighScores = new ArrayList<Integer>();
    	ArrayList<String>  highScore = GetHighScores();
    	for(int i=0;i<highScore.size();i++) {
    		String score = highScore.get(i);
    		for(int j=0;j<5;j++) {
    			if(score.charAt(j) == '_') {
    				gearHighScores.add(Integer.parseInt(score.substring(0,j)));
    				break;
    			}
    		}
    	}    	
    	return gearHighScores;
    }
    public ArrayList GetTimeHighScores() {
    	ArrayList<String> timeHighScores  = new ArrayList<String>();
    	ArrayList<String>  highScore      = GetHighScores();
    	for(int i=0;i<highScore.size();i++) {
    		String score = highScore.get(i);
    		for(int j=0;j<5;j++) {
    			if(score.charAt(j) == '_') {
    				timeHighScores.add(score.substring(j+1,score.length()));
    				break;
    			}
    		}
    	}    	
    	return timeHighScores;
    }
	public void SaveHighScore(int gearScore, String timeScore) {
    	ArrayList<Integer> gearScores = GetGearHighScores();
    	ArrayList<String>  timeScores = GetTimeHighScores();
    	if(gearScores.get(0) == null) {
    		SetHighScoresZero();
    		gearScores = GetGearHighScores();
    		timeScores = GetTimeHighScores();
    	}
    	
    	Collections.sort(gearScores, Collections.reverseOrder());
    	Collections.sort(timeScores, Collections.reverseOrder());
    	for(int i=0;i<5;i++) {
    		if(gearScore == gearScores.get(i)) {
    			int index = timeScores.get(i).indexOf(':');
    			int oldMins  = Integer.parseInt(timeScores.get(i).substring(0,index));
    			int	oldSecs  = Integer.parseInt(timeScores.get(i).substring(index+1,timeScores.get(i).length()));
    			int indexOfColon2 = timeScore.indexOf(':');
    			int newMins  = Integer.parseInt(timeScore.substring(0,indexOfColon2));
    			int newSecs  = Integer.parseInt(timeScore.substring(indexOfColon2+1,timeScore.length()));
    			
    			if(newMins == oldMins) {
    				if(newSecs > oldSecs) {
    					gearScores.add(i,gearScore);
    	    			timeScores.add(i,timeScore);
    	    			break;
    				}
    			}
    			else if(newMins > oldMins) {
    				gearScores.add(i, gearScore);
	    			timeScores.add(i, timeScore);
    			}
    			
    		}
    		else if(gearScore > gearScores.get(i)) {
    			gearScores.add(i, gearScore);
    			timeScores.add(i, timeScore);
    			break;
    			
    		}
    	}
    	SaveTop5(gearScores, timeScores);
	}
	public void SaveTop5(ArrayList<Integer> gearScores, ArrayList<String> times) {
		//Log.w("LOG","gears:"+gearScores+" time:"+times);
		String score1 = Integer.toString(gearScores.get(0)) + "_" +  times.get(0);
		String score2 = Integer.toString(gearScores.get(1)) + "_" +  times.get(1);
		String score3 = Integer.toString(gearScores.get(2)) + "_" +  times.get(2);
		String score4 = Integer.toString(gearScores.get(3)) + "_" +  times.get(3);
		String score5 = Integer.toString(gearScores.get(4)) + "_" +  times.get(4);
		
		editor.putString("score1", score1);
		editor.putString("score2", score2);
		editor.putString("score3", score3);
		editor.putString("score4", score4);
		editor.putString("score5", score5);
		
		editor.commit();
	}
	public void SetHighScoresZero() {
		editor.putString("score1","0_00:00");
		editor.putString("score2","0_00:00");
		editor.putString("score3","0_00:00");
		editor.putString("score4","0_00:00");
		editor.putString("score5","0_00:00");
		editor.commit();
	}
    
    @Override
    public void onPause() {
        super.onPause();
        wakeLock.release();
        renderView.pause();
        screen.pause();
        
        if (isFinishing())
            screen.dispose();
    }

    @Override
    public Input getInput() {
        return input;
    }
    public void Vibrate(int time) {
    	Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    	v.vibrate(time);
    }
    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public Audio getAudio() {
    	
        return audio;
    }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }
    
    public Screen getCurrentScreen() {
    	return screen;
    }
}