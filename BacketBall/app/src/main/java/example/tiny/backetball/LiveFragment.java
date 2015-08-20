package example.tiny.backetball;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import example.tiny.adapter.StickyListAdapter;
import example.tiny.pulltorefreshstickylistview.StickyListHeadersListView;

/**
 * Created by tiny on 15-8-19.
 */
public class LiveFragment extends Fragment{
    StickyListHeadersListView liveList = null;
    StickyListAdapter liveListAdapter = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live, container, false);
        liveList = (StickyListHeadersListView)view.findViewById(R.id.list_live_competition);
        liveListAdapter = new StickyListAdapter(getActivity());
        liveList.setAdapter(liveListAdapter);
        return view;
    }
}
