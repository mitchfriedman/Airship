package com.mitch.flyship.objects;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.GameBody;
import com.mitch.flyship.SliderMoveListener;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.Input;
import com.mitch.framework.containers.Rect;
import com.mitch.framework.containers.Vector2d;

public class Slider extends GameBody {

	boolean lastTouchedOnScreen = false;
	boolean lastTouched = false;
	
	SliderMoveListener listener;
	float sliderPosition;
	int minX, maxX;
	
	Image low, high, slider, notch;
	
	
	public Slider(AirshipGame game, String name, Vector2d pos, float defaultValue, SliderMoveListener listener) {
		super(game, name);
		slider = Assets.getImage(name);
		low = Assets.getImage("GUI/low");
		high = Assets.getImage("GUI/high");
		notch = Assets.getImage("GUI/Slider notch");
		setPos(pos);
		
		this.listener = listener;
		
		sliderPosition = defaultValue; //TODO: Load from preferences
	}
	
	@Override
	protected void onSetPos() {
		minX = (int) (getPos().x + 7 + low.getWidth());
		maxX = (int) (minX + slider.getWidth() - notch.getWidth());
	}

	@Override
	public void onUpdate(double deltaTime) {
		
		//TODO: touching slider bar should put notch under finger
		if (!lastTouchedOnScreen && !lastTouched && isSliderTouched()) {
			
			lastTouched = true;
			listener.onDown();
		}
		else if (lastTouched && game.getInput().isTouchDown(0)) {
			
			sliderPosition = ((float) (game.getInput().getTouchX(0) - minX)) / (float)(maxX-minX);
			sliderPosition = sliderPosition < 0 ? 0 : sliderPosition;
			sliderPosition = sliderPosition > 1 ? 1 : sliderPosition;
			
			listener.onPositionChanged(sliderPosition);
		}
		else if (lastTouched && !game.getInput().isTouchDown(0)) {
			lastTouched = false;
			listener.onUp();
		}
		
		if (!lastTouchedOnScreen && game.getInput().isTouchDown(0)) {
			lastTouchedOnScreen = true;
		}
		else if (lastTouchedOnScreen && !game.getInput().isTouchDown(0)) {
			lastTouchedOnScreen = false;
		}
	}
	
	public boolean isSliderTouched()
	{
		Input input = game.getInput();
		
		if (!input.isTouchDown(0)) {
			return false;
		}
		
		Vector2d touchPos = new Vector2d(input.getTouchX(0), input.getTouchY(0));
		return Rect.vectorWithinRect(touchPos, getTouchBounds());
	}
	
	Rect getTouchBounds()
	{
		
		return new Rect(
				getPos().x, 
				getPos().y, 
				11 + low.getWidth() + slider.getWidth() + high.getWidth(), 
				notch.getHeight());
	}

	
	public float getHeight() {
		return notch.getHeight() + 20;
	}
	
	public float getWidth() {
		return low.getWidth() + high.getWidth() + slider.getWidth() + 15;
	}

	@Override
	public void onPaint(float deltaTime) {
		// TODO Auto-generated method stub
		Graphics g = game.getGraphics();
		g.drawImage(low, getPos().add(new Vector2d(3,0)));
		g.drawImage(slider, new Vector2d(getPos().x + 7 + low.getWidth(), getPos().y));
		g.drawImage(high, new Vector2d(getPos().x + 11 + low.getWidth() + slider.getWidth(), getPos().y));
		g.drawImage(notch, new Vector2d(getPos().x + 7 + low.getWidth() + sliderPosition*(maxX-minX), getPos().y - notch.getHeight()/2 + slider.getHeight()/2));
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}
}
