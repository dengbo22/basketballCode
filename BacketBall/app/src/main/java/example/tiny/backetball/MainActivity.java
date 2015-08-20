package example.tiny.backetball;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by tiny on 15-8-19.
 */
public class MainActivity extends Activity{


    private final String LOG_TAG = "MainActivity";

    private static final int FRAGMENT_LIVE = 0;
    private static final int FRAGMENT_COMPETITION = 1;
    private static final int FRAGMENT_NEWS = 2;
    private static final int FRAGMENT_ME = 3;

    private final int NUM_PAGES = 4;
    private ViewPager mVpMainPager = null;
    private RadioGroup mRdoGrpMainBottom = null;
    private TextView mTvMainHeaderText = null;
    private ImageView mImgMainHeaderImage = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitWidget();
    }


    @Override
    protected void onStart() {
        Log.v(LOG_TAG, "onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.v(LOG_TAG, "onResume()");
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v(LOG_TAG, "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        Log.v(LOG_TAG, "onDestory()");
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        Log.v(LOG_TAG, "onRestart()");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        Log.v(LOG_TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.v(LOG_TAG, "onStop()");
        super.onStop();
    }

    public void InitWidget() {
        mVpMainPager = (ViewPager) findViewById(R.id.vp_main_pager);
        mRdoGrpMainBottom = (RadioGroup) findViewById(R.id.rdoGrp_main_bottom);
        PagerAdapter mVpMainPagerAdapter = new ScreenSlidePagerAdapter( getFragmentManager());
        mVpMainPager.setAdapter(mVpMainPagerAdapter);
        //设置初始页面
        mVpMainPager.setCurrentItem(FRAGMENT_LIVE);
        mVpMainPager.setOnPageChangeListener(new PageChangerListener());
        mVpMainPager.setOffscreenPageLimit(NUM_PAGES);
        mRdoGrpMainBottom.setOnCheckedChangeListener(new RadioChangeListener());
        //Header Layout
        mTvMainHeaderText = (TextView)findViewById(R.id.tv_header_text);
        mImgMainHeaderImage = (ImageView)findViewById(R.id.imgBtn_header_concern);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> fragments = null;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<Fragment>();
            fragments.add(FRAGMENT_LIVE, new LiveFragment());
            fragments.add(FRAGMENT_COMPETITION, new CompetitionFragment());
            fragments.add(FRAGMENT_NEWS, new NewsFragment());
            fragments.add(FRAGMENT_ME, new MeFragment());
        }

        @Override
        public Fragment getItem(int position) {
            if (position >= NUM_PAGES) {
                Log.e(LOG_TAG, "FragmentAdapter调用getItem时参数大于页面数");
                return null;
            } else
                return fragments.get(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }



    public class PageChangerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            int currentPage = mVpMainPager.getCurrentItem();
            switch (currentPage) {
                case FRAGMENT_LIVE:
                    mRdoGrpMainBottom.check(R.id.rdoBtn_main_live);
                    break;
                case FRAGMENT_COMPETITION:
                    mRdoGrpMainBottom.check(R.id.rdoBtn_main_competition);
                    break;
                case FRAGMENT_NEWS:
                    mRdoGrpMainBottom.check(R.id.rdoBtn_main_news);
                    break;
                case FRAGMENT_ME:
                    mRdoGrpMainBottom.check(R.id.rdoBtn_main_me);
                    break;
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            Log.i(LOG_TAG, "onPageScrolled() called!");
        }

        @Override
        public void onPageScrollStateChanged(int state) {
//            Log.i(LOG_TAG, "onPageScrollStateChanged() called!");
        }
    }

    public class RadioChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int currentPage = FRAGMENT_LIVE;
            switch (checkedId) {
                case R.id.rdoBtn_main_live:
                    mTvMainHeaderText.setText("直播");
                    mImgMainHeaderImage.setImageResource(R.drawable.drawable_imgbtn_header_live);
                    break;
                case R.id.rdoBtn_main_competition:
                    currentPage = FRAGMENT_COMPETITION;
                    mTvMainHeaderText.setText("赛事");
                    mImgMainHeaderImage.setImageResource(R.drawable.drawable_imgbtn_header_competition);
                    break;
                case R.id.rdoBtn_main_news:
                    currentPage = FRAGMENT_NEWS;
                    mTvMainHeaderText.setText("新闻");
                    break;
                case R.id.rdoBtn_main_me:
                    mTvMainHeaderText.setText("个人");
                    currentPage = FRAGMENT_ME;
                    break;
            }

            if (mVpMainPager.getCurrentItem() != currentPage) {
                mVpMainPager.setCurrentItem(currentPage);
            }
        }
    }

}
