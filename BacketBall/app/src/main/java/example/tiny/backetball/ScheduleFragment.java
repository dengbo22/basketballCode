package example.tiny.backetball;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by tiny on 15-9-8.
 */
public class ScheduleFragment extends Fragment {
    StickyListHeadersListView mScheduleList = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        mScheduleList = (StickyListHeadersListView) view.findViewById(R.id.list_schedule_competition);
        return view;
    }
}
