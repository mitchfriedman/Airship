package com.mitch.framework;

import android.media.SoundPool;


public interface Audio {
    public Music createMusic(String file);
    public SoundPool getSound();
    public Sound createSound(String file);
}
