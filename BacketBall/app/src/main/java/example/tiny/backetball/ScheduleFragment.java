package example.tiny.backetball;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.List;

import example.tiny.adapter.ScheduleListAdapter;
import example.tiny.data.LiveItemData;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by tiny on 15-9-8.
 */
public class ScheduleFragment extends Fragment {
    private static final String LOG_TAG = "ScheduleFragment";
    StickyListHeadersListView mScheduleList = null;
    ScheduleListAdapter mScheduleListAdapter = null;
    String mObjectId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg = getArguments();
        mObjectId = arg.getString(GameDetailActivity.GAME_OBJECT_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        mScheduleList = (StickyListHeadersListView) view.findViewById(R.id.list_schedule_competition);
        mScheduleListAdapter = new ScheduleListAdapter(getActivity());
        mScheduleList.setAdapter(mScheduleListAdapter);
        RequestScheduleData();
        return view;
    }



    private void RequestScheduleData() {
        AVQuery<AVObject> query = new AVQuery<>("Competition");
        query.whereEqualTo("gameId", AVObject.createWithoutData("Game", mObjectId) );
        query.orderByDescending("level");
        query.orderByDescending("beginTime");
        query.include("scoreId");
        query.include("teamAId");
        query.include("teamBId");
        query.include("gameId");
        query.include("gameId.campusId");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(e == null)
                    InsertValueIntoAdapter(list);
                else
                    Toast.makeText(getActivity(), "Error:" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void InsertValueIntoAdapter(List<AVObject> list) {
        for(int i = 0; i < list.size(); i++) {
            AVObject item = list.get(i);
            LiveItemData insertItem = new LiveItemData();
            insertItem.setObjectId(item.getObjectId());
            insertItem.setBeginTime(item.getDate("beginTime"));

            AVObject teamA = item.getAVObject("teamAId");
            AVObject teamB = item.getAVObject("teamBId");
            String teamAName = teamA.getString("name");
            String teamALogo = teamA.getString("logoUrl");
            String teamBName = teamB.getString("name");
            String teamBLogo = teamB.getString("logoUrl");
            insertItem.setTeamAIconData(teamALogo);
            insertItem.setTeamANameData(teamAName);
            insertItem.setTeamBIconData(teamBLogo);
            insertItem.setTeamBNameData(teamBName);

            AVObject game = item.getAVObject("gameId");
            String gameName = "";
            String campus = "";
            if(game != null) {
                gameName = game.getString("name");
                AVObject campu = game.getAVObject("campusId");
                if(campu != null)
                    campus = campu.getString("name");
            }
            insertItem.setGameNameData(gameName);
            insertItem.setCampusData(campus);


            AVObject score = item.getAVObject("scoreId");
            int teamAScore = 0;
            int teamBScore = 0;
            if(score != null) {
                teamAScore = score.getInt("scoreA");
                teamBScore = score.getInt("scoreB");
            }
            String type = item.getString("type");
            String status = item.getString("status");
            insertItem.setTeamAScoreData(teamAScore);
            insertItem.setTeamBScoreData(teamBScore);
            insertItem.setCompetitionTypeData(type);
            insertItem.setCompetitionStatusData(status);

            mScheduleListAdapter.add(insertItem);
        }

        mScheduleListAdapter.notifyDataSetChanged();
    }
}
