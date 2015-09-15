package example.tiny.backetball;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;

import java.util.ArrayList;

import example.tiny.data.BasketSQLite;
import example.tiny.data.CompetitionItemData;


/**
 * Created by tiny on 15-8-19.
 */
public class MainActivity extends Activity{

    private final String LOG_TAG = "MainActivity";

    static final int FRAGMENT_LIVE = 0;
    static final int FRAGMENT_COMPETITION = 1;
    static final int FRAGMENT_NEWS = 2;
    static final int FRAGMENT_ME = 3;

    private final int NUM_PAGES = 4;
    private ViewPager mVpMainPager = null;
    private RadioGroup mRdoGrpMainBottom = null;
    private TextView mTvMainHeaderText = null;
    private ImageView mImgMainHeaderImage = null;
    private RadioGroup mChkMainHeaderFinished = null;
    private LiveFragment mLive = null;
    private CompetitionFragment mCompetition = null;
    private int mCurrentPage = 0;

    public BasketSQLite getBasketSQLite() {
        return dbHelper;
    }

    private BasketSQLite dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitWidget();
        dbHelper = new BasketSQLite(this, "BasketApp.db", null, 1);

        //如果尚未登陆，则跳转到登陆界面
        if(AVUser.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AVUser.logOut();
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
        mChkMainHeaderFinished = (RadioGroup)findViewById(R.id.rdoGrp_header_finished);
        mChkMainHeaderFinished.setOnCheckedChangeListener(new CheckChangeListener());

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
            if(mLive == null)
                mLive = new LiveFragment();
            fragments.add(FRAGMENT_LIVE, mLive);
            if(mCompetition == null)
                mCompetition = new CompetitionFragment();
            fragments.add(FRAGMENT_COMPETITION, mCompetition );

            fragments.add(FRAGMENT_NEWS, new NewsFragment());
            fragments.add(FRAGMENT_ME, new MeFragment());
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(LOG_TAG, "getItem" + position);
            if (position >= NUM_PAGES) {
                Log.e(LOG_TAG, "FragmentAdapter调用getItem时参数大于页面数");
                return null;
            } else
                return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


    }


    public class PageChangerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            mCurrentPage = mVpMainPager.getCurrentItem();
            switch (mCurrentPage) {
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
//            Log.i(LOG_TAG, "onPageScrollStateChanged() called!\tstate:" + state);
        }
    }

    public class RadioChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rdoBtn_main_live:
                    //修改Header的内容
                    if(mTvMainHeaderText.getVisibility() == View.GONE){
                        mTvMainHeaderText.setVisibility(View.VISIBLE);
                        mTvMainHeaderText.setText("直播");
                        mChkMainHeaderFinished.setVisibility(View.GONE);
                    }
                    mImgMainHeaderImage.setImageResource(R.drawable.drawable_imgbtn_header_live);

                    mCurrentPage = FRAGMENT_LIVE;
                    break;
                case R.id.rdoBtn_main_competition:
                    //修改Header的内容
                    mTvMainHeaderText.setVisibility(View.GONE);
                    mChkMainHeaderFinished.setVisibility(View.VISIBLE);
                    mImgMainHeaderImage.setImageResource(R.drawable.drawable_imgbtn_header_competition);

                    mCurrentPage = FRAGMENT_COMPETITION;
                    break;
                case R.id.rdoBtn_main_news:
                    if(mTvMainHeaderText.getVisibility() == View.GONE){
                        mTvMainHeaderText.setVisibility(View.VISIBLE);
                        mChkMainHeaderFinished.setVisibility(View.GONE);
                    }
                    mTvMainHeaderText.setText("新闻");

                    mCurrentPage = FRAGMENT_NEWS;
                    break;
                case R.id.rdoBtn_main_me:
                    if(mTvMainHeaderText.getVisibility() == View.GONE){
                        mTvMainHeaderText.setVisibility(View.VISIBLE);
                        mChkMainHeaderFinished.setVisibility(View.GONE);
                    }
                    mTvMainHeaderText.setText("个人");
                    mCurrentPage = FRAGMENT_ME;
                    break;
            }

            if (mVpMainPager.getCurrentItem() != mCurrentPage) {
                mVpMainPager.setCurrentItem(mCurrentPage);
            }
        }
    }

    class CheckChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rdoBtn_header_finished:
                    mCompetition.changeState(true);
                    break;
                case R.id.rdoBtn_header_unfinished:
                    mCompetition.changeState(false);
                    break;

                default:
                    Log.e(LOG_TAG, "RadioButton选中出现异常！");
            }
        }
    }

}
