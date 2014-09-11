package com.mitch.flyship.levelmanagers;

import java.util.ArrayList;
import java.util.List;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.GoogleAPIManager;
import com.mitch.flyship.R;

public class AchievementManager {
	
	private static final double SEVEN_MINUTES = 60*7;
	
	private GoogleAPIManager apiManager;
	private double waterPercent;
	List<String> enemiesHit;
	
	boolean unlockedSevenMinutes = false;
	
	public AchievementManager(AirshipGame game)
	{
		this.apiManager = game.getGoogleAPIManager();
		resetRun();
	}
	
	public void resetRun()
	{
		waterPercent = 0;
		enemiesHit = new ArrayList<String>();
	}
	
	public void setElapsedTime(double elapsedTime)
	{
		if (!unlockedSevenMinutes && elapsedTime > SEVEN_MINUTES) {
			apiManager.unlockAchievement(R.string.achievement_seven_minutes_in_heaven);
			unlockedSevenMinutes = true;
		}
	}
	
	public void onCollectWater()
	{
		if (waterPercent < 0.1) {
			apiManager.unlockAchievement(R.string.achievement_running_on_vapors);
		}
	}
	
	public void setWaterPercent(double percent)
	{
		this.waterPercent = percent;
	}
	
	public void onDeath()
	{
		
	}
	
	public void onEnemyHit(String enemy)
	{
		enemiesHit.add(enemy);
		
		boolean onlyGullsHit = true;
		for (int i = 0; i < enemiesHit.size(); i++) {
			if (enemiesHit.get(i).equals("bird")) {
				onlyGullsHit = false;
				break;
			}
		}
		
		if (onlyGullsHit && enemiesHit.size() >= 10 ) {
			apiManager.unlockAchievement(R.string.achievement_laridae_laridead);
		}
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
