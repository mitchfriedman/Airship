package com.mitch.flyship.objects;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.GameBody;
import com.mitch.flyship.SliderMoveListener;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Align;
import com.mitch.framework.containers.Rect;
import com.mitch.framework.containers.Vector2d;

public class Slider extends GameBody {

	boolean lgastTouched = false;
	boolean down = false;
	Align align;
	float sliderX;
	SliderMoveListener listener;
	
	Image low, high, slider, notch;
	
	
	public Slider(AirshipGame game, String name, Vector2d pos, SliderMoveListener listener) {
		super(game, name);
		slider = Assets.getImage(name);
		low = Assets.getImage("GUI/low");
		high = Assets.getImage("GUI/high");
		notch = Assets.getImage("GUI/Slider notch");
		setPos(pos);
		sliderX = 0;
		this.listener = listener;
	}


	@Override
	public void onUpdate(float deltaTime) {
		/*Vector2d size = new Vector2d(slider.getSize().x, slider.getSize().y*3);
		setSize(size);

		boolean touched = isTouched(new Vector2d(getPos().x - low.getWidth() - 12,0));
		boolean screenTouched = game.getInput().isTouchDown(0);
		float touchPos;
		
		if (touched && !lastTouched) {
			listener.onDown();
			onDown();
			
			touchPos = game.getInput().getTouchX(0);
			sliderX = (float) (touchPos - getPos().x - 7);

			
			listener.onPositionChanged(sliderX);
			Log.d("pos:","pos:" + sliderX);
			
			down = true;
			lastTouched = true;
		}
		else if (lastTouched && !touched && screenTouched) {
			listener.onCancel();
			onCancel();
			
			down = false;
			lastTouched = false;
		}
		else if (lastTouched && !touched) {
			listener.onUp();
			onUp();
			
			
			
			down = false;
			lastTouched = false;
		}
		*/
	}
	
	void onDown()
	{
		//sliderX = 50;
		//currentImage = pressedImage;
	}
	
	void onUp()
	{
		//currentImage = activeImage;
	}
	
	void onCancel()
	{
		onUp();
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
		g.drawImage(notch, new Vector2d(getPos().x + 7 + low.getWidth() + sliderX, getPos().y - notch.getHeight()/2 + slider.getHeight()/2));
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
