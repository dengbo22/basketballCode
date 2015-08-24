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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import example.tiny.adapter.PullRefreshAdapter;
import example.tiny.data.BasketSQLite;
import example.tiny.data.CompetitionItemData;

/**
 * Created by tiny on 15-8-19.
 */
public class CompetitionFragment extends Fragment {
    private static final String LOG_TAG = "CompetitionFragment";
    private final int LOAD_SIZE = 3;
    PullToRefreshListView competitionList = null;
    PullRefreshAdapter competitionListAdapter = null;
    private ArrayList<CompetitionItemData> dataList = null;
    private CompetitionRequestRefreshListener listener;
    boolean mIsFinishStatus = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在这里进行Adapter的初始化
        MainActivity father = (MainActivity) getActivity();
        competitionListAdapter = new PullRefreshAdapter(father);
        //读取数据库,加载数据库中的数据
        ArrayList<CompetitionItemData> dataInSQL = father.getBasketSQLite().onGetAllInCompetition();
        for (CompetitionItemData data : dataInSQL) {
            if (data.getIsFinished())
                competitionListAdapter.getFinishedGameData().add(data);
            else
                competitionListAdapter.getUnfinishedGameData().add(data);
        }
        //数据加载完毕
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_competition, container, false);
        competitionList = (PullToRefreshListView) view.findViewById(R.id.list_competition_allcompetition);
        competitionList.setAdapter(competitionListAdapter);
        mIsFinishStatus = competitionListAdapter.getFinishState();
        if (mIsFinishStatus)
            dataList = competitionListAdapter.getFinishedGameData();
        else
            dataList = competitionListAdapter.getUnfinishedGameData();
        if (listener == null)
            listener = new CompetitionRequestRefreshListener();
        competitionList.setOnRefreshListener(listener);


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在Fragment结束的时候将已有数据存入数据库中
        BasketSQLite sqlite = ((MainActivity) getActivity()).getBasketSQLite();
        sqlite.onDeleteAllInCompetition();
        if (competitionListAdapter.getUnfinishedGameData().size() >= LOAD_SIZE)
            for (int i = 0; i < LOAD_SIZE; i++)
                sqlite.onInsertCompetitionData(competitionListAdapter.getUnfinishedGameData().get(i));
        if (competitionListAdapter.getFinishedGameData().size() >= LOAD_SIZE)
            for (int i = 0; i < LOAD_SIZE; i++)
                sqlite.onInsertCompetitionData(competitionListAdapter.getFinishedGameData().get(i));
    }

    public void changeState(boolean newState) {
        if (competitionListAdapter != null) {
            competitionListAdapter.setFinishState(newState);
            mIsFinishStatus = newState;
            if (mIsFinishStatus)
                dataList = competitionListAdapter.getFinishedGameData();
            else
                dataList = competitionListAdapter.getUnfinishedGameData();

            if (dataList.size() == 0)
                listener.onRefresh(competitionList);

            competitionListAdapter.notifyDataSetChanged();
        }

    }

    private boolean addDataIntoCompetitionList(List<AVObject> list) {
        if (list == null || list.size() == 0)
            return false;
        else {
            for (AVObject item : list) {
                boolean addFlag = true;
                //获取全部的数据
                String Id = item.getObjectId();
                String name = item.getString("name");
                String college = item.getString("college");
                boolean isFinish = item.getBoolean("isFinished");
                int follow = item.getInt("follows");
                String type = item.getString("type");
                AVObject campus = item.getAVObject("campusId");
                String campusName = campus.getString("name");
                String coverUrl = item.getString("coverUrl");

                CompetitionItemData data = null;
                //查找是否已经有这条数据
                for(int i = 0; i < dataList.size() ; i++) {
                    if(dataList.get(i).getID().equals(Id)) {
                        //数据已经存在，此时直接更新该条数据：
                        data = dataList.get(i);
                        addFlag = false;
                        break;
                    }
                }
                //如果以上操作并未将data赋值，说明该条数据不存在
                if(data == null) {
                    data = new CompetitionItemData();
                    data.setID(Id);
                }

                data.setName(name);
                data.setCollege(college);
                data.setCampus(campusName);
                data.setFollowNumber(follow);
                data.setType(type);
                data.setIsFinished(isFinish);
                data.setCoverUrl(coverUrl);

                if(addFlag)
                    dataList.add(data);

            }
            //所有数据加载完毕以后，进行数据排序
            Collections.sort(dataList, new Comparator<CompetitionItemData>() {
                @Override
                public int compare(CompetitionItemData lhs, CompetitionItemData rhs) {
                    return rhs.getFollowNumber() - lhs.getFollowNumber();
                }
            });

            competitionListAdapter.notifyDataSetChanged();
        }
        return true;
    }

    private void requestCompetitionData() {
        AVQuery<AVObject> query = new AVQuery<>("Game");
        query.orderByDescending("follows");
        query.include("campusId");
        query.whereEqualTo("isFinished", mIsFinishStatus);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list.size() == 0)
                        Toast.makeText(getActivity(), "没有更多数据！", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(getActivity(), "获取数据成功！", Toast.LENGTH_SHORT).show();
                    }
                    addDataIntoCompetitionList(list);
                } else {
                    Toast.makeText(getActivity(), e + "", Toast.LENGTH_SHORT).show();
                }
                competitionList.onRefreshComplete();
            }
        });

    }

    class CompetitionRequestRefreshListener implements PullToRefreshBase.OnRefreshListener {
        @Override
        public void onRefresh(PullToRefreshBase refreshView) {
            if (((MainActivity) getActivity()).isOnline()) {
                requestCompetitionData();
            } else {
                //如果网络状况异常，则在延迟1秒后提示并完成刷新
                competitionList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络状态不可用", Toast.LENGTH_SHORT).show();
                        competitionList.onRefreshComplete();
                    }
                }, 1000);

            }
        }
    }
}
