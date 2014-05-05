package com.mitch.flyship;

import java.util.ArrayList;
import java.util.List;

import com.mitch.framework.Image;
import com.mitch.framework.containers.Rect;

public class AnimationImage {
	
	Image src;
	List<Rect> frames = new ArrayList<Rect>();
	int currentFrame = 0;
	double time = 0;
	double deltaFrameTime;
	
	public AnimationImage(Image src, double deltaFrameTime)
	{
		this.src = src;
		this.deltaFrameTime = deltaFrameTime;
	}
	
	public Image getImage()
	{
		return src;
	}
	
	public Rect getFrameAtIndex(int index)
	{
		if (index < frames.size() && index >= 0) {
			return frames.get(index);
		}
		else {
			return null;
		}
	}
	
	public Rect getCurrentFrame()
	{
		return getFrameAtIndex(currentFrame);
	}
	
	public void addTime(double time)
	{
		this.time += time;
	}
	
	public void updateCurrentFrame()
	{
		if (frames.size() > 0) {
			currentFrame = (int) (time * deltaFrameTime) % frames.size();
		}
	}
	
	public void addFrame(Rect frame)
	{
		frames.add(frame);
	}
	
	public void addFrame(int index, Rect frame)
	{
		frames.add(index, frame);
	}
	
}
