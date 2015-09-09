package example.tiny.backetball;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import example.tiny.widget.ViewPagerIndicator;

/**
 * Created by tiny on 15-9-7.
 */
public class GameDetailActivity extends Activity {
    private TextView mTvTopGameName;
    private TextView mTvTopCollege;
    private TextView mTvTopLocation;
    private CheckBox mChkTopFollow;
    private ImageView mImgTopShare;
    private ImageView mImgBackground;

    private ViewPager mViewPager;
    private ViewPagerIndicator mIndicator;
    private List<Fragment> mTabContents = new ArrayList<Fragment>();
    private FragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        initViews();
        initDatas();
    }

    void initViews() {
        mTvTopGameName = (TextView)findViewById(R.id.tv_game_topview_gamename);
        mTvTopCollege = (TextView) findViewById(R.id.tv_game_topview_college);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_topview_game_location);
        mTvTopLocation = (TextView) ll.findViewById(R.id.tv_topview_location_text);
        mChkTopFollow = (CheckBox) findViewById(R.id.chk_topview_game_follow);
        mImgBackground = (ImageView)findViewById(R.id.img_topview_background);
        mViewPager = (ViewPager) findViewById(R.id.vp_game_detail);
        mIndicator = (ViewPagerIndicator) findViewById(R.id.vpindicator_game_indicator);
        mIndicator.setInnerViewPager(mViewPager, 0);
    }

    void initDatas() {
        Intent intent = getIntent();
        String college = intent.getStringExtra("College");
        String game_name = intent.getStringExtra("Name");
        String cover = intent.getStringExtra("CoverUrl");
        String campus = intent.getStringExtra("Campus");
        mTvTopGameName.setText(game_name);
        mTvTopCollege.setText(college);
        mTvTopLocation.setText(campus);

        ImageLoader.getInstance().displayImage(cover, mImgBackground, ((BasketBallApp) getApplication()).getBackGroundOptions());

        mTabContents.add(new TeamsFragment());
        mTabContents.add(new ScheduleFragment());
        mTabContents.add(new GameReportFragment());
        mAdapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public int getCount() {

                return mTabContents.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mTabContents.get(position);
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(mTabContents.size());



    }

}
