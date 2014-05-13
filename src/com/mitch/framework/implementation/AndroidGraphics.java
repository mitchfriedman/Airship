package com.mitch.framework.implementation;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Rect;
import com.mitch.framework.containers.Vector2d;

public class AndroidGraphics implements Graphics {
    AssetManager assets;
    Bitmap frameBuffer;
    Canvas canvas;
    Paint paint;

    public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) 
    {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();
    }
    
    @Override
    public Image newImage(String fileName, ImageFormat format) 
    {
        Config config = null;
        if (format == ImageFormat.RGB565)
            config = Config.RGB_565;
        else if (format == ImageFormat.ARGB4444)
            config = Config.ARGB_4444;
        else
            config = Config.ARGB_8888;

        Options options = new Options();
        options.inPreferredConfig = config;
        options.inScaled = false;
        
        InputStream in = null;
        Bitmap bitmap = null;
        try {
            in = assets.open(fileName);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            if (bitmap == null)
                throw new RuntimeException("Couldn't load bitmap from asset '"
                        + fileName + "'");
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '"
                    + fileName + "'");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }

        if (bitmap.getConfig() == Config.RGB_565)
            format = ImageFormat.RGB565;
        else if (bitmap.getConfig() == Config.ARGB_4444)
            format = ImageFormat.ARGB4444;
        else
            format = ImageFormat.ARGB8888;

        return new AndroidImage(bitmap, format);
    }

    @Override
    public void clearScreen(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
                (color & 0xff));
    }


    @Override
    public void drawLine(int x, int y, int x2, int y2, int color) {
        paint.setColor(color);
        canvas.drawLine(x, y, x2, y2, paint);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
    }
    
    @Override
    public void drawARGB(int a, int r, int g, int b) {
        paint.setStyle(Style.FILL);
       canvas.drawARGB(a, r, g, b);
    }
    
    @Override
    public void drawString(String text, int x, int y, Paint paint) {
    	canvas.drawText(text, x, y, paint);
    }
    
    @Override
    public void drawImage(Image image, double x, double y) 
    {
    	drawImage(image, new Vector2d(x,y));
    }
    
    @Override
    public void drawImage(Image image, Vector2d pos, Vector2d size) 
    {
    	Rect dst = new Rect(pos, size);
    	canvas.drawBitmap(((AndroidImage)image).bitmap, null, dst.getAndroidRectF(), null);
    }
    
    @Override
    public void drawImage(Image image, double x, double y, double width,
    		double height) 
    {
    	drawImage(image, new Vector2d(x,y), new Vector2d(width, height));
    }
    
    @Override
    public void drawImage(Image image, int x, int y, double scale) 
    {
    	drawImage(image, new Vector2d(x,y), scale);
    }
    
    @Override
    public void drawImage(Image image, int x, int y, Rect src) 
    {
    	drawImage(image, new Vector2d(x,y), src);
    }
    
    @Override
    public void drawImage(Image image, Rect dst) 
    {
    	canvas.drawBitmap(((AndroidImage)image).bitmap, null, dst.getAndroidRectF(), null);
    }
    
    @Override
    public void drawImage(Image image, Rect dst, Rect src) 
    {
    	canvas.drawBitmap(((AndroidImage)image).bitmap, src.getAndroidRect(), dst.getAndroidRectF(), null);
    }
    
    @Override
    public void drawImage(Image image, Vector2d pos) 
    {
    	Rect dst = new Rect(pos, image.getSize());
    	canvas.drawBitmap(((AndroidImage)image).bitmap, null, dst.getAndroidRectF(), null);
    }
    
    @Override
    public void drawImage(Image image, Vector2d pos, double scale)
    {
    	Rect dst = new Rect(pos, image.getSize().scale(scale));
    	canvas.drawBitmap(((AndroidImage)image).bitmap, null, dst.getAndroidRectF(), null);
    }
    
    @Override
    public void drawImage(Image image, Vector2d dest, Rect src) 
    {
    	Rect dst = new Rect(dest, image.getSize());
    	canvas.drawBitmap(((AndroidImage)image).bitmap, src.getAndroidRect(), dst.getAndroidRectF(), null);
    }
   
    @Override
    public int getWidth() 
    {
        return frameBuffer.getWidth();
    }

    @Override
    public int getHeight()
    {
        return frameBuffer.getHeight();
    }
    public Canvas getCanvas() 
    {
    	return canvas;
    }
}
