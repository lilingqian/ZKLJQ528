package com.example.lenovo.zk_ljq_528;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class GramophoneView extends View{



    //绘制唱片的相关变量
    private int halfMeasureWidth;

    //中间默认的半径是：
    private static final int DEFAULT_PICTURE_RADIUS = 400;
    //唱片的旋转速度
    private static final float DEFAULT_DISK_ROTATE_SPEED = 0.3f;
    //中间图片的半径
    private int pictureRadius;
    //黑色圆环的宽度
    private int ringWidth;
    //唱片旋转的速度
    private float diskRotateSpeed;
    //唱片的画笔
    private Paint discPaint;
    //裁剪图片的路径
    private Path clipPath;
    //图片
    private Bitmap bitmap;
    //图片被裁减的范围
    private Rect srcRect;
    //图片绘制的范围
    private Rect dstRect;



    //绘制唱针的相关变量

    //播放状态时唱针的旋转角度
    private static final int PLAY_DEGREE = -15;
    //暂停时唱针的旋转角度
    private static final int PAUSE_DEGREE = -45;
    //唱针顶部的小圆半径
    private int smallCircleRadius = 20;
    //唱针顶部的大圆半径
    private int bigCircleRadius;
    //唱针的较长那段
    private int longArmLength;
    //唱针长度较短的那段
    private int shortArmLength;
    //唱针的头较长的那段
    private int longHeadLength;
    //唱针的头较短的那段
    private int shortHeadLength;
    //唱针的画笔
    private Paint needlePaint;



    //状态控制的相关变量
    //判断是否处于播放的状态
    private boolean isPlaying;
    //唱针旋转角度的计数器
    private int needleDegreeCounter;
    //唱片旋转角度的计数器
    private float diskDegreeCounter;


    public GramophoneView(Context context) {
        this(context, null);
    }

    public GramophoneView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //加载xml文件

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GramophoneView);
        pictureRadius = (int)typedArray.getDimension(R.styleable.GramophoneView_picture_radius, DEFAULT_PICTURE_RADIUS);
        diskRotateSpeed = typedArray.getFloat(R.styleable.GramophoneView_disk_rotate_speed, DEFAULT_DISK_ROTATE_SPEED);
        Drawable drawable = typedArray.getDrawable(R.styleable.GramophoneView_src);
        if(drawable == null){
            bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.b);
        } else{
            bitmap = ((BitmapDrawable)drawable).getBitmap();
        }
        typedArray.recycle();

        //初始化唱片的变量
        ringWidth = pictureRadius>>1;
        discPaint = new Paint();
        discPaint.setColor(Color.BLACK);
        discPaint.setStyle(Paint.Style.STROKE);
        discPaint.setStrokeWidth(ringWidth);
        srcRect = new Rect();
        dstRect = new Rect();
        setBitmapRect(srcRect, dstRect);
        clipPath = new Path();
        clipPath.addCircle(0, 0, pictureRadius, Path.Direction.CW);
        diskDegreeCounter = 0;

        //初始化唱针的变量
        bigCircleRadius = smallCircleRadius<<1;
        shortHeadLength = (pictureRadius + ringWidth)/15;
        longHeadLength = shortHeadLength<<1;
        shortArmLength = longHeadLength<<1;
        longArmLength = shortArmLength<<1;
        needlePaint = new Paint();
        needleDegreeCounter = PAUSE_DEGREE;
    }

    private void setBitmapRect(Rect src, Rect dst) {

        src.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        dst.set(-pictureRadius, -pictureRadius, pictureRadius, pictureRadius);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = (pictureRadius+ringWidth)*2;
        int height = (pictureRadius+ringWidth)*2+longArmLength;
        int measuredWidth = resolveSize(width, widthMeasureSpec);
        int measuredHeight = resolveSize(height, heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        halfMeasureWidth = getMeasuredWidth()>>1;
        drawDisk(canvas);
        drawNeedle(canvas);
        if(needleDegreeCounter > PAUSE_DEGREE){
            invalidate();
        }
    }


    // 绘制唱片
    private void drawDisk(Canvas canvas) {
        diskDegreeCounter = diskDegreeCounter%360+diskRotateSpeed;
        drawDisk(canvas, diskDegreeCounter);

    }
    // 绘制旋转了制定角度的唱片
    private void drawDisk(Canvas canvas, float degree){
        // 绘制圆环，注意理解平移的圆心距离和圆环半径是怎么计算的
        canvas.save();
        canvas.translate(halfMeasureWidth, pictureRadius+ringWidth+longArmLength);
        canvas.rotate(degree);
        canvas.drawCircle(0, 0, pictureRadius+ringWidth/2, discPaint);
        // 绘制图片
        canvas.clipPath(clipPath);
        canvas.drawBitmap(bitmap, srcRect, dstRect, discPaint);
        canvas.restore();
    }

    //绘制唱针
    private void drawNeedle(Canvas canvas) {
        // 由于PLAY_DEGREE和PAUSE_DEGREE之间的差值是30，所以每次增/减值应当是30的约数即可
        if (isPlaying) {
            if (needleDegreeCounter < PLAY_DEGREE) {
                needleDegreeCounter += 3;
            }
        } else {
            if (needleDegreeCounter > PAUSE_DEGREE) {
                needleDegreeCounter -= 3;
            }
        }
        drawNeedle(canvas, needleDegreeCounter);
    }
    // 绘制旋转了指定角度的唱针
    private void drawNeedle(Canvas canvas, int degree){
        // 移动坐标到水平中点
        canvas.save();
        canvas.translate(halfMeasureWidth, 0);
        // 绘制唱针手臂
        needlePaint.setStrokeWidth(20);
        needlePaint.setColor(Color.parseColor("#C0C0C0"));
        // 绘制第一段臂
        canvas.rotate(degree);
        canvas.drawLine(0, 0, 0, longArmLength, needlePaint);
        // 绘制第二段臂
        canvas.translate(0, longArmLength);
        canvas.rotate(-30);
        canvas.drawLine(0, 0, 0, shortArmLength, needlePaint);
        // 绘制唱针头
        // 绘制第一段唱针头
        canvas.translate(0, shortArmLength);
        needlePaint.setStrokeWidth(40);
        canvas.drawLine(0, 0, 0, longHeadLength, needlePaint);
        // 绘制第二段唱针头
        canvas.translate(0, longHeadLength);
        needlePaint.setStrokeWidth(60);
        canvas.drawLine(0, 0, 0, shortHeadLength, needlePaint);
        canvas.restore();

        // 两个重叠的圆形
        canvas.save();
        canvas.translate(halfMeasureWidth, 0);
        needlePaint.setStyle(Paint.Style.FILL);
        needlePaint.setColor(Color.parseColor("#C0C0C0"));
        canvas.drawCircle(0, 0, bigCircleRadius, needlePaint);
        needlePaint.setColor(Color.parseColor("#8A8A8A"));
        canvas.drawCircle(0, 0, smallCircleRadius, needlePaint);
        canvas.restore();
    }

    //是否处于播放的状态

    public void setPlaying(boolean isPlaying){
        this.isPlaying = isPlaying;
        invalidate();
    }
    //获取播放的状态
    public boolean getPlaying(){
        return isPlaying;
    }

    //获取图片的半径
    public int getPictureRadius() {
        return pictureRadius;
    }

    //设置图片的半径
    public void setPictureRadius(int pictureRadius) {
        this.pictureRadius = pictureRadius;
    }

    //获取唱片的旋转速度
    public float getDiskRotateSpeed() {
        return diskRotateSpeed;
    }

    //设置唱片的旋转速度
    public void setDiskRotateSpeed(float diskRotateSpeed) {
        this.diskRotateSpeed = diskRotateSpeed;
    }

    public void setPictureRes(int resId){
        bitmap = BitmapFactory.decodeResource(getContext().getResources(), resId);
        setBitmapRect(srcRect, dstRect);
        invalidate();
    }


    public GramophoneView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}