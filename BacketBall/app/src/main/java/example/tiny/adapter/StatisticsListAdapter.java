package example.tiny.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;

import java.util.ArrayList;

import example.tiny.backetball.LiveDetailActivity;
import example.tiny.backetball.R;

/**
 * Created by tiny on 15-9-1.
 */
public class StatisticsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String LOG_TAG = "StatisticsListAdapter";
    private final Context mContext;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_TITLE = 1;
    private static final int TYPE_ITEM = 2;


    public static int mPlayerPerLine = 6;
    public static int mScorePerLine = 4;

    public static int SCORE_HEIGHT = 3;
    public static int PLAYER_A_HEIGHT = 4;
    public static int PLAYER_B_HEIGHT = 4;

    private ArrayList<String> mItems;

    public boolean mStarted = false;
    private RecyclerView mRecyclerView;
    private ArrayList<Header> mHeaders;
    private GridLayoutManager mLayoutManager;
    private LayoutInflater mLayoutInflater;


    public StatisticsListAdapter(Context context, RecyclerView recyclerView) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mRecyclerView = recyclerView;
        mItems = new ArrayList<>();
        mHeaders = new ArrayList<>();
        //在StatisticsLstAdatper创建的时候则请求数据,知道数据请求完成才开始继续创建
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestStatisticsData();

            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mLayoutManager = new GridLayoutManager(mContext, mPlayerPerLine * mScorePerLine);
        mHeaders.add(new Header(0, "得分统计"));
        mHeaders.add(new Header(mScorePerLine * SCORE_HEIGHT + 1, "11级A班球员数据"));
        mHeaders.add(new Header(mScorePerLine * SCORE_HEIGHT + mPlayerPerLine * PLAYER_A_HEIGHT + 2, "11级B班球员数据"));

        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (getPositionType(position)) {
                    case TYPE_HEADER:
                        return mLayoutManager.getSpanCount();
                    case TYPE_TITLE:
                    case TYPE_ITEM:
                        if (position <= SCORE_HEIGHT * mScorePerLine)
                            return mPlayerPerLine;
                        else
                            return mScorePerLine;
                    default:
                        Log.e(LOG_TAG, "setSpanSize()异常:position不属于任何一类-->" + position);
                        return 1;
                }
            }
        });
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    public int getDataSize() {
        return mItems.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int typeView) {
        switch (typeView) {
            case TYPE_HEADER:
                View header_view = mLayoutInflater.inflate(R.layout.layout_statistics_header, parent, false);
                return new HeaderViewHolder(header_view);
            case TYPE_TITLE:
                View title_view = mLayoutInflater.inflate(R.layout.layout_statistics_title, parent, false);
                return new TitleViewHolder(title_view);
            case TYPE_ITEM:
                View item_view = mLayoutInflater.inflate(R.layout.layout_statistics_item, parent, false);
                return new ItemViewHolder(item_view);
        }
        Log.e(LOG_TAG, "Error Type in onCreateViewHolder-->type" + typeView);
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        switch (getPositionType(position)) {
            case TYPE_HEADER:
                for (int i = 0; i < mHeaders.size(); i++) {
                    if (mHeaders.get(i).position == position) {
                        ((HeaderViewHolder) viewHolder).header.setText(mHeaders.get(i).title);
                        return;
                    }
                }
                Log.e(LOG_TAG, "在mHeader中找不到position相符的内容");
                break;
            case TYPE_TITLE:
                ((TitleViewHolder) viewHolder).title.setText(mItems.get(parsePositionToItem(position)));
                break;
            case TYPE_ITEM:
                ((ItemViewHolder) viewHolder).item.setText(mItems.get(parsePositionToItem(position)));
                break;
            default:
                Log.e(LOG_TAG, "onBindViewHolder异常,获取到非正常类型的position" + position);
                break;
        }
        return;

    }

    @Override
    public int getItemViewType(int position) {
        return getPositionType(position);
    }


    private class Header {
        int position;
        CharSequence title;

        Header(int firstPosition, CharSequence title) {
            this.position = firstPosition;
            this.title = title;
        }
    }

    //将position转化成item的Position
    private int parsePositionToItem(int position) {
        for (int i = 0; i < mHeaders.size(); i++) {
            if (position == mHeaders.get(i).position) {
                Log.e(LOG_TAG, "对HeaderPostion调用parsePosition-->" + position);
                return RecyclerView.NO_POSITION;    //返回-1;
            }
        }
        //如果position参数并非header的位置,则进行正常转化:
        //位置在0(1->1,2->2),13(14->13, 15->14),39(40->38, 41->39)
        int offset = 0;
        for (int i = 0; i < mHeaders.size(); i++) {
            if (position > mHeaders.get(i).position)
                offset++;
        }
        return position - offset;
    }

    //获取指定position位置的Type
    private int getPositionType(int position) {
        for (int i = 0; i < mHeaders.size(); i++) {
            if (position == mHeaders.get(i).position)
                return TYPE_HEADER;
        }
        //position是TYPE_TITLE的情况:第一行是得分行:
        if (position <= mScorePerLine
                || (position <= mHeaders.get(1).position + mScorePerLine && position > mHeaders.get(1).position)
                || (position <= mHeaders.get(2).position + mScorePerLine && position > mHeaders.get(2).position))
            return TYPE_TITLE;

        else {
            return TYPE_ITEM;
        }

    }


    private void RequestStatisticsData() {
        AVQuery<AVObject> query = new AVQuery<>("Competition");
        AVObject avObject = null;
        try {
            avObject = query.get(((LiveDetailActivity) mContext).objectId);
        } catch (AVException e) {
            e.printStackTrace();
        }
        Log.e(LOG_TAG, avObject + "");
        if (avObject != null) {
            String statisticsString = avObject.getString("statistics");
            if (statisticsString != null && !"".equals(statisticsString)) {
                JSONObject obj = JSON.parseObject(statisticsString);
                JSONArray statistics = obj.getJSONArray("statistics");
                JSONArray playerA = obj.getJSONArray("playerA");
                JSONArray playerB = obj.getJSONArray("playerB");
                //初始化各项参数
                SCORE_HEIGHT = statistics.size();
                PLAYER_A_HEIGHT = playerA.size();
                PLAYER_B_HEIGHT = playerB.size();
                JSONArray a = statistics.getJSONArray(0);
                mScorePerLine = a.size();
                JSONArray b = playerA.getJSONArray(0);
                mPlayerPerLine = b.size();
                //解析得分统计
                for (int i = 0; i < SCORE_HEIGHT; i++) {
                    JSONArray array = statistics.getJSONArray(i);
                    for (int j = 0; j < array.size(); j++) {
                        mItems.add(array.get(j).toString());
                    }
                }
                //解析球员数据
                for (int i = 0; i < PLAYER_A_HEIGHT; i++) {
                    JSONArray array = playerA.getJSONArray(i);
                    for (int j = 0; j < array.size(); j++) {
                        mItems.add(array.get(j).toString());
                    }
                }
                //解析球员数据
                for (int i = 0; i < PLAYER_B_HEIGHT; i++) {
                    JSONArray array = playerB.getJSONArray(i);
                    for (int j = 0; j < array.size(); j++) {
                        mItems.add(array.get(j).toString());
                    }
                }
                notifyDataSetChanged();
            }else {
                //获取到数据但是不包含statistics内容
            }
        }

    }


    @Override
    public int getItemCount() {
        if(mStarted && mItems.size() != 0)
            return mItems.size() + mHeaders.size();
        else
            return 0;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        public TextView header;

        public HeaderViewHolder(View view) {
            super(view);
            header = (TextView) view.findViewById(R.id.tv_statistics_header);
        }
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {

        public TextView title;

        public TitleViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_statistics_title);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView item;

        public ItemViewHolder(View itemView) {
            super(itemView);
            item = (TextView) itemView.findViewById(R.id.tv_statistics_item);
        }
    }

}
