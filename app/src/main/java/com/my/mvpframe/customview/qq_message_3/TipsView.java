package com.my.mvpframe.customview.qq_message_3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Create by jzhan on 2018/12/10
 */
public class TipsView extends FrameLayout {
    private int startX;
    private int startY;
    private int x;
    private int y;
    private View tipImageView;
    private Path path;
    private Point fixCircle;// 固定圆的圆心
    private float radius = 20;
    private Paint paint;

    public TipsView(@NonNull Context context) {
        this(context, null);
    }

    public TipsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TipsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        setWillNotDraw(false);
        path = new Path();


        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(2);
        paint.setColor(0xffed5050);
    }

    public void attach(View attachView, Listener listener) {
        attach(attachView, new Func<View>() {
            @Override
            public View invoke() {
                Bitmap bm = view2Bitmap(attachView);
                ImageView iv = new ImageView(getContext());
                iv.setImageBitmap(bm);
                return iv;
            }
        }, listener);
    }


    void attach(View attachView, Func<View> copyViewCreator, Listener listener) {
        attachView.setOnTouchListener(new OnTouchListener() {

            void init() {
                tipImageView = copyViewCreator.invoke();

                tipImageView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                TipsView.this.addView(tipImageView);
                tipImageView.measure(0, 0);

                if (listener != null) {
                    listener.onStart();
                }

            }

            void destroy() {
                TipsView.this.removeView(tipImageView);
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i("TAG","ACTION_DOWN");
                        init();
                        startX = (int) attachView.getX();
                        startY = (int) attachView.getY();

                        fixCircle = new Point(startX,startY);

                        setX(attachView.getX());
                        setY(attachView.getY());
                        invalidate();
//                        onDraw();
                        break;
                    case MotionEvent.ACTION_MOVE:
//                        Log.i("TAG","x = "+event.getX()+"  y = "+event.getY());
                        setX(startX + event.getX() - tipImageView.getMeasuredWidth() / 2);
                        setY(startY + event.getY() - tipImageView.getMeasuredHeight() / 2);
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
//                        Log.i("TAG","ACTION_UP");
                        destroy();
                        if (listener != null) {
                            listener.onCancel();
                        }
                        break;
                }

                return true;
            }
        });
    }

    public Bitmap view2Bitmap(View view) {
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (fixCircle != null) {
            canvas.drawCircle(fixCircle.x, fixCircle.y, radius, paint);
        } else {
            Log.i("TAG","fixCircle  is  null");
        }

    }

    public interface Listener {
        void onStart();

        void onComplete();

        void onCancel();
    }

    public interface Func<Tresult> {
        Tresult invoke();
    }
}
