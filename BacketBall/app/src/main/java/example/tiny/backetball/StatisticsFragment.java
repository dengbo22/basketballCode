package example.tiny.backetball;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import example.tiny.adapter.StatisticsListAdapter;

/**
 * Created by tiny on 15-8-30.
 */
public class StatisticsFragment extends Fragment {
    private static final String LOG_TAG = "StatisticsFragment";
    RecyclerView mRecycleView = null;
    StatisticsListAdapter mSectionedAdapter;


    //请求数据，设置基本参数
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        mRecycleView = (RecyclerView) view.findViewById(R.id.recycler_statistics);
        mSectionedAdapter = new StatisticsListAdapter(getActivity(), mRecycleView);
        mRecycleView.setAdapter(mSectionedAdapter);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            mSectionedAdapter.mStarted = true;
            mSectionedAdapter.notifyDataSetChanged();
        }
    }

}
