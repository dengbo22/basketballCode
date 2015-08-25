package example.tiny.backetball;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import example.tiny.widget.ViewPagerIndicator;

/**
 * Created by tiny on 15-8-21.
 */
public class LiveDetailActivity extends Activity {
    private List<Fragment> mTabContents = new ArrayList<Fragment>();
    private FragmentPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private List<String> mDatas = Arrays.asList("Data1", "Data2", "Data3");
    private ViewPagerIndicator mIndicator;
    private TextView mTvTopGameName;
    private ImageView mImgTopShare;
    private ImageView mImgTopTeamAIcon;
    private TextView mTvTopTeamAName;
    private ImageView mImgTopTeamBIcon;
    private TextView mTvTopTeamBName;
    private TextView mTvTopTeamAScore;
    private TextView mTvTopTeamBScore;
    private TextView mTvTopCompetitionType;
    private TextView mTvTopStatus;
    DisplayImageOptions options ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_detail);
        //设置加载图片的设置；
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_team_logo)
                .showImageForEmptyUri(R.drawable.default_team_logo)
                .showImageOnFail(R.drawable.default_team_logo)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.NONE)
                .bitmapConfig(Bitmap.Config.RGB_565)//设置为RGB565比起默认的ARGB_8888要节省大量的内存
                .delayBeforeLoading(100)//载入图片前稍做延时可以提高整体滑动的流畅度
                .build();


        String objectId = getIntent().getStringExtra("objectId");
        //使用获取到的objectId来请求数据



        //加载Fragment
        initView();;
        initDatas();
        mViewPager.setAdapter(mAdapter);
        mIndicator.setInnerViewPager(mViewPager, 0);
    }

    private void initDatas()
    {
        for (String data : mDatas) {
            CommentFragment fragment = new CommentFragment();
            mTabContents.add(fragment);
        }


        mAdapter = new FragmentPagerAdapter(getFragmentManager())
        {
            @Override
            public int getCount()
            {
                return mTabContents.size();
            }

            @Override
            public Fragment getItem(int position)
            {
                return mTabContents.get(position);
            }
        };
    }

    private void initView()
    {
        //加载基本控件
        mTvTopGameName = (TextView) findViewById(R.id.tv_topview_gamename);
        mImgTopShare = (ImageView) findViewById(R.id.img_topview_share);
        mTvTopTeamAScore = (TextView)findViewById(R.id.tv_topview_score_left);
        mTvTopTeamBScore = (TextView)findViewById(R.id.tv_topview_score_right);
        LinearLayout lTeam = (LinearLayout) findViewById(R.id.layout_topview_leftteam);
        mImgTopTeamAIcon = (ImageView) lTeam.findViewById(R.id.img_list_team_icon);
        mTvTopTeamAName = (TextView) lTeam.findViewById(R.id.tv_list_team_name);
        LinearLayout rTeam = (LinearLayout) findViewById(R.id.layout_topview_rightteam);
        mImgTopTeamBIcon = (ImageView) rTeam.findViewById(R.id.img_list_team_icon);
        mTvTopTeamBName = (TextView) rTeam.findViewById(R.id.tv_list_team_name);
        mTvTopCompetitionType = (TextView)findViewById(R.id.tv_topview_competitiontype);
        mTvTopStatus = (TextView)findViewById(R.id.tv_topview_status);
        mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        mIndicator = (ViewPagerIndicator) findViewById(R.id.id_stickynavlayout_indicator);
        //从Intent中获取信息并且加载内容：
        Intent intent = getIntent();
        mTvTopGameName.setText(intent.getStringExtra("mGameNameData"));
        mTvTopCompetitionType.setText(intent.getStringExtra("mCompetitionTypeData"));
        mTvTopStatus.setText(intent.getStringExtra("mCompetitionStatusData"));
        mTvTopTeamAName.setTextColor(Color.WHITE);
        mTvTopTeamBName.setTextColor(Color.WHITE);
        mTvTopTeamAName.setText(intent.getStringExtra("mTeamANameData"));
        mTvTopTeamBName.setText(intent.getStringExtra("mTeamBNameData"));
        mTvTopTeamAScore.setText(intent.getIntExtra("mTeamAScoreData", -1) +"");
        mTvTopTeamBScore.setText(intent.getIntExtra("mTeamBScoreData", -1) +"");
        String teamALogo = intent.getStringExtra("mTeamAIconData");
        String teamBLogo = intent.getStringExtra("mTeamBIconData");
        ImageLoader.getInstance().displayImage(teamALogo, mImgTopTeamAIcon, options);
        ImageLoader.getInstance().displayImage(teamBLogo, mImgTopTeamBIcon, options);


    }


}
