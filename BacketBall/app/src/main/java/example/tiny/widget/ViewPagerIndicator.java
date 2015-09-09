package example.tiny.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import example.tiny.backetball.R;
import example.tiny.utils.ScreenUtils;

/**
 * Created by tiny on 15-8-24.
 */
public class ViewPagerIndicator extends LinearLayout {

    private static final int COUNT_DEFAULT_TAB = 3;

    private Context mContext;
    //---------------绘制三角形的参数------------
    private Paint mTrianglePaint;
    private Path mTrianglePath;
    private int mTriangleWidth;
    private int mTriangleHeight;
    private static final float RADIO_TRIANGEL = 1.0f / 6;
    private final int DIMENSION_TRIANGEL_WIDTH = (int) (getScreenWidth() / 3 * RADIO_TRIANGEL);
    //-----------------------------------------
    private int mTabVisibleCount = 3;
    int mColorGray = Color.parseColor("#808080");
    int mColorPink = Color.parseColor("#CB1A48");


    private ViewPager mViewPager;
    private InnerOnPageChangeListener mPageChangeListener;
    private int mInitTranslationX;
    private float mTranslationX;


    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mTabVisibleCount = a.getInt(R.styleable.ViewPagerIndicator_item_count, COUNT_DEFAULT_TAB);
        if (mTabVisibleCount < 0)
            mTabVisibleCount = COUNT_DEFAULT_TAB;
        a.recycle();
        mTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTrianglePaint.setColor(Color.parseColor("#CB1A48"));
        mTrianglePaint.setPathEffect(new CornerPathEffect(3));

        //
        if (mPageChangeListener == null)
            mPageChangeListener = new InnerOnPageChangeListener();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int cCount = getChildCount();
        if (cCount == 0)
            return;

        for (int i = 0; i < cCount; i++) {
            View view = getChildAt(i);
            LinearLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
            lp.weight = 0;
            lp.width = ScreenUtils.getScreenWidth(mContext) / mTabVisibleCount;
            view.setLayoutParams(lp);
        }
        setItemClickEvent();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTriangleWidth = (int) (w / mTabVisibleCount * RADIO_TRIANGEL);
        mTriangleWidth = Math.min(DIMENSION_TRIANGEL_WIDTH, mTriangleWidth);

        initTriangle();

        mInitTranslationX = getWidth() / mTabVisibleCount / 2 - mTriangleWidth / 2;
    }

    void setItemClickEvent() {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            final int j = i;
            View view = getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }

    private void initTriangle() {
        mTrianglePath = new Path();
        mTriangleHeight = (int) (mTriangleWidth / 2 / Math.sqrt(2.0));
        mTrianglePath.moveTo(0, 0);
        mTrianglePath.lineTo(mTriangleWidth, 0);
        mTrianglePath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
        mTrianglePath.close();
    }

    public int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 1);
        canvas.drawPath(mTrianglePath, mTrianglePaint);
        canvas.restore();

        super.dispatchDraw(canvas);
    }

    public void setInnerViewPager(ViewPager mViewPager, int pos) {
        this.mViewPager = mViewPager;
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        // 设置当前页
        mViewPager.setCurrentItem(pos);
        pointToImageView(pos);
    }


    protected void pointToImageView(int position) {
        ImageView commentView = (ImageView) getChildAt(0);
        ImageView reportView = (ImageView) getChildAt(1);
        ImageView statisticsView = (ImageView) getChildAt(2);

        switch (position) {
            case 0:
                commentView.setColorFilter(mColorPink);
                reportView.setColorFilter(mColorGray);
                statisticsView.setColorFilter(mColorGray);
                break;
            case 1:
                commentView.setColorFilter(mColorGray);
                reportView.setColorFilter(mColorPink);
                statisticsView.setColorFilter(mColorGray);
                break;
            case 2:
                commentView.setColorFilter(mColorGray);
                reportView.setColorFilter(mColorGray);
                statisticsView.setColorFilter(mColorPink);
                break;
        }
    }


    public void scroll(int position, float offset) {
        mTranslationX = getWidth() / mTabVisibleCount * (position + offset);

        int tabWidth = getScreenWidth() / mTabVisibleCount;
        if (offset > 0 && position >= (mTabVisibleCount - 2) && getChildCount() > mTabVisibleCount) {
            if (mTabVisibleCount != 1) {
                this.scrollTo(position - (mTabVisibleCount - 2) * tabWidth + (int) (tabWidth * offset), 0);
            } else {
                this.scrollTo(position * tabWidth + (int) (tabWidth * offset), 0);
            }
        }
        invalidate();
    }


    class InnerOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            scroll(position, positionOffset);
            if(listener != null) {
                listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            pointToImageView(position);
            if(listener != null) {
                listener.onPageSelected(position);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if(listener != null) {
                listener.onPageScrollStateChanged(state);
            }
        }
    }

    private IndicatorChangeListener listener;
    public interface IndicatorChangeListener {
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        public void onPageSelected(int position);

        public void onPageScrollStateChanged(int state);
    }

    public void setIndicatorChangeListener(IndicatorChangeListener indicatorChangeListener) {
        listener = indicatorChangeListener;
    }
}
