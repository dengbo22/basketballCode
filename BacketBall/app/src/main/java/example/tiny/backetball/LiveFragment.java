package example.tiny.backetball;

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

import example.tiny.adapter.LiveListAdapter;
import example.tiny.data.BasketSQLite;
import example.tiny.data.LiveItemData;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by tiny on 15-8-19.
 */
public class LiveFragment extends Fragment {
    private static final String LOG_TAG = "LiveFragment";

    private static final int LOAD_SIZE = 3;

    StickyListHeadersListView liveList = null;
    LiveListAdapter liveListAdapter = null;
    ArrayList<LiveItemData> dataList = null;
    PtrFrameLayout mFrameLiveRefresh = null;
    private int mSkip;
    private Date mFirstDate;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_fix, container, false);

        mFrameLiveRefresh = (PtrFrameLayout) view.findViewById(R.id.flayout_live_refresh);
        liveList = (StickyListHeadersListView) view.findViewById(R.id.list_live_competition);
        liveList.setAdapter(new LiveListAdapter(getActivity()) );
        liveListAdapter = (LiveListAdapter) liveList.getAdapter();
        liveList.setOnItemClickListener(new ItemClickListener());

        mFrameLiveRefresh.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(ptrFrameLayout, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                RequestLiveData();
            }
        });

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //加载数据部分，首先先加载数据库中的内容
        BasketSQLite sql = ((MainActivity)getActivity()).getBasketSQLite();
        ArrayList<LiveItemData> mData = sql.onGetAllInLive();
        if(mData != null && mData.size() != 0) {
            //数据库中本身就有数据，将其加载
            for(LiveItemData item : mData) {
                liveListAdapter.getCompetitionData().add(item);
            }
            Collections.sort(liveListAdapter.getCompetitionData());

        } else {
            //数据为null，自动请求服务器并且加载数据
            mFrameLiveRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mFrameLiveRefresh.autoRefresh(true);
                }
            }, 150);
        }
        dataList = liveListAdapter.getCompetitionData();


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
    protected boolean insertDataIntoAdapter(List<AVObject> list) {
        //当前没有返回数据，返回插入失败
        if (list == null || list.size() == 0)
            return false;
        else {
            for(int i = 0; i < list.size(); i++) {
                LiveItemData item = LiveItemData.FromAVObject(list.get(i));
                for(int j = 0; j < dataList.size(); j++) {
                    LiveItemData listItem = dataList.get(j);
                    if(listItem.getObjectId().equals(item.getObjectId())) {
                        //具有相同Id的数据，此处采用的操作是：删除之前数据，将新数据插入
                        dataList.remove(listItem);
                    }
                }
                dataList.add(0, item);
            }
            Collections.sort(dataList);

            liveListAdapter.notifyDataSetChanged();
            return true;
        }
    }


    private void RequestLiveData() {
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
        if(requestCode == MainActivity.LOGIN_CODE)
            return;

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
                Toast.makeText(getActivity(), "Error"+ e, Toast.LENGTH_SHORT).show();
            }

            mFrameLiveRefresh.refreshComplete();
        }
    }


}
