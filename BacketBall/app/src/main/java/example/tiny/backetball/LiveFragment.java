package example.tiny.backetball;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import example.tiny.adapter.StickyListAdapter;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live, container, false);
        liveList = (StickyListHeadersListView) view.findViewById(R.id.list_live_competition);
        liveListAdapter = new StickyListAdapter(getActivity());
        liveList.setAdapter(liveListAdapter);
        liveList.setOnItemClickListener(new ItemClickListener());
        if (freshListener == null)
            freshListener = new RequestRefreshListener();
        liveList.setOnRefreshListener(freshListener);
        dataList = liveListAdapter.getCompetitionData();


        return view;
    }





    private boolean insertDataIntoAdapter(List<AVObject> list) {
        if (list == null || list.size() == 0)
            return false;
        else {
            for (AVObject i : list) {
                //Get Data
                LiveItemData data = new LiveItemData();
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
                if(game != null) {
                    gameName = game.getString("name");
                    AVObject c = game.getAVObject("campusId");
                    if(c != null) {
                        campus = c.getString("name");
                    }else {
                        Log.e(LOG_TAG, "Campus:" + campus);
                    }
                }else {
                    Log.e(LOG_TAG, "game = " + game);
                }

                AVObject score = i.getAVObject("scoreId");
                int teamAScore = 0;
                int teamBScore = 0;
                if(score != null) {
                    teamAScore = score.getInt("scoreA");
                    teamBScore = score.getInt("scoreB");
                }else {
                    Log.e(LOG_TAG, "Score:" + score);
                }
                String type = i.getString("type");
                String status = i.getString("status");


                //Set Data
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

                dataList.add(0,data);
            }
            return true;
        }
    }


    class RequestRefreshListener implements PullToRefreshListView.OnRefreshListener {
        @Override
        public void onRefresh() {
            AVQuery<AVObject> query = new AVQuery<>("Competition");
            query.orderByDescending("beginTime");
            query.setLimit(LOAD_SIZE);
            query.setSkip(dataList.size());
            query.include("scoreId");
            query.include("teamAId");
            query.include("teamBId");
            query.include("gameId");
            query.include("gameId.campusId");
            query.findInBackground(
                    new FindCallback<AVObject>() {
                        @Override
                        public void done(List<AVObject> list, AVException e) {
                            if (e == null) {
                                if(list.size() == 0)
                                    Toast.makeText(getActivity(), "没有更多数据！", Toast.LENGTH_SHORT).show();
                                else {
                                    Toast.makeText(getActivity(), "获取数据成功！", Toast.LENGTH_SHORT).show();
                                    insertDataIntoAdapter(list);
                                }
                            } else {
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                Log.i(LOG_TAG, e + "");
                            }

                            liveList.onRefreshComplete();
                        }
                    }

            );
        }
    }

    class ItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), LiveDetailActivity.class);
            startActivity(intent);
        }
    }
}
