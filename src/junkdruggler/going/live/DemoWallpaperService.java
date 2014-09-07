package junkdruggler.going.live;

import java.util.Calendar;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class DemoWallpaperService extends WallpaperService {

	@Override
	public Engine onCreateEngine() {
		return new DemoWallpaperEngine();
	}
	
	private class DemoWallpaperEngine extends Engine {
		private boolean mVisible = false;
		private int mColor = 0;
		private int mAlpha = 0;
		private boolean mIsCountingUp=true;
		private String mText = "Hello!";
		private final Handler mHandler = new Handler();
		private final Runnable mUpdateDisplay = new Runnable() {
		@Override
		public void run() {
			draw();
		}};
	
		private void draw() {
		   SurfaceHolder holder = getSurfaceHolder();
		   Canvas c = null;
		   try {
		      c = holder.lockCanvas();
		      if (c != null) {
		    	 //android.os.Debug.waitForDebugger(); 
		    	 Paint p = new Paint();
		    	 p.setTextSize(100);
		 		 p.setAntiAlias(true);
		 		String text = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
		         float w = p.measureText(text, 0, text.length());
		         int offset = (int) w / 2;
				 int x = c.getWidth()/2 - offset;
		         int y = c.getHeight()/2;
		         p.setColor(Color.BLACK);
		         c.drawRect(0, 0, c.getWidth(), c.getHeight(), p);
		         p.setColor(mColor);
		         p.setAlpha(mAlpha);
		         c.drawText(text, x, y, p);
		         if (mIsCountingUp) {
		        	 mAlpha+=10;
			         if (mAlpha > 255) {
			        	 mAlpha = 255;
			        	 mIsCountingUp=false;
			         }
		         } else {
		        	 mAlpha-=10;
		        	 if (mAlpha < 0) {
		        		 mAlpha = 0;
		        		 mIsCountingUp=true;
		        	 }
		         }
		         
		      }
		   } finally {
		      if (c != null)
		         holder.unlockCanvasAndPost(c);
		   }
		   mHandler.removeCallbacks(mUpdateDisplay);
	       if (mVisible) {
	           mHandler.postDelayed(mUpdateDisplay, 100);
	       }
		}
		
		@Override
		public void onVisibilityChanged(boolean visible) {
			mVisible = visible;
			if (visible) {
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(DemoWallpaperService.this);
				mText = prefs.getString("text_to_display", "This is the default.");
				String tmp = prefs.getString("text_color", "#ffffff");
				String r = tmp.substring(1,3);
				String g = tmp.substring(3,5);
				String b = tmp.substring(5,7);
				mColor = Color.rgb(Integer.parseInt(r, 16), Integer.parseInt(g, 16), Integer.parseInt(b, 16));
				draw();
			} else {
				mHandler.removeCallbacks(mUpdateDisplay);
			}
		}
		
		 @Override
	      public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	         draw();
	      }
		
		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			mVisible = false;
			mHandler.removeCallbacks(mUpdateDisplay);
		}
		
		@Override
	    public void onDestroy() {
	         super.onDestroy();
	         mVisible = false;
	         mHandler.removeCallbacks(mUpdateDisplay);
	    }
	}    
}
