package example.tiny.backetball;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_detail);
        String objectId = getIntent().getStringExtra("objectId");
        Toast.makeText(LiveDetailActivity.this, objectId, Toast.LENGTH_SHORT).show();
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
        mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        mIndicator = (ViewPagerIndicator) findViewById(R.id.id_stickynavlayout_indicator);
    }


}
