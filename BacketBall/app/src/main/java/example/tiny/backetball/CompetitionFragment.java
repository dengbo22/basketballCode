package example.tiny.backetball;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import example.tiny.adapter.PullRefreshAdapter;

/**
 * Created by tiny on 15-8-19.
 */
public class CompetitionFragment extends Fragment {
    private static final String LOG_TAG = "CompetitionFragment";
    private final int LOAD_SIZE = 3;
    PullToRefreshListView competitionList = null;
    PullRefreshAdapter competitionListAdapter = null;
    private ArrayList<CompetitionItemData> dataList = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_competition, container, false);
        competitionList = (PullToRefreshListView) view.findViewById(R.id.list_competition_allcompetition);
        competitionListAdapter = new PullRefreshAdapter(getActivity());
        competitionList.setAdapter(competitionListAdapter);
        dataList = competitionListAdapter.getGameData();
        competitionList.setOnRefreshListener(new RequestRefreshListener());
        return view;
    }


    private boolean addDataIntoCompetitionList(List<AVObject> list) {
        if (list == null || list.size() == 0)
            return false;
        else {
            for(AVObject item : list){
                String name = item.getString("name");
                String college = item.getString("college");
                boolean isFinish = item.getBoolean("isFinished");
                int follow = item.getInt("follows");
                String type = item.getString("type");
                AVObject campus = item.getAVObject("campusId");
                String campusName = campus.getString("name");
                String coverUrl = item.getString("coverUrl");

                CompetitionItemData data = new CompetitionItemData();
                data.setName(name);
                data.setCollege(college);
                data.setCampus(campusName);
                data.setFollowNumber(follow);
                data.setType(type);
                data.setIsFinished(isFinish);
                data.setCoverUrl(coverUrl);
                dataList.add(data);

            }

            competitionListAdapter.notifyDataSetChanged();
        }

        return true;
    }



    class RequestRefreshListener implements PullToRefreshBase.OnRefreshListener {
        @Override
        public void onRefresh(PullToRefreshBase refreshView) {
            AVQuery<AVObject> query = new AVQuery<>("Game");
            query.orderByDescending("follows");
            query.setLimit(LOAD_SIZE);
            query.setSkip(dataList.size());
            query.include("campusId");
            query.findInBackground(new FindCallback<AVObject> () {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (e == null) {
                        if(list.size() == 0)
                            Toast.makeText(getActivity(), "没有更多数据！", Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(getActivity(), "获取数据成功！", Toast.LENGTH_SHORT).show();
                            addDataIntoCompetitionList(list);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        Log.i(LOG_TAG, e + "");
                    }

                    competitionList.onRefreshComplete();
                    Log.i(LOG_TAG, dataList.size() + "size");
                }
            });
        }
    }
}
