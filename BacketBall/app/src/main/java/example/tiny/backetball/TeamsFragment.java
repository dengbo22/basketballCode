package example.tiny.backetball;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.FindCallback;

import java.util.List;

import example.tiny.adapter.TeamRecyclerAdapter;
import example.tiny.data.Team;

/**
 * Created by tiny on 15-9-8.
 */
public class TeamsFragment extends Fragment {
    private static final String LOG_TAG = "TeamsFragment";
    RecyclerView mTeamList = null;
    TeamRecyclerAdapter mTeamListAdapter = null;
    String mObjectId;
    AVObject mCompetitionEntry ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mObjectId = getArguments().getString(GameDetailActivity.GAME_OBJECT_ID);
        String entry = getArguments().getString(GameDetailActivity.GAME_OBJECT_ENTRY);
        try {
            mCompetitionEntry = AVObject.parseAVObject(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teams, container, false);
        mTeamList = (RecyclerView) view.findViewById(R.id.recycler_teams);
        mTeamList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTeamListAdapter = new TeamRecyclerAdapter(getActivity());
        mTeamList.setAdapter(mTeamListAdapter);
        RequestTeamsData();
        return view;
    }


    private void RequestTeamsData() {
        AVRelation<AVObject> relation = mCompetitionEntry.getRelation("teams");
        relation.getQuery().findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(e == null)
                    InsertIntoTeamsAdapter(list);
                else
                    Toast.makeText(getActivity(), "Error:" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void InsertIntoTeamsAdapter(List<AVObject> list) {
        for(int i = 0; i < list.size(); i++) {
            mTeamListAdapter.add(Team.FromAVObject(list.get(i)));
        }
        mTeamListAdapter.notifyDataSetChanged();
    }
}
