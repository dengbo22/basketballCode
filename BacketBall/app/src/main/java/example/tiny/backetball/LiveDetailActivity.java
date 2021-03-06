package example.tiny.backetball;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import example.tiny.widget.ViewPagerIndicator;

/**
 * Created by tiny on 15-8-21.
 */
public class LiveDetailActivity extends Activity {
    public static final String BUNDLE_COMPETITION_KEY = "competitionId";
    public static final int DETAIL_FRAGMENT_COMMENT = 0;
    public static final int DETAIL_FRAGMENT_REPORT = 1;
    public static final int DETAIL_FRAGMENT_STATISTICS = 2;
    public static final int PAGE_NUMBER = 3;
    private static final String LOG_TAG = "LiveDetailActivity";
    private ProgressDialog mProgress;
    private List<Fragment> mTabContents = new ArrayList<Fragment>();
    private FragmentPagerAdapter mAdapter;
    private ViewPager mViewPager;
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
    private CheckBox mChkTopLeftUpvote;
    private TextView mTvTopLeftFollowNumber;
    private CheckBox mChkTopRightUpvote;
    private TextView mTvTopRightFollowNumber;
    private TextView mTvTopAward;
    private LinearLayout mLlayoutComment;
    private ImageView mImgCommentEmotion;
    private EditText mEdtTxtComment;
    private TextView mTvCommentSend;
    public String objectId;
    private SharePopupWindow mShareWindow;
    private int mSupport;

    public EditText getEdtTxtComment() {
        return mEdtTxtComment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_detail);
        //设置加载图片的设置；


        mProgress = new ProgressDialog(this);
        mProgress.setMessage("正在加载数据...");
        mProgress.setTitle("请求数据");

        objectId = getIntent().getStringExtra("objectId");
        //使用获取到的objectId来请求数据
        RequestTopViewData(objectId);
        //加载Fragment
        initView();
        initDatas();
        initEvent();

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(PAGE_NUMBER);
        mIndicator.setIndicatorChangeListener(new LiveDetailActivityDefaultViewPagerListener());
        mIndicator.setInnerViewPager(mViewPager, 0);

    }


    private void initDatas() {
        CommentFragment comment = new CommentFragment();
        ReportFragment report = new ReportFragment();
        Bundle competitionId = new Bundle();
        competitionId.putString(BUNDLE_COMPETITION_KEY, objectId);
        comment.setArguments(competitionId);
        report.setArguments(competitionId);

        mTabContents.add(comment);
        mTabContents.add(report);
        mTabContents.add(new StatisticsFragment());

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
    }


    public TextView getTvCommentSend() {
        return mTvCommentSend;
    }

    private void initView() {
        //加载基本控件
        mTvTopGameName = (TextView) findViewById(R.id.tv_topview_gamename);
        mImgTopShare = (ImageView) findViewById(R.id.img_topview_share);
        mTvTopTeamAScore = (TextView) findViewById(R.id.tv_topview_score_left);
        mTvTopTeamBScore = (TextView) findViewById(R.id.tv_topview_score_right);
        LinearLayout lTeam = (LinearLayout) findViewById(R.id.layout_topview_leftteam);
        mImgTopTeamAIcon = (ImageView) lTeam.findViewById(R.id.img_list_team_icon);
        mTvTopTeamAName = (TextView) lTeam.findViewById(R.id.tv_list_team_name);
        LinearLayout rTeam = (LinearLayout) findViewById(R.id.layout_topview_rightteam);
        mImgTopTeamBIcon = (ImageView) rTeam.findViewById(R.id.img_list_team_icon);
        mTvTopTeamBName = (TextView) rTeam.findViewById(R.id.tv_list_team_name);
        mTvTopCompetitionType = (TextView) findViewById(R.id.tv_topview_competitiontype);
        mTvTopStatus = (TextView) findViewById(R.id.tv_topview_status);
        mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        mIndicator = (ViewPagerIndicator) findViewById(R.id.id_stickynavlayout_indicator);
        //几个无法从本地获取信息的控件初始化
        LinearLayout lUpvote = (LinearLayout) findViewById(R.id.layout_topview_leftupvote);
        mChkTopLeftUpvote = (CheckBox) lUpvote.findViewById(R.id.chk_follow_upvote);
        mTvTopLeftFollowNumber = (TextView) lUpvote.findViewById(R.id.tv_follow_follownumber);
        LinearLayout rUpvote = (LinearLayout) findViewById(R.id.layout_topview_rightupvote);
        mChkTopRightUpvote = (CheckBox) rUpvote.findViewById(R.id.chk_follow_upvote);
        mTvTopRightFollowNumber = (TextView) rUpvote.findViewById(R.id.tv_follow_follownumber);
        LinearLayout awardLayout = (LinearLayout) findViewById(R.id.layout_topview_award);
        mTvTopAward = (TextView) awardLayout.findViewById(R.id.tv_topview_award);

        mEdtTxtComment = (EditText) findViewById(R.id.edtTxt_comment_input);
        mTvCommentSend = (TextView) findViewById(R.id.tv_comment_send);


        //从Intent中获取信息并且加载内容：
        Intent intent = getIntent();
        mTvTopGameName.setText(intent.getStringExtra("mGameNameData"));
        mTvTopCompetitionType.setText(intent.getStringExtra("mCompetitionTypeData"));
        mTvTopStatus.setText(intent.getStringExtra("mCompetitionStatusData"));
        mTvTopTeamAName.setTextColor(Color.WHITE);
        mTvTopTeamBName.setTextColor(Color.WHITE);
        mTvTopTeamAName.setText(intent.getStringExtra("mTeamANameData"));
        mTvTopTeamBName.setText(intent.getStringExtra("mTeamBNameData"));
        mTvTopTeamAScore.setText(intent.getIntExtra("mTeamAScoreData", -1) + "");
        mTvTopTeamBScore.setText(intent.getIntExtra("mTeamBScoreData", -1) + "");
        String teamALogo = intent.getStringExtra("mTeamAIconData");
        String teamBLogo = intent.getStringExtra("mTeamBIconData");
        ImageLoader.getInstance().displayImage(teamALogo, mImgTopTeamAIcon, BasketBallApp.getTeamIconOptions());
        ImageLoader.getInstance().displayImage(teamBLogo, mImgTopTeamBIcon, BasketBallApp.getTeamIconOptions());
        //加载底部评论
        mLlayoutComment = (LinearLayout) findViewById(R.id.layout_detail_comment);
        mImgCommentEmotion = (ImageView) mLlayoutComment.findViewById(R.id.img_comment_emotion);
        mEdtTxtComment = (EditText) mLlayoutComment.findViewById(R.id.edtTxt_comment_input);

    }

    private void initEvent() {
        mChkTopLeftUpvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AVUser.getCurrentUser() == null || AVUser.getCurrentUser().isAnonymous()) {
                    Intent intent = new Intent(LiveDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                    mChkTopLeftUpvote.setChecked(!mChkTopLeftUpvote.isChecked());
                    return;
                }

                ChangeVote(mTvTopLeftFollowNumber, mChkTopLeftUpvote.isChecked());
                if (mChkTopRightUpvote.isChecked()) {
                    mChkTopRightUpvote.setChecked(false);
                    ChangeVote(mTvTopRightFollowNumber, mChkTopRightUpvote.isChecked());
                }
                if (mChkTopLeftUpvote.isChecked())
                    mSupport = 1;
                else if (mChkTopRightUpvote.isChecked())
                    mSupport = 2;
                else
                    mSupport = 0;

                //联网请求保存数据
                new Thread() {
                    @Override
                    public void run() {
                        SaveSupportData();
                    }
                }.start();

            }
        });

        mChkTopRightUpvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AVUser.getCurrentUser() == null || AVUser.getCurrentUser().isAnonymous()) {
                    Intent intent = new Intent(LiveDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                    mChkTopRightUpvote.setChecked(!mChkTopRightUpvote.isChecked());
                    return;
                }

                ChangeVote(mTvTopRightFollowNumber, mChkTopRightUpvote.isChecked());
                if (mChkTopLeftUpvote.isChecked()) {
                    mChkTopLeftUpvote.setChecked(false);
                    ChangeVote(mTvTopLeftFollowNumber, mChkTopLeftUpvote.isChecked());
                }

                if (mChkTopLeftUpvote.isChecked())
                    mSupport = 1;
                else if (mChkTopRightUpvote.isChecked())
                    mSupport = 2;
                else
                    mSupport = 0;

                //联网请求保存数据
                new Thread() {
                    @Override
                    public void run() {
                        SaveSupportData();
                    }
                }.start();

            }
        });


        mImgTopShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LiveDetailActivity.this, SharePopupWindow.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //用于启动以后开始请求数据
    private void RequestTopViewData(String objId) {
        mProgress.show();
        AVQuery<AVObject> query = new AVQuery<>("Competition");
        query.whereEqualTo("objectId", objId);
        query.include("reportId");
        query.include("gameId");
        query.include("scoreId");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    for (AVObject i : list) {
                        UpdateTopViewData(list);
                    }
                }

            }
        });

    }


    //用户加载请求后的TopView数据
    private void UpdateTopViewData(List<AVObject> list) {
        if (list.size() == 1) {
            AVObject item = list.get(0);
            String status = item.getString("status");
            AVObject score = item.getAVObject("scoreId");
            int scoreA = 0;
            int scoreB = 0;
            if (score != null) {
                scoreA = score.getInt("scoreA");
                scoreB = score.getInt("scoreB");
            } else {
                Log.w(LOG_TAG, "获取到Score为NULL");
            }
            String type = item.getString("type");
            int likesA = item.getInt("likesA");
            int likesB = item.getInt("likesB");
            int award = item.getInt("award");
            //设置控件
            mTvTopStatus.setText(status + "");
            mTvTopTeamAScore.setText(scoreA + "");
            mTvTopTeamBScore.setText(scoreB + "");
            mTvTopCompetitionType.setText(type);
            mTvTopLeftFollowNumber.setText(likesA + "");
            mTvTopRightFollowNumber.setText(likesB + "");
            mTvTopAward.setText("￥ " + award);
            //获取支持状态
            AVQuery<AVObject> query = new AVQuery<AVObject>("TeamFollow");
            query.whereEqualTo("userId", AVUser.getCurrentUser());
            query.whereEqualTo("competitionId", item);
            query.findInBackground(new FindCallback<AVObject>() {
                                       @Override
                                       public void done(List<AVObject> list, AVException e) {
                                           if (list.size() == 1 && e == null) {
                                               int support = list.get(0).getInt("team");
                                               if(support == 1)
                                                   mChkTopLeftUpvote.setChecked(true);
                                               if(support == 2)
                                                   mChkTopRightUpvote.setChecked(true);
                                           }
                                           mProgress.dismiss();
                                       }
                                   }
            );


        } else {
            Log.e(LOG_TAG, "一个ObjectId有多个对象,返回超过1个内容！");
        }

    }

    /*
    修改投票情况
    text 需要修改内容的textView
    isAdd true表示需要+1,false表示需要-1
    */

    private void ChangeVote(TextView text, boolean isAdd) {
        String lText = (String) text.getText();
        int textVote = Integer.parseInt(lText.trim());
        if (isAdd)
            ++textVote;
        else
            --textVote;

        text.setText(textVote + "");
    }

    private void SaveSupportData() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("competitionId", objectId);
        parameters.put("team", mSupport);
        try {
            Object result = AVCloud.callFunction("teamfollow", parameters);
            Log.e(LOG_TAG, "result:" + result);
        } catch (AVException e) {
            e.printStackTrace();
        }

        //Update奖金池内容

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra("scoreA", Integer.parseInt((String) mTvTopTeamAScore.getText()));
        intent.putExtra("scoreB", Integer.parseInt((String) mTvTopTeamBScore.getText()));
        intent.putExtra("status", (String) mTvTopStatus.getText());
        intent.putExtra("type", (String) mTvTopCompetitionType.getText());
        setResult(RESULT_OK, intent);

        super.onBackPressed();
    }

    class LiveDetailActivityDefaultViewPagerListener implements ViewPagerIndicator.IndicatorChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case DETAIL_FRAGMENT_COMMENT:
                    mLlayoutComment.setVisibility(View.VISIBLE);
                    break;
                case DETAIL_FRAGMENT_REPORT:
                    mLlayoutComment.setVisibility(View.GONE);
                    break;
                case DETAIL_FRAGMENT_STATISTICS:
                    mLlayoutComment.setVisibility(View.GONE);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


}
