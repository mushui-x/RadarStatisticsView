package com.jersay.radarstatisticsview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhangjie on 2018/10/9.
 */
public class RadarStatisticsView extends View {

    private int count = 6;//默认数据量
    private float angle;//每条数据之间的角度
    private float radius;//网状半径
    private int centerX;//中心点x坐标
    private int centerY;//中心点y坐标
    private String[] titles = {};
    private double[] data = {};//各维度分值
    private float maxValue = 100;//默认数据最大值
    private Paint mainPaint;//雷达区画笔
    private Paint valuePaint;//数据区画笔
    private Paint titlePaint;//文本画笔


    public RadarStatisticsView(Context context) {
        super(context);
        init();
    }

    public RadarStatisticsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadarStatisticsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        count = Math.min(data.length, titles.length);//根据数据源计算数据量
        angle = (float) (Math.PI * 2 / count);
        mainPaint = new Paint();
        mainPaint.setAntiAlias(true);//抗锯齿
        mainPaint.setColor(Color.GRAY);//颜色
        mainPaint.setStyle(Paint.Style.STROKE);//模式为描边

        valuePaint = new Paint();
        valuePaint.setAntiAlias(true);
        valuePaint.setColor(Color.BLUE);
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        titlePaint = new Paint();
        titlePaint.setTextSize(20);//设置标题大小
        titlePaint.setColor(Color.BLACK);
        titlePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        radius = Math.min(w, h) / 2 * 0.9f;//计算半径
        centerX = w / 2;
        centerY = h / 2;
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPolygon(canvas);//绘制多边形
        drawLines(canvas);//绘制直线
        drawTitles(canvas);//绘制标题
        drawDataArea(canvas);//绘制数据区
    }

    /**
     * 绘制多边形
     *
     * @param canvas canvas
     */
    private void drawPolygon(Canvas canvas) {
        Path path = new Path();
        float r = radius / (count - 1);//每个多边形之间的距离
        for (int i = 1; i < count; i++) {
            float curR = r * i;
            path.reset();
            for (int j = 0; j < count; j++) {
                if (j == 0) {
                    path.moveTo(centerX + curR, centerY);//多边形的起点位置
                } else {//多边形的其他点位置
                    float x = (float) (centerX + curR * Math.cos(angle * j));
                    float y = (float) (centerY + curR * Math.sin(angle * j));
                    path.lineTo(x, y);
                }
            }
            path.close();//path闭合
            canvas.drawPath(path, mainPaint);//绘制多边形
        }

    }

    /**
     * 绘制直线
     *
     * @param canvas canvas
     */
    private void drawLines(Canvas canvas) {
        Path path = new Path();
        for (int i = 0; i < count; i++) {
            path.reset();
            path.moveTo(centerX, centerY);//将path起点移到多边形中心
            float x = (float) (centerX + radius * Math.cos(angle * i));
            float y = (float) (centerY + radius * Math.sin(angle * i));
            path.lineTo(x, y);
            canvas.drawPath(path, mainPaint);
        }
    }

    /**
     * 绘制标题
     *
     * @param canvas canvas
     */
    private void drawTitles(Canvas canvas) {
        Paint.FontMetrics fontMetrics = titlePaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        for (int i = 0; i < count; i++) {
            float x = (float) (centerX + (radius + fontHeight / 2) * Math.cos(angle * i));
            float y = (float) (centerY + (radius + fontHeight / 2) * Math.sin(angle * i));
            if (angle * i >= 0 && angle * i <= Math.PI / 2) {//位于第1象限(屏幕坐标象限，非数学坐标系象限)
                canvas.drawText(titles[i], x, y, titlePaint);
            } else if (angle * i > Math.PI / 2 && angle * i <= Math.PI) {//位于第2象限
                float dis = titlePaint.measureText(titles[i]);//文本长度
                canvas.drawText(titles[i], x - dis, y, titlePaint);
            } else if (angle * i >= Math.PI && angle * i < 3 * Math.PI / 2) {//位于第3象限
                float dis = titlePaint.measureText(titles[i]);//文本长度
                canvas.drawText(titles[i], x - dis, y, titlePaint);
            } else if (angle * i >= 3 * Math.PI / 2 && angle * i <= Math.PI * 2) {//第4象限
                canvas.drawText(titles[i], x, y, titlePaint);
            }
        }
    }

    /**
     * 绘制数据区域
     *
     * @param canvas canvas
     */
    private void drawDataArea(Canvas canvas) {
        Path path = new Path();
        valuePaint.setAlpha(225);//设置透明度
        for (int i = 0; i < count; i++) {
            double percent = data[i] / maxValue;
            float x = (float) (centerX + radius * Math.cos(angle * i) * percent);
            float y = (float) (centerY + radius * Math.sin(angle * i) * percent);
            if (i == 0) {
                path.moveTo(x, centerY);
            }
            path.lineTo(x, y);
            //绘制小圆点
            canvas.drawCircle(x, y, 10, valuePaint);
        }
        //绘制数据区边框
        valuePaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, valuePaint);

        //绘制填充区域
        valuePaint.setAlpha(127);
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path, valuePaint);
    }

    /**
     * 设置标题
     *
     * @param titles 标题数组
     */
    public void setTitles(String[] titles) {
        this.titles = titles;
        init();
        invalidate();
    }

    /**
     * 设置数据源
     *
     * @param data 数据源
     */
    public void setData(double[] data) {
        this.data = data;
        init();
        invalidate();
    }

    /**
     * 设置最大值
     *
     * @param maxValue 最大值
     */
    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * 获取最大值
     *
     * @return 最大值
     */
    public float getMaxValue() {
        return maxValue;
    }

    /**
     * 设置多边形网格颜色
     *
     * @param color 多边形网格颜色
     */
    public void setMainPaintColor(String color) {
        mainPaint.setColor(Color.parseColor(color));
    }

    /**
     * 设置多边形网格线条宽度
     *
     * @param width 多边形网格线条宽度
     */
    public void setMainPaintStrokeWidth(float width) {
        mainPaint.setStrokeWidth(width);
    }

    /**
     * 设置标题颜色
     *
     * @param color 标题颜色
     */
    public void setTitlePaintColor(String color) {
        titlePaint.setColor(Color.parseColor(color));
    }

    /**
     * 设置标题大小
     *
     * @param size 标题大小
     */
    public void setTitleSize(float size) {
        titlePaint.setTextSize(size);
    }

    /**
     * 设置数据区域颜色
     *
     * @param color 数据区颜色
     */
    public void setValuePaintColor(String color) {
        valuePaint.setColor(Color.parseColor(color));
    }


}
