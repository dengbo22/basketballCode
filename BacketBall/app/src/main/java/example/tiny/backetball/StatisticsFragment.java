package example.tiny.backetball;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;

import java.util.ArrayList;
import java.util.List;

import example.tiny.adapter.SimpleAdapter;
import example.tiny.adapter.StatisticsListAdapter;

/**
 * Created by tiny on 15-8-30.
 */
public class StatisticsFragment extends Fragment {
    private static final String LOG_TAG = "StatisticsFragment";
    RecyclerView mRecycleView = null;


    //请求数据，设置基本参数
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        RequestStatisticsData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        mRecycleView = (RecyclerView) view.findViewById(R.id.recycler_statistics);
        mRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), SimpleAdapter.mPlayerSpan * SimpleAdapter.mScoreSpan));

        //This is the code to provide a sectioned grid
        List<StatisticsListAdapter.Section> sections =
                new ArrayList<StatisticsListAdapter.Section>();

        //Sections
        sections.add(new StatisticsListAdapter.Section(0, "得分统计"));
        sections.add(new StatisticsListAdapter.Section(SimpleAdapter.mPlayerSpan * SimpleAdapter.SCORE_HEIGHT, "11级A班球员数据"));
        sections.add(new StatisticsListAdapter.Section(SimpleAdapter.mPlayerSpan * SimpleAdapter.SCORE_HEIGHT
                + SimpleAdapter.PLAYER_A_HEIGHT * SimpleAdapter.mScoreSpan, "11级B班球员数据"));

        //Add your adapter to the sectionAdapter
        StatisticsListAdapter.Section[] dummy = new StatisticsListAdapter.Section[sections.size()];
        StatisticsListAdapter mSectionedAdapter = new
                StatisticsListAdapter(getActivity(), mRecycleView);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        //Apply this adapter to the RecyclerView
        mRecycleView.setAdapter(mSectionedAdapter);


        return view;
    }

    private void RequestStatisticsData() {
        AVObject data = null;
        AVQuery<AVObject> query = new AVQuery<>("Competition");
        query.getInBackground(((LiveDetailActivity) getActivity()).objectId, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    if (avObject != null) {
                        String statisticsString = avObject.getString("statistics");

                        JSONObject obj = JSON.parseObject(statisticsString);
                        JSONArray statistics = obj.getJSONArray("statistics");
                        JSONArray playerA = obj.getJSONArray("playerA");
                        JSONArray playerB = obj.getJSONArray("playerB");
                        SimpleAdapter.SCORE_HEIGHT = statistics.size();
                        SimpleAdapter.PLAYER_A_HEIGHT = playerA.size();
                        SimpleAdapter.PLAYER_B_HEIGHT = playerB.size();

                        Log.e(LOG_TAG, "statistics:" + statistics.size() + "\t" + playerA.size() + "\t" + playerB.size());
                    }
                } else {
                    Log.e(LOG_TAG, "Exception in Request");
                }

            }
        });


    }


}
