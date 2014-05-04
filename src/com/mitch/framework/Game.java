package com.mitch.framework;

import java.util.ArrayList;

public interface Game {

	public Audio getAudio();

    public Input getInput();

    public FileIO getFileIO();

    public Graphics getGraphics();

    public void setScreen(Screen screen);

    public Screen getCurrentScreen();

    public Screen getInitScreen();

	public void Vibrate(int i);
	
	public void SaveTop5(ArrayList<Integer> gearScores, ArrayList<String> times);
    public void SetHighScoresZero();
	public ArrayList GetHighScores();
	public void SaveHighScore(int i, String string);
	public ArrayList GetGearHighScores();
	public ArrayList GetTimeHighScores();
}
