package example.tiny.backetball;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import example.tiny.adapter.StickyListAdapter;
import example.tiny.data.BasketSQLite;
import example.tiny.data.LiveItemData;
import example.tiny.pulltorefreshstickylistview.PullToRefreshListView;
import example.tiny.pulltorefreshstickylistview.StickyListHeadersListView;

/**
 * Created by tiny on 15-8-19.
 */
public class LiveFragment extends Fragment {
    private static final String LOG_TAG = "LiveFragment";

    private static final int LOAD_SIZE = 3;

    StickyListHeadersListView liveList = null;
    StickyListAdapter liveListAdapter = null;
    RequestRefreshListener freshListener = null;
    ArrayList<LiveItemData> dataList = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity parent = (MainActivity) getActivity();
        liveListAdapter = new StickyListAdapter(parent);
        ArrayList<LiveItemData> dataInSQL = parent.getBasketSQLite().onGetAllInLive();
        if (dataInSQL != null) {
            for (LiveItemData data : dataInSQL) {
                liveListAdapter.getCompetitionData().add(0, data);
            }
        } else {
            Toast.makeText(getActivity(), "没有数据加载", Toast.LENGTH_SHORT).show();
            //此处应该请求数据
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live, container, false);
        liveList = (StickyListHeadersListView) view.findViewById(R.id.list_live_competition);
        liveList.setAdapter(liveListAdapter);
        liveList.setOnItemClickListener(new ItemClickListener());
        if (freshListener == null)
            freshListener = new RequestRefreshListener();
        liveList.setOnRefreshListener(freshListener);
        dataList = liveListAdapter.getCompetitionData();


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //获取数据库,删除过去数据并且当前最新的LOAD_SIZE条数据
        BasketSQLite sqlite = ((MainActivity) getActivity()).getBasketSQLite();
        sqlite.onDeleteAllInLive();
        if (dataList != null) {
            //如果dataList中的数据条数少于LOAD_SIZE,则将dataList中的所有数据均保存
            int len = dataList.size() < LOAD_SIZE ? dataList.size() : LOAD_SIZE;

            for (int i = 0; i < len; i++) {
                LiveItemData data = dataList.get(dataList.size() - 1 - i);
                sqlite.onInsertLiveData(data);
            }
        }

    }

    //将获取的数据插入到当前数据集合中
    private boolean insertDataIntoAdapter(List<AVObject> list) {
        //当前没有返回数据，返回插入失败
        if (list == null || list.size() == 0)
            return false;
        else {
            for (AVObject i : list) {
                //对列表中的每一个数据进行如下操作：
                boolean addFlag = true;
                //获取每一项数据内容：
                String objectId = i.getObjectId();
                Date date = i.getDate("beginTime");
                AVObject teamA = i.getAVObject("teamAId");
                AVObject teamB = i.getAVObject("teamBId");
                String teamA_name = teamA.getString("name");
                String teamA_logo = teamA.getString("logoUrl");
                String teamB_name = teamB.getString("name");
                String teamB_logo = teamB.getString("logoUrl");
                AVObject game = i.getAVObject("gameId");
                String gameName = " ";
                String campus = "";
                //执行异常检测
                if (game != null) {
                    gameName = game.getString("name");
                    AVObject c = game.getAVObject("campusId");
                    if (c != null) {
                        campus = c.getString("name");
                    } else {
                        Log.e(LOG_TAG, "Campus数据为NULL");
                    }
                } else {
                    Log.e(LOG_TAG, "game = null");
                }

                AVObject score = i.getAVObject("scoreId");
                int teamAScore = 0;
                int teamBScore = 0;
                if (score != null) {
                    teamAScore = score.getInt("scoreA");
                    teamBScore = score.getInt("scoreB");
                } else {
                    Log.e(LOG_TAG, "Score:" + score);
                }
                String type = i.getString("type");
                String status = i.getString("status");


                LiveItemData data = null;

                //数据比对，查看dataList中是否已经含有该条数据，如果有则addFlag设为false表示该条数据不插入
                for (LiveItemData j : dataList) {
                    if (j.getObjectId().equals(objectId)) {
                        data = j;
                        addFlag = false;
                        break;
                    }
                }

                if (data == null)
                    data = new LiveItemData();


                //Set Data
                data.setObjectId(objectId);
                data.setCampusData(campus);
                data.setBeginTime(date);
                data.setCompetitionTypeData(type);
                data.setTeamAIconData(teamA_logo);
                data.setTeamANameData(teamA_name);
                data.setTeamBIconData(teamB_logo);
                data.setTeamBNameData(teamB_name);
                data.setCompetitionStatusData(status);
                data.setGameNameData(gameName);
                data.setTeamAScoreData(teamAScore);
                data.setTeamBScoreData(teamBScore);

                if (addFlag)
                    dataList.add(data);


                //执行排序，保证列表不被打乱
                Collections.sort(dataList, new Comparator<LiveItemData>() {
                    @Override
                    public int compare(LiveItemData lhs, LiveItemData rhs) {
                        if (lhs.getBeginTime().after(rhs.getBeginTime()))
                            return 1;
                        else
                            return -1;

                    }
                });
                liveListAdapter.notifyDataSetChanged();
            }
            return true;
        }
    }

    //监听手动下拉动作，执行数据请求
    class RequestRefreshListener implements PullToRefreshListView.OnRefreshListener {
        @Override
        public void onRefresh() {
            AVQuery<AVObject> query = new AVQuery<>("Competition");
            query.orderByDescending("beginTime");
            Date begin;
            if (dataList != null && dataList.size() != 0)
                begin = dataList.get(0).getBeginTime();
            else
                begin = new java.util.Date();

            query.setLimit(LOAD_SIZE);
            query.whereLessThanOrEqualTo("beginTime", begin);
            query.include("scoreId");
            query.include("teamAId");
            query.include("teamBId");
            query.include("gameId");
            query.include("gameId.campusId");
            query.findInBackground(new RequestCallBack());
        }
    }

    //监听ListView点击事件，执行页面跳转
    class ItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (id == -1) {
                Toast.makeText(getActivity(), "你点击了Header/Footer!", Toast.LENGTH_SHORT).show();
                return;
            }
            int realPosition = (int) id;
            LiveItemData obj = dataList.get(realPosition);

            Intent intent = new Intent(getActivity(), LiveDetailActivity.class);
            intent.putExtra("objectId", obj.getObjectId());
            intent.putExtra("mGameNameData", obj.getGameNameData());
            intent.putExtra("mTeamAIconData", obj.getTeamAIconData());
            intent.putExtra("mTeamANameData", obj.getTeamANameData());
            intent.putExtra("mTeamBIconData", obj.getTeamBIconData());
            intent.putExtra("mTeamBNameData", obj.getTeamBNameData());
            intent.putExtra("mCompetitionTypeData", obj.getCompetitionTypeData());
            intent.putExtra("mTeamAScoreData", obj.getTeamAScoreData());
            intent.putExtra("mTeamBScoreData", obj.getTeamBScoreData());
            intent.putExtra("mCompetitionStatusData", obj.getCompetitionStatusData());

            startActivityForResult(intent, realPosition);
        }
    }

    //从Detail界面返回，更新对应列的数据
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int scoreA = data.getIntExtra("scoreA", -1);
        int scoreB = data.getIntExtra("scoreB", -1);
        String status = data.getStringExtra("status");
        String type = data.getStringExtra("type");
        LiveItemData changeData = liveListAdapter.getCompetitionData().get(requestCode);
        changeData.setTeamAScoreData(scoreA);
        changeData.setTeamBScoreData(scoreB);
        changeData.setCompetitionStatusData(status);
        changeData.setCompetitionTypeData(type);
        liveListAdapter.notifyDataSetChanged();
    }

    //数据获取完成以后的回调函数，用于进行数据插入和onRefreshComplete();
    class RequestCallBack extends FindCallback<AVObject> {
        @Override
        public void done(List<AVObject> list, AVException e) {
            if (e == null) {
                if (list.size() == 0)
                    Toast.makeText(getActivity(), "没有更多数据！", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(getActivity(), "获取数据成功！", Toast.LENGTH_SHORT).show();
                    insertDataIntoAdapter(list);
                }
            } else {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }

            liveList.onRefreshComplete();
        }
    }


}
