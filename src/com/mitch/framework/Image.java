package com.mitch.framework;

import com.mitch.framework.Graphics.ImageFormat;
import com.mitch.framework.containers.Vector2d;

public interface Image {
    public int getWidth();
    public int getHeight();
    public Vector2d getSize();
    public ImageFormat getFormat();
    public void dispose();
}
