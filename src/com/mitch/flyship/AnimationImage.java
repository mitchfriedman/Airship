package com.mitch.flyship;

import java.util.ArrayList;
import java.util.List;

import com.mitch.framework.containers.Frame;

public class AnimationImage {
	
	List<Frame> frames = new ArrayList<Frame>();
    boolean playingInReverse = false;
    boolean paused = false;
	int currentFrame = 0;
	float time = 0;
	float deltaFrameTime = 1/30;
	
	public AnimationImage(float fps)
	{
		setSpeed(fps);
	}
	
	public void setSpeed(float fps)
	{
        if (fps == 0) {
            deltaFrameTime = Float.POSITIVE_INFINITY;
        } else {
            playingInReverse = fps < 0;
            deltaFrameTime = Math.abs(1/fps);
        }
	}

    public boolean isPaused() { return paused; }

    public void pause()
    {
        paused = true;
    }

    public void resume()
    {
        paused = false;
    }

	public Frame getFrame(int index)
	{
		if (index < frames.size()) {
			return frames.get(index);
		} 
		else {
			return null;
		}
	}
	
	public Frame getFrame()
	{
		return getFrame(getCurrentFrameIndex());
	}
	
	public int getAnimationSize()
	{
		return frames.size();
	}
	
	public int getCurrentFrameIndex()
	{
		return currentFrame;
	}
	
	public void updateTime(double deltaTime)
	{
        if (!paused) {
            this.time += deltaTime;
        }

		if (time > deltaFrameTime) {
			currentFrame += playingInReverse ? -1 : 1;
			time = 0;
		}
		
		if (currentFrame >= getAnimationSize()) {
			currentFrame = 0;
		}

        if (currentFrame < 0) {
            currentFrame = getAnimationSize()-1;
        }
	}
	
	public void addFrame(Frame frame)
	{
		frames.add(frame);
	}
	
	public void setFrames(List<Frame> frames)
	{
		this.frames = frames;
	}
	
	public void addFrame(int index, Frame frame)
	{
		frames.add(index, frame);
	}
	
	public void resetAnimation() {
		currentFrame = playingInReverse ? 0 : getAnimationSize()-1;
	}
}
