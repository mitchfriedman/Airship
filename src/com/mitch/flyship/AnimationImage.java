package com.mitch.flyship;

import java.util.ArrayList;
import java.util.List;

import com.mitch.framework.containers.Frame;

public class AnimationImage {
	
	List<Frame> frames = new ArrayList<Frame>();
	int currentFrame = 0;
	float time = 0;
	float deltaFrameTime = 1000;
	
	public AnimationImage(float fps)
	{
		setSpeed(fps);
	}
	
	public void setSpeed(float fps)
	{
		deltaFrameTime = 1000/fps;
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
	
	public void updateTime(float deltaTime)
	{
		this.time += deltaTime;
		if (time > deltaFrameTime) {
			currentFrame++;
			time = 0;
		}
		
		if (currentFrame >= getAnimationSize()) {
			currentFrame = 0;
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
	
}
