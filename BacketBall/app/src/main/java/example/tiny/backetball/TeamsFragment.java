package example.tiny.backetball;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import example.tiny.adapter.TeamRecyclerAdapter;

/**
 * Created by tiny on 15-9-8.
 */
public class TeamsFragment extends Fragment {
    RecyclerView mTeamList = null;
    TeamRecyclerAdapter mTeamListAdapter = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teams, container, false);
        mTeamList = (RecyclerView) view.findViewById(R.id.recycler_teams);
        mTeamList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTeamListAdapter = new TeamRecyclerAdapter(getActivity());
        mTeamList.setAdapter(mTeamListAdapter);
        return view;
    }
}
