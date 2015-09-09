package example.tiny.backetball;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import example.tiny.adapter.ReportRecyclerAdapter;

/**
 * Created by tiny on 15-9-8.
 */
public class GameReportFragment extends Fragment {
    RecyclerView mReportList = null;
    ReportRecyclerAdapter mReportListAdapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_report, container, false);
        mReportList = (RecyclerView) view.findViewById(R.id.recycler_game_report);
        mReportList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mReportListAdapter = new ReportRecyclerAdapter(getActivity());
        mReportList.setAdapter(mReportListAdapter);
        return view;
    }
}
