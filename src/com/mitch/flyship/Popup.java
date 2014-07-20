package com.mitch.flyship;

import java.util.ArrayList;
import java.util.HashMap;

import com.mitch.flyship.objects.Button;
import com.mitch.flyship.objects.Slider;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.Input;
import com.mitch.framework.containers.Align;
import com.mitch.framework.containers.Vector2d;

public class Popup {
	
	private static final int TOUCH_OFFSET = 5;
	private static final float DEFAULT_MARGIN = 10;
	//private static final float MIN_POPUP_HEIGHT = 250;
	
	boolean screenLastTouched = false;
	boolean lastTouchedOutside = false;
	
	Vector2d position;
	AirshipGame game;
	Graphics g;
	
	boolean enabled;
    boolean disableOnClick = true;
	float popupHeight = 160;//= MIN_POPUP_HEIGHT;
	double currentY;
	float margin = 0;
	
	Image topBorder, center;
	
	HashMap<Image, Vector2d> images;
	ArrayList<Button> buttons;
	ArrayList<Slider> sliders;
	ArrayList<Float> margins;
	
	public Popup(AirshipGame game) {
		/* At the time of writing this code, I am the only one who knows what I am doing.
		 * By the time I read this again, nobody will know what I have done. What was I thinking?
		 * Sometimes you just need to sit back and smoke a joint, let it come to you later on. */
		this.game = game;
		g = game.getGraphics();
		
		topBorder = Assets.getImage("GUI/menu border");
		center = Assets.getImage("GUI/menu center");
		
		images = new HashMap<Image, Vector2d>();
		buttons = new ArrayList<Button>();
		sliders = new ArrayList<Slider>();
		margins = new ArrayList<Float>();

		position = new Vector2d((g.getWidth()-topBorder.getWidth())/2, (g.getHeight()-(topBorder.getHeight()*2 + popupHeight))/2);
		currentY = position.y + topBorder.getHeight();
	}

    public void setDisableOnClick(boolean disable)
    {
        disableOnClick = disable;
    }

	public void update(float deltaTime) {
		
		Input input = game.getInput();
		
		for(Button button : buttons) {
			button.onUpdate(deltaTime);
		}
		
		for(Slider slider : sliders) {
			slider.onUpdate(deltaTime);
		}
		
		
		if (disableOnClick && !input.isTouchDown(0) && screenLastTouched && lastTouchedOutside) {
			setEnabled(false);
		}
		
		if (input.isTouchDown(0)) {
			screenLastTouched = true;
			lastTouchedOutside = touchedOutside(input);
		}
		else {
			screenLastTouched = false;
		}
	}
	private boolean touchedOutside(Input input) {
		return (input.getTouchY(0) < position.y - TOUCH_OFFSET|| input.getTouchY(0) > currentY + TOUCH_OFFSET + topBorder.getHeight() + 5 ||
				input.getTouchX(0) < position.x - TOUCH_OFFSET || input.getTouchX(0) > position.x + topBorder.getWidth() + TOUCH_OFFSET);
	}
	
	public void paint(float deltaTime) {
		
		position = new Vector2d((g.getWidth()-topBorder.getWidth())/2, (g.getHeight()-(topBorder.getHeight()*2 + popupHeight))/2);
		
		if(enabled) {
			g.drawImage(topBorder, position);
						
			for(double i=position.y + topBorder.getHeight(); i < popupHeight + position.y; i++) {
				g.drawImage(center, new Vector2d(position.x, i));
			}
			g.drawImage(topBorder, new Vector2d(position.x, position.y + popupHeight), false, true);

			for (HashMap.Entry<Image, Vector2d> image : images.entrySet()) {
			    Image temp = image.getKey();
			    Vector2d pos = image.getValue();
			    
			    g.drawImage(temp, pos);
			}
			
			for(Button button : buttons) {
				button.onPaint(deltaTime);
			}
			
			for(Slider slider : sliders) {
				slider.onPaint(deltaTime);
			}
		}
		
	}
	
	public void setMargin(float margin) {
		this.margin = margin;
		margins.add(margin);
		//popupHeight += margin;
	}

    public void addNumericImage(int value)
    {
        final String FONT = "FONT/TIMER/";
        final int FONT_WIDTH = Assets.getImage(FONT + "0").getWidth();

        String strValue = String.valueOf(value);
        Vector2d numericImagePos = new Vector2d(g.getWidth() / 2 -
                FONT_WIDTH * strValue.length() / 2, currentY);

        for (int n = 0; n < strValue.length(); n++) {
            Vector2d pos = numericImagePos.add(new Vector2d(FONT_WIDTH*n+n, 0));
            images.put( Assets.getImage(FONT + strValue.charAt(n)) , pos);
        }

    }

	public void addImage(String name, Align.Horizontal horizontal) {
		Image image = Assets.getImage(name);
		Vector2d pos;
		switch(horizontal) {
		case LEFT:
			pos = centerImage(image);
			break;
		case CENTER:
			pos = centerImage(image);
			break;
		case RIGHT:
			pos = centerImage(image);
			break;
		default: 
			pos = centerImage(image);
		}
		
		pos = addMargin(pos);
		
		images.put(image, pos);
		//popupHeight += image.getHeight();
		currentY += image.getHeight();
	}
	
	public void addSlider(float defaultValue, SliderMoveListener listener) {

		Vector2d pos = new Vector2d(0,0);//new Vector2d((g.getWidth() - )/2,currentY);
		
		Slider slider = new Slider(game, "GUI/Slider bar", pos, defaultValue, listener);
		pos = new Vector2d((g.getWidth() - slider.getWidth())/2, currentY);
		setMargin(30);
		pos = addMargin(pos);
		slider.setPos(pos);
		
		//popupHeight += slider.getHeight();
		sliders.add(slider);
		currentY += slider.getHeight();
		
	}
	
	private Vector2d addMargin(Vector2d pos) {
		if(margin == 0) {
			pos.y += DEFAULT_MARGIN;
		} else {
			pos.y += margin;
			margin = 0;
		}
		return pos;
	}
	
	private Vector2d centerImage(Image image) {
		Vector2d pos = new Vector2d((g.getWidth() - image.getWidth())/2, currentY);
		return pos;
	}
	
	public void addButton(ButtonClickListener listener, String name) {
		Vector2d pos = new Vector2d((g.getWidth() - Assets.getImage(name+"-active").getWidth())/2, currentY);
		pos = addMargin(pos);
		Button button = new Button(game, name, new Align(Align.Vertical.TOP, Align.Horizontal.LEFT), pos, listener);
		
		position = addMargin(position);
		
		
		buttons.add(button);
		//popupHeight += button.getImage().getHeight();
		currentY += button.getImage().getHeight();
		
	}

	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean getEnabled() {
		return this.enabled;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
}
