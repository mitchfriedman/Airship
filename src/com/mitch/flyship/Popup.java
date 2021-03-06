package com.mitch.flyship;

import java.util.ArrayList;

import android.graphics.Rect;

import com.mitch.flyship.objects.Button;
import com.mitch.flyship.objects.Slider;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.Input;
import com.mitch.framework.containers.Align;
import com.mitch.framework.containers.Vector2d;

public class Popup {
	
	public static Popup settings;
	
	public float marginTop = 0;
	
	private boolean screenLastTouched = false;
	private boolean lastTouchedOutside = false;
	
	private Vector2d position;
	private AirshipGame game;
	private Graphics g;
	
	private boolean enabled;
    boolean disableOnClick = true;
	private double currentY = 0;
	private int popupHeight = 0;
	
	public Image topBorder, center;
	
	//private HashMap<Image, Vector2d> images;
	private ArrayList<Image> images;
	private ArrayList<Vector2d> imagePositions;
	private ArrayList<Button> buttons;
	private ArrayList<Slider> sliders;
	
	public Popup(AirshipGame game) {
		/* At the time of writing this code, I am the only one who knows what I am doing.
		 * By the time I read this again, nobody will know what I have done. What was I thinking?
		 * Sometimes you just need to sit back and smoke a joint, let it come to you later on. */
		this.game = game;
		g = game.getGraphics();
		
		topBorder = Assets.getImage("GUI/menu border");
		center = Assets.getImage("GUI/menu center");
		
		images = new ArrayList<Image>();
		imagePositions = new ArrayList<Vector2d>();
		buttons = new ArrayList<Button>();
		sliders = new ArrayList<Slider>();
		position = new Vector2d(0,0);
	}
	
	public void build()
	{
		popupHeight = (int) currentY;
		
		position = new Vector2d( g.getWidth()/2 - topBorder.getWidth()/2, 
				g.getHeight()/2 - popupHeight / 2 + marginTop);
		
		for (Vector2d imagePos : imagePositions) {
			imagePos.y += position.y - marginTop;
		}
		
		for (Button button : buttons) {
			button.setPos(button.getPos().add(new Vector2d(0, position.y - marginTop)));
		}
		
		for (Slider slider : sliders) {
			slider.setPos(slider.getPos().add(new Vector2d(0, position.y - marginTop)));
		}
	}
	
	public int getHeight()
	{
		return (int) (popupHeight + marginTop);
	}
	
    public void setDisableOnClick(boolean disable)
    {
        disableOnClick = disable;
    }

	public void update(double deltaTime) {
		
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
			lastTouchedOutside = false;
		}
	}
	private boolean touchedOutside(Input input) {
		Rect bounds = getBounds();
		Vector2d touch = new Vector2d(input.getTouchX(0), input.getTouchY(0));
		
		return touch.y < bounds.top || touch.y > bounds.bottom || touch.x < bounds.left || touch.x > bounds.right;
	}
	
	public Rect getBounds() {
		
		Rect bounds = new Rect(0,0,0,0);
		bounds.top = (int)position.y;
		bounds.left = (int)position.x;
		bounds.right = bounds.left + topBorder.getWidth();
		bounds.bottom = bounds.top + popupHeight;
		return bounds;
	}
	
	public void paint(float deltaTime) 
	{
		
		if(enabled) {
			
			g.drawARGB(190, 100, 59, 15);
			
			g.drawImage(topBorder, position);
						
			for(int i = 0; i < popupHeight - marginTop - topBorder.getHeight(); i++) {
				int yPos = (int) (position.y + topBorder.getHeight() + i);
				g.drawImage(center, new Vector2d(position.x, yPos));
			}
			
			g.drawImage(topBorder, new Vector2d(position.x, position.y + popupHeight-marginTop), false, true);
			
			for (int i = 0; i < images.size(); i++) {
				Image image = images.get(i);
				Vector2d pos = imagePositions.get(i);
				g.drawImage(image, pos);
			}
			
			for(Button button : buttons) {
				button.onPaint(deltaTime);
			}
			
			for(Slider slider : sliders) {
				slider.onPaint(deltaTime);
			}
		}
		
	}
	
	public void addHeightMargin(int margin) {
		currentY += margin;
	}
	
	public void addTimerImage(final double timeInSeconds, final String FONT)
	{
		
		String strMinutes = String.valueOf( (int) (timeInSeconds / 60) );
		String strSeconds = String.valueOf( (int) (timeInSeconds % 60) );
		
		if (strMinutes.length() == 1) {
			strMinutes = "0" + strMinutes;
		}
		
		if (strSeconds.length() == 1) {
			strSeconds = "0" + strSeconds;
		}
		
		String strTime = strMinutes + ":" + strSeconds;
		
		final int WIDTH = Assets.getImage(FONT + "0").getWidth() * (2 + strMinutes.length()) + 
				Assets.getImage(FONT + ":").getWidth();
		final int HEIGHT = Assets.getImage(FONT + "0").getHeight();;
		final int CHARACTER_SPACING = 0;
		
		int currentX = 0;
		Vector2d timerPos = new Vector2d( g.getWidth()/2 - WIDTH/2, 0 );
		for (int c = 0; c < strTime.length(); c++) {

			Image charImage = Assets.getImage(FONT + strTime.charAt(c));
			Vector2d charPos = timerPos.add(new Vector2d(currentX, currentY + HEIGHT/2 - charImage.getHeight()/2));
			images.add(charImage);
			imagePositions.add(charPos);
			currentX += charImage.getWidth() + CHARACTER_SPACING;
		}
		
		currentY += HEIGHT;
	}
	
    public void addNumericImage(int value)
    {
        final String FONT = "FONT/TIMER/";
        final int FONT_WIDTH = Assets.getImage(FONT + "0").getWidth();
        final int FONT_HEIGHT = Assets.getImage(FONT + "0").getHeight();

        String strValue = String.valueOf(value);
        Vector2d numericImagePos = new Vector2d(g.getWidth() / 2 -
                FONT_WIDTH * strValue.length() / 2, currentY);

        for (int n = 0; n < strValue.length(); n++) {
            images.add(Assets.getImage(FONT + strValue.charAt(n)));
			imagePositions.add( numericImagePos.add( new Vector2d(FONT_WIDTH * n + n, 0) ) );
        }
        
        currentY += FONT_HEIGHT;

    }

	public void addImage(String name, Align.Horizontal horizontal) {
		
		Image image = Assets.getImage(name);
		Vector2d pos = new Vector2d(0, currentY);
		
		switch(horizontal) {
		case LEFT:
			pos.x = g.getWidth() / 2 - center.getWidth() / 2 + topBorder.getHeight();
			break;
		case CENTER:
			pos.x = g.getWidth() / 2 - image.getWidth() / 2;
			break;
		case RIGHT:
			pos.x = g.getWidth() / 2 + center.getWidth() / 2 - topBorder.getHeight();
			break;
		}
		
		images.add(image);
		imagePositions.add(pos);
		currentY += image.getHeight();
	}
	
	public void addSlider(float defaultValue, SliderMoveListener listener) {

		Vector2d pos = new Vector2d(0,0);
		
		Slider slider = new Slider(game, "GUI/Slider bar", pos, defaultValue, listener);
		pos = new Vector2d(g.getWidth()/2 - slider.getWidth()/2, currentY);
		slider.setPos(pos);
		sliders.add(slider);
		
		currentY += slider.getHeight();
		
	}
	
	public void addButton(ButtonClickListener listener, String name) {
		
		Vector2d pos = new Vector2d(0,0);
		Button button = new Button(game, name, new Align(Align.Vertical.TOP, Align.Horizontal.LEFT), pos, listener);
		pos = new Vector2d((g.getWidth() - Assets.getImage(name+"-active").getWidth())/2, currentY);
		button.setPos(pos);
		buttons.add(button);
		
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
	
	public static Popup buildSettingsPopup(final AirshipGame game)
	{
		if (settings != null) {
			return settings;
		}
		
		SliderMoveListener sensitivityListener = new SliderMoveListener() {
            @Override
            public void onPositionChanged(float position) {
            	game.setSensitivity(position);
            }
        };
        ButtonClickListener calibrateListener = new ButtonClickListener() {
            @Override
            public void onUp()
            {
            	game.centerOrientation();
            	game.getAchievementManager().onCalibrate();
            }
        };
		
        settings = new Popup(game);
        
        settings.addHeightMargin(settings.topBorder.getHeight() + 4);
        
        settings.addImage("GUI/tilt sensitivity", Align.Horizontal.CENTER);
        settings.addHeightMargin(3);
        settings.addSlider((float) game.getSensitivity(), sensitivityListener);
        settings.addHeightMargin(10);
        
        settings.addImage("GUI/tilt calibration", Align.Horizontal.CENTER);
        settings.addHeightMargin(3);
        settings.addButton(calibrateListener, "GUI/Calibrate");
        settings.addHeightMargin(4);
        
        settings.build();
        
        return settings;
	}
}
