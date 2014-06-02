package com.codepix.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.codepix.loader.ImageLoader;

public class GestureImageViewLock extends ImageView {

	Paint paint = new Paint();
    Point point = new Point();
  
    boolean flag=false;
	private ImageLoader mLoader;
	private Matrix resize;
	
	Context context;
    public GestureImageViewLock(Context context) {
        super(context);
        
        this.context=context;
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        paint.setStyle(Style.STROKE);
       
    
    }
    
    public GestureImageViewLock(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GestureImageViewLock(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }

  

	@SuppressLint("DrawAllocation")
	@Override
    protected void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
	    int w = getWidth(), h = getHeight();
	    
		Drawable drawable = getDrawable();

        Bitmap b =  ((BitmapDrawable)drawable).getBitmap() ;
             
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
	    // resize
	   resize = new Matrix();
	  resize.postScale(403 / (float)bitmap.getWidth(), 503 / (float)bitmap.getHeight());
	  Bitmap imageScaled = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), resize, false);

	//c.drawBitmap(imageScaled, 0,0, paint);
		
		
		
	
        
        canvas.drawBitmap(imageScaled, 0,0 , paint);
         if(ImageGestureActivity.gesture==true)
         {
        
          canvas.drawCircle(point.x, point.y, 20, paint);
          
         }
    }
	
	 @Override
	 protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		 try {
		      Drawable drawable = getDrawable();
		      if (drawable == null) {
		        setMeasuredDimension(0, 0);
		      } else {
		        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
		        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
		        if (measuredHeight == 0 && measuredWidth == 0) { //Height and width set to wrap_content
		          setMeasuredDimension(measuredWidth, measuredHeight);
		        } else if (measuredHeight == 0) { //Height set to wrap_content
		          int width = measuredWidth;
		          int height = width *  drawable.getIntrinsicHeight() / drawable.getIntrinsicWidth();
		          setMeasuredDimension(width, height);
		        } else if (measuredWidth == 0){ //Width set to wrap_content
		          int height = measuredHeight;
		          int width = height * drawable.getIntrinsicWidth() / drawable.getIntrinsicHeight();
		          setMeasuredDimension(width, height);
		        } else { //Width and height are explicitly set (either to match_parent or to exact value)
		          setMeasuredDimension(measuredWidth, measuredHeight);
		        }
		      }
		    } catch (Exception e) {
		      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		    }
	 }    

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
        	flag=true;
            point.x = event.getX();
            point.y = event.getY();
            ImageGestureActivity.gestureCordinates=event.getX()+","+event.getY();
            ImageGestureActivity.gestureEnabled=true;
            
        }
          invalidate();
        return true;

    }
    class Point {
        float x, y;
    }
}
 

 
