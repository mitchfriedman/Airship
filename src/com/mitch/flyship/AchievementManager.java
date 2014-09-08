package com.mitch.flyship;

import java.util.ArrayList;
import java.util.List;

public class AchievementManager {
	
	private static final double SEVEN_MINUTES = 60*7;
	private static final double FIVE_MINUTES = 60*5;
	
	private GoogleAPIManager apiManager;
	private double elapsedTime;
	private double waterPercent;
	List<String> enemiesHit;
	
	boolean unlockedSevenMinutes = false;
	boolean unlockedRunningOnVapours = false;
	boolean runningOnVapours = false;
	double runningOnVapoursStartTime = 0;
	
	public AchievementManager(AirshipGame game)
	{
		this.apiManager = game.getGoogleAPIManager();
		resetRun();
	}
	
	public void resetRun()
	{
		elapsedTime = 0;
		waterPercent = 0;
		enemiesHit = new ArrayList<String>();
		runningOnVapours = false;
	}
	
	public void setElapsedTime(double elapsedTime)
	{
		this.elapsedTime = elapsedTime;
		
		if (!unlockedSevenMinutes && elapsedTime > SEVEN_MINUTES) {
			apiManager.unlockAchievement(R.string.achievement_seven_minutes_in_heaven);
			unlockedSevenMinutes = true;
		}
		
		if (!unlockedRunningOnVapours 
					&& runningOnVapours 
					&& elapsedTime - runningOnVapoursStartTime > FIVE_MINUTES) {
			apiManager.unlockAchievement(R.string.achievement_running_on_vapors);
			unlockedRunningOnVapours = true;
		}
	}
	
	public void setWaterPercent(double percent)
	{
		this.waterPercent = percent;
		
		if (waterPercent < 0.5 && !runningOnVapours) {
			runningOnVapours = true;
			runningOnVapoursStartTime = elapsedTime;
		} 
		else if (waterPercent >= 0.5 && runningOnVapours) {
			runningOnVapours = false;
		}
	}
	
	public void onDeath()
	{
		boolean onlyGullsHit = true;
		for (int i = 0; i < enemiesHit.size(); i++) {
			if (enemiesHit.get(i) != "bird") {
				onlyGullsHit = false;
				break;
			}
		}
		
		if (onlyGullsHit && enemiesHit.size() >= 10 ) {
			apiManager.unlockAchievement(R.string.achievement_laridae_laridead);
		}
	}
	
	public void onEnemyHit(String enemy)
	{
		enemiesHit.add(enemy);
	}
	
	public void onCrateBreak()
	{
		apiManager.incrementAchievement(R.string.achievement_box_buster, 1);
	}
	
	public void onCalibrate()
	{
		apiManager.incrementAchievement(R.string.achievement_never_satisfied, 1);
	}
}
