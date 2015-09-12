package example.tiny.backetball;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import example.tiny.widget.ViewPagerIndicator;

/**
 * Created by tiny on 15-9-7.
 */
public class GameDetailActivity extends Activity {
    public static final String GAME_OBJECT_ID = "GameObjectId";
    public static final String GAME_OBJECT_ENTRY = "GameObjectEntry";
    private TextView mTvTopGameName;
    private TextView mTvTopCollege;
    private TextView mTvTopLocation;
    private CheckBox mChkTopFollow;
    private ImageView mImgTopShare;
    private ImageView mImgBackground;
    private String mObjectId;
    private AVObject mCompetitionEntry;

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
        mImgTopShare = (ImageView) findViewById(R.id.img_topview_share);
        mIndicator.setInnerViewPager(mViewPager, 0);
    }

    void initDatas() {
        Intent intent = getIntent();
        String college = intent.getStringExtra("College");
        String game_name = intent.getStringExtra("Name");
        String cover = intent.getStringExtra("CoverUrl");
        String campus = intent.getStringExtra("Campus");
        mObjectId = intent.getStringExtra("GameId");
        mTvTopGameName.setText(game_name);
        mTvTopCollege.setText(college);
        mTvTopLocation.setText(campus);

        ImageLoader.getInstance().displayImage(cover, mImgBackground, ((BasketBallApp) getApplication()).getBackGroundOptions());




        //添加子Fragment
        Thread networkRequest = new Thread() {
            @Override
            public void run() {
                RequestGameDetail();
            }
        };

        try {
            networkRequest.start();
            networkRequest.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TeamsFragment teams = new TeamsFragment();
        ScheduleFragment schedule = new ScheduleFragment();
        GameReportFragment report = new GameReportFragment();
        Bundle arg = new Bundle();
        arg.putString(GAME_OBJECT_ID, mObjectId);
        arg.putString(GAME_OBJECT_ENTRY, mCompetitionEntry.toString());
        teams.setArguments(arg);
        schedule.setArguments(arg);
        report.setArguments(arg);
        mTabContents.add(teams);
        mTabContents.add(schedule);
        mTabContents.add(report);
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
        mImgTopShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameDetailActivity.this, SharePopupWindow.class);
                startActivity(intent);
            }
        });

    }

    void RequestGameDetail() {
        AVQuery<AVObject> query = new AVQuery<AVObject>("Game");
        try {
            mCompetitionEntry = query.get(mObjectId);
        } catch (AVException e) {
            e.printStackTrace();
        }
    }


}
