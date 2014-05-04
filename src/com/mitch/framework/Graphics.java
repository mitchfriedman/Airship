package com.mitch.framework;


import android.graphics.Canvas;
import android.graphics.Paint;

import com.mitch.framework.containers.Vector2d;

public interface Graphics {
	public static enum ImageFormat {
		ARGB8888, ARGB4444, RGB565
	}

	public Image newImage(String fileName, ImageFormat format);

	public void clearScreen(int color);

	public void drawLine(int x, int y, int x2, int y2, int color);

	public void drawRect(int x, int y, int width, int height, int color);

	public void drawImage(Image image, int x, int y, int srcX, int srcY,
			int srcWidth, int srcHeight);

	public void drawImage(Image Image, double x, double y);
	
	public void drawImage(Image image, double x, double y, double width, double height);
	
	public void drawImage(Image Image, Vector2d pos);

	Vector2d drawString(String text, int x, int y, Paint paint);
	
	public void drawImage2(Image Image, float x, float y);
	public int getWidth();

	public int getHeight();

	public void drawARGB(int i, int j, int k, int l);

	public Canvas getCanvas();

	public void drawScaledImage(Image currentSprite, int x, int y, float f,
			float g);
}
