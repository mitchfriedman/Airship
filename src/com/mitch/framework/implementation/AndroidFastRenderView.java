package com.mitch.framework.implementation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AndroidFastRenderView extends SurfaceView implements Runnable {
    public AndroidGame game;
    public Bitmap framebuffer;
    public Thread renderThread = null;
    public SurfaceHolder holder;
    volatile boolean running = false;
    
    final float FPS = 45.0f;
    final float UPS = 45.0f;
    
    public long startTime;
    public long lastUpdate;
    public long lastRender;
    
    
    public AndroidFastRenderView(AndroidGame game, Bitmap framebuffer) {
        super(game);
        this.game = game;
        this.framebuffer = framebuffer;
        this.holder = getHolder();
    }
    
    public void resume() { 
        running = true;
        renderThread = new Thread(this);
        renderThread.start();  
    }      
    
    public void run() {
    	long now = System.nanoTime();
        startTime  = now;
        lastUpdate = startTime;
        lastRender = startTime;
        
        int updateCount = 0;
        int frameCount = 0;
        long lastTime = now;
        long time = 0;
        int fps = 0;
        int ups = 0;
        
        while(running) {  
            if(!holder.getSurface().isValid())
                continue;
            
            now = System.nanoTime();
            
            time += now-lastTime;
            lastTime = now;
            
            if (time > 1000000000) {
    			time -= 1000000000;
    			fps = frameCount;
    			ups = updateCount;
    			updateCount = 0;
    			frameCount = 0;
    			Log.d("FPS:UPS", fps + ":"+ups);
    		}
            
            now = System.nanoTime();
            if (now - lastUpdate > 1000000000/UPS) {
            	game.getCurrentScreen().update(System.nanoTime() - lastUpdate);
            	
        		updateCount++;
        		lastUpdate = now;
        		
        		/*if (now - lastUpdate > 1000000000/UPS) {
        			Log.d("UPDATING AGAIN", (now - lastUpdate) +":"+ (1000000000/UPS));
        		}*/
            }
            
            now = System.nanoTime();
            if (now - lastRender > 1000000000/FPS) {
            	game.getCurrentScreen().paint(System.nanoTime() - game.renderView.lastRender);
                Canvas canvas = holder.lockCanvas();
                canvas.drawBitmap(framebuffer, null, canvas.getClipBounds(), null);                           
                holder.unlockCanvasAndPost(canvas); 
        		
        		frameCount++;
        		lastRender = now;
        		
        		/*if (now - lastRender > 1000000000/FPS) {
        			Log.d("RENDERING AGAIN", (now - lastRender) +":"+ (1000000000/FPS));
        		}*/
            }
        }
    }

    public void pause() {                        
        running = false;                        
        while(true) {
            try {
                renderThread.join();
                break;
            } catch (InterruptedException e) {
                // retry
            }
            
        }
    }     
    
  
}