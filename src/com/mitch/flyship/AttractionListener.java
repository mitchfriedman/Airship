package com.mitch.flyship;

/**
 * Created by KleptoKat on 7/19/2014.
 */
public abstract class AttractionListener {

    public <T>AttractionListener() {}
    public abstract void onCollection(GameBody body);
    public abstract void onAttraction(GameBody body);
}
