package com.monisha.samples.andrawoid.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.monisha.samples.andrawoid.R;

import java.util.ArrayList;

/**
 * Created by Monisha on 2/18/2018.
 * Ref - Part 1: https://www.codeproject.com/Articles/1045443/Android-Drawing-App-Tutorial-Pt
 *       Part 2: https://www.codeproject.com/Articles/1046849/Android-Drawing-App-Tutorial-Pt
 */

public class CustomView extends View{
    private Path drawPath; //drawing path
    private Paint canvasPaint; //defines what to draw
    private Paint drawPaint; //defines how to draw
    private int paintColor = 0xFF000000; //initial color
    private Canvas drawCanvas; //holds yours drawings and transfers them to the view
    private Bitmap canvasBitmap; //canvas bitmap
    private float currentBrushSize; //brush size

    private boolean clearEnabled = false;

    private ArrayList<DrawElement> paths = new ArrayList<DrawElement>();
    private ArrayList<DrawElement> undonePaths = new ArrayList<DrawElement>();

    private float mX, mY;
    private float TOUCH_TOLERANCE = 0; //TODO check this

    private class DrawElement {
        Path path;
        int color;
        float brushSize;

        public DrawElement(Path path, int color, float brushSize) {
            this.path = path;
            this.color = color;
            this.brushSize = brushSize;
        }
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        currentBrushSize = getResources().getInteger(R.integer.MEDIUM_SIZE);

        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(currentBrushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
//        canvas.drawPath(drawPath, drawPaint);
        for (DrawElement p : paths) {
            drawPaint.setColor(p.color);
            drawPaint.setStrokeWidth(p.brushSize);
            canvas.drawPath(p.path, drawPaint);
        }
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh); //create canvas of certain device size
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888); //create Bitmap of certain w, h
        drawCanvas = new Canvas(canvasBitmap); //apply bitmap to graphic to start drawing
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        //respond to down, move and up events
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchStart(touchX, touchY);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(touchX, touchY);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
            default:
                return false;
        }
        //redraw
        return  true;
    }

    private void touchStart(float x, float y){
        undonePaths.clear();
        drawPath.reset();
        drawPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y){
        float dX = Math.abs(x - mX);
        float dY = Math.abs(y - mY);
        if(dX >= TOUCH_TOLERANCE || dY >= TOUCH_TOLERANCE) {
            drawPath.quadTo(mX, mY, (x + mX)/2 , (y + mY)/2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        drawPath.lineTo(mX, mY);
        drawCanvas.drawPath(drawPath, drawPaint);
        paths.add(new DrawElement(drawPath, paintColor, currentBrushSize));
        drawPath = new Path();

    }

    public boolean erase(Context context){
        //Toast.makeText(context, "Erase feature yet to be implemented", Toast.LENGTH_SHORT).show();
        if (paths.size()>0)
        {
            undonePaths.add(paths.remove(paths.size()-1));
            invalidate();
        }
        return true;
    }

    public boolean delete(Context context){
        //Toast.makeText(context, "Delete feature yet to be implemented", Toast.LENGTH_SHORT).show();
        //drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR); //This code works perfectly without the arrays
        paths.clear();
        undonePaths.clear();
        invalidate();
        return true;
    }

    public boolean redo(Context context){
        //Toast.makeText(context, "Undo feature yet to be implemented", Toast.LENGTH_SHORT).show();
        if (undonePaths.size()>0)
        {
            paths.add(undonePaths.remove(undonePaths.size()-1));
            invalidate();
        }
        return true;
    }

    public boolean brush(int brushSize){
        currentBrushSize = brushSize;
        drawPaint.setStrokeWidth(currentBrushSize);
        invalidate();
        return true;
    }

    public boolean color(Context context, int color){
        //Toast.makeText(context, "Color feature yet to be implemented", Toast.LENGTH_SHORT).show();
        paintColor = color;
        drawPaint.setColor(paintColor);
        invalidate();
        return true;
    }

}
