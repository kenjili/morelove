package wjy.weiai.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;

/**
 * 圆形图片控件，用于展示头像
 * @author wjy
 */
public class CircleImageView extends NetworkImageView {

    private float width;
    private float height;
    private float radius;
    private Paint paint;
    private Matrix matrix;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        //设置抗锯齿
        paint.setAntiAlias(true);
        //初始化缩放矩阵
        matrix = new Matrix();
    }

    /**
     * 测量控件的宽高，并获取其内切圆的半径
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        radius = Math.min(width, height) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //将着色器设置给画笔
        BitmapShader bitmapShader = initBitmapShader();
        if(bitmapShader==null){
            super.onDraw(canvas);
            return;
        }
        paint.setShader(bitmapShader);
        //使用画笔在画布上画圆
        canvas.drawCircle(width / 2, height / 2, radius, paint);
    }

    /**
     * 获取ImageView中资源图片的Bitmap，
     * 利用Bitmap初始化图片着色器,
     * 通过缩放矩阵将原资源图片缩放到铺满整个绘制区域，避免边界填充
     */
    private BitmapShader initBitmapShader() {
        Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
        if(bitmap==null)return null;
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = Math.max(width / bitmap.getWidth(), height / bitmap.getHeight());
        matrix.setScale(scale, scale);
        //将图片宽高等比例缩放，避免拉伸
        bitmapShader.setLocalMatrix(matrix);
        return bitmapShader;
    }


}
