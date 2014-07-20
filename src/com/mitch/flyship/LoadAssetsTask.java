package com.mitch.flyship;

import android.content.res.XmlResourceParser;
import android.os.AsyncTask;

import com.mitch.framework.Audio;
import com.mitch.framework.Graphics;
import com.mitch.framework.implementation.AndroidGame;

public class LoadAssetsTask extends AsyncTask<AndroidGame, Void, Void> {

	@Override
	protected Void doInBackground(AndroidGame... args) {
		AndroidGame game = args[0];
        XmlResourceParser xrp;

        //Loads assets
		xrp = game.getResources().getXml(R.xml.asset_list);
		Graphics g = game.getGraphics();
		Audio a = game.getAudio();
		Assets.loadFromXML(xrp, g, a);
		xrp.close();

        //Loads levels
        xrp = game.getResources().getXml(R.xml.level_list);
        LevelProperties.loadLevels(xrp);
        xrp.close();

		return null;
	}

}
