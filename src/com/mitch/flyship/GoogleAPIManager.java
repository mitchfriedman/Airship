package com.mitch.flyship;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;

import java.util.*;
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
            for (LeaderboardScore score : leaderboardScores) {
                Games.Leaderboards.submitScore(client, score.getLeaderboard(), score.getScore());
                game.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(client,
                        score.getLeaderboard()), REQUEST_LEADERBOARD);
            }
            leaderboardScores.clear();

        } else if (!client.isConnecting()) {
            connect();
        }

    }

    public void pushLeaderboardScore(String leaderboardID, long score)
    {
        tryPushScores();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Connection Failed", connectionResult.toString());
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(game, CONNECTION_RESOLUTION_REQUEST_CODE);
            } catch(Exception e) {
                connect();
            }
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d("CONNECTED", "Connected to Google Play. Pushing scores");
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

