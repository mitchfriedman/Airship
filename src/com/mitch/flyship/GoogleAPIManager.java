package com.mitch.flyship;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.mitch.framework.implementation.AndroidGame;

public class GoogleAPIManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final int CONNECTION_RESOLUTION_REQUEST_CODE = 511010;
    public static final int REQUEST_LEADERBOARD = 500;

    List<LeaderboardScore> leaderboardScores;
    GoogleApiClient client;
    AndroidGame game;

    public GoogleAPIManager(AndroidGame game)
    {
        this.game = game;
        client = buildClient(game);
        leaderboardScores = new ArrayList<LeaderboardScore>();
    }

    public GoogleApiClient buildClient(AndroidGame androidGame) {
        return new GoogleApiClient.Builder(androidGame)
                .addApi(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .setGravityForPopups(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void suspendConnection()
    {
        client.disconnect();
    }

    public void connect()
    {
        if (!client.isConnected() && !client.isConnecting()) {
            client.connect();
        }
    }

    public boolean isConnected()
    {
        return client.isConnected();
    }

    void tryPushScores()
    {
        if (client.isConnected()) {
            Log.d("Google Api Manager", "Pushing Scores");
            for (LeaderboardScore score : leaderboardScores) {
                Games.Leaderboards.submitScore(client, score.getLeaderboard(), score.getScore());
                /*game.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(client,
                        score.getLeaderboard()), REQUEST_LEADERBOARD);*/
            }
            leaderboardScores.clear();

        } else if (!client.isConnecting()) {
            connect();
        }

    }

    public void pushLeaderboardScore(String leaderboardID, long score)
    {
        leaderboardScores.add(new LeaderboardScore(leaderboardID, score));
        tryPushScores();
    }
    
    public void loadBoards()
    {
    	if (client.isConnected()) {
    		game.startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(client), REQUEST_LEADERBOARD);
    	} else {
    		client.connect();
    	}
    }
    
    public void loadBoard(String leaderboard)
    {
    	if (client.isConnected()) {
    		game.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(client,
    	        leaderboard), REQUEST_LEADERBOARD);
    	} else {
    		client.connect();
    	}

    }
    
    public void unlockAchievement(String id)
    {
    	Games.Achievements.unlock(client, id);
    }
    
    public void incrementAchievement(String id, int value)
    {
    	Games.Achievements.increment(client, id, value);
    }
    
    public void unlockAchievement(int id)
    {
    	Games.Achievements.unlock(client, game.getString(id));
    }
    
    public void incrementAchievement(int id, int value)
    {
    	if (isConnected()) {
    		/*Games.Achievements.incrementImmediate(client, game.getString(id), value).setResultCallback(new ResultCallback<Achievements.UpdateAchievementResult>() {
    			
    			@Override
    			public void onResult(UpdateAchievementResult arg0) {
    				Log.d(arg0.getAchievementId(), arg0.getStatus().toString());
    			}
    		});*/
    		Games.Achievements.increment(client, game.getString(id), value);
    	}
    	
    	
    	/*Games.Achievements.increment(client, game.getString(id), value);
    	Log.d("ACHIEVEMENT INCREMENTED!", value + ", bam");*/
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(game, CONNECTION_RESOLUTION_REQUEST_CODE);
            } catch(Exception e) {
                connect();
            }
        } else {
            Log.d("Google Api Manager", "No resolution for failed connection.");
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d("Google Api Manager", "Connected to Google Play.");
        tryPushScores();

    }

    @Override
    public void onConnectionSuspended(int cause) {
        if (cause == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
            connect();
        }
    }


    public class LeaderboardScore
    {

        String leaderboard;
        long score;

        public LeaderboardScore(String leaderboard, long score)
        {
            this.leaderboard = leaderboard;
            this.score = score;
        }

        public String getLeaderboard() {
            return leaderboard;
        }

        public long getScore() {
            return score;
        }
    }
}

