package com.pyoinsoo.retrofit.kotlin.customwidget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.pyoinsoo.retrofit.kotlin.R

/*
 * Created by pyoinsoo on 2017-01-10.
 * insoo.pyo@gmail.com
 *
 * 자외선/미세먼지를 나타내는 확장 원형프로그래스바
 */
class CircularProgressBar(context:Context,attrs: AttributeSet) : View(context, attrs){

    //현재 확장위젯의 기본속성값 Setting
    var progressValue = 0f
       set(value){
           if( value <= 100){
               field = value
           }else {
               field = 100f
           }
           invalidate()
       }
    private var foregroundStrokeWidth = resources.getDimension(R.dimen.default_foreground_stroke_width)
    private var backgroundStrokeWidth = resources.getDimension(R.dimen.default_background_stroke_width)

    var foregroundColor = Color.WHITE
        set(value){
            field = value
            foregroundPaint.color = field
            invalidate()
            requestLayout()
        }
    var backGroundColor = Color.GRAY
        set(value){
            field = value
            backgroundPaint.color = field
            invalidate()
            requestLayout()
        }

    //원을 그리기 위한 속성
    private val startAngle = -90
    private val rectF = RectF()
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val foregroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /*
     * Paint객체를 초기화 한다
     */
    init{
        val typedArray = context.theme.obtainStyledAttributes(attrs,R.styleable.CircularAttrs,0,0)
        progressValue = typedArray.getFloat(R.styleable.CircularAttrs_progress_value,progressValue)
        backgroundStrokeWidth = typedArray.getDimension(R.styleable.CircularAttrs_background_progressbar_width,
                                  backgroundStrokeWidth)
        foregroundStrokeWidth = typedArray.getDimension(R.styleable.CircularAttrs_foreground_progressbar_width,
                                  foregroundStrokeWidth)
        typedArray.recycle()

        // 백그라운드 초기화
        backgroundPaint.color = backGroundColor
        backgroundPaint.style = Paint.Style.STROKE
        backgroundPaint.strokeWidth = backgroundStrokeWidth

        // 포 그라운드 초기화
        foregroundPaint.color = foregroundColor
        foregroundPaint.style = Paint.Style.STROKE
        foregroundPaint.strokeWidth = foregroundStrokeWidth
    }
    override fun onMeasure(widthMeasureSpec:Int, heightMeasureSpec:Int){
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = getDefaultSize(suggestedMinimumHeight,heightMeasureSpec)

        val min = Math.min(width,height)
        setMeasuredDimension(min,min)

        val highStroke:Int
        if(foregroundStrokeWidth > backgroundStrokeWidth){
            highStroke = foregroundStrokeWidth.toInt()
        }else{
            highStroke = backgroundStrokeWidth.toInt()
        }
        rectF.set((0 + highStroke/2.0).toFloat() ,(0 + highStroke/2).toFloat(),
                (min-highStroke/2).toFloat(), (min -highStroke / 2).toFloat())
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawOval(rectF, backgroundPaint)
        /*
         * 생활 지수 값이 300을 넘을수 있기때문에
         * 백분율에 맞춘다
         */
        val angle = 360 * progressValue / 100
        canvas.drawArc(rectF, startAngle.toFloat(),angle,false,foregroundPaint)
    }
}