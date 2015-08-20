package example.tiny.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RadioButton;

import example.tiny.backetball.R;


/**
 * Created by tiny on 15-8-19.
 */
public class ImageRadioButton extends RadioButton {
    int mDrawableSize = 0;


    public ImageRadioButton(Context context) {
        this(context, null);
    }

    public ImageRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable drawableLeft = null, drawableTop = null, drawableRight = null, drawableBottom = null;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ImageRadioButton);

        mDrawableSize = array.getDimensionPixelSize(R.styleable.ImageRadioButton_drawableImageSize, 50);
        Log.i("ImageRadioButton", mDrawableSize +"");
        drawableTop = array.getDrawable(R.styleable.ImageRadioButton_drawableTop);
        Log.i("ImageRadioButton", drawableTop +"");
        drawableBottom = array.getDrawable(R.styleable.ImageRadioButton_drawableBottom);
        drawableLeft = array.getDrawable(R.styleable.ImageRadioButton_drawableLeft);
        drawableRight = array.getDrawable(R.styleable.ImageRadioButton_drawableRight);

        array.recycle();
        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {

        if (left != null) {
            left.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (right != null) {
            right.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (top != null) {
            top.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        setCompoundDrawables(left, top, right, bottom);
    }
}
