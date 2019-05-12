package wjy.morelove.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import wjy.morelove.widget.expandablebuttonmenu.ScreenHelper;

public class MeBgView extends View{

    private Paint paint;
    private Path path;

    public MeBgView(Context context) {
        super(context);
    }

    public MeBgView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MeBgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MeBgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(path==null){
            path = new Path();
            //起始顶点
            path.moveTo(0, 0);
            path.lineTo(0, 0);
            int thisWidth = getMeasuredWidth();
            int thisHeight = getMeasuredHeight();
            //第一个转点
            int zdX = thisWidth/3;
            path.lineTo(zdX,0);
            //第二个转点
            int zdX1 = thisWidth/2;
            int zdY2 = (int) ScreenHelper.dpToPx(getContext(),45);
            path.lineTo(zdX1,zdY2);
            //第三个点
            path.lineTo(thisWidth,zdY2);
            //封闭路径
            path.lineTo(thisWidth,thisHeight);
            path.lineTo(0,thisHeight);
            path.lineTo(0,0);
            path.close();

        }
        if(paint==null){
            paint = new Paint();
            //画笔颜色
            paint.setColor(getContext().getColor(android.R.color.white));
            //设置画笔为填充
            paint.setStyle(Paint.Style.FILL);
            //设置抗锯齿。
            paint.setAntiAlias(true);
        }
        canvas.drawPath(path, paint);

    }
}
