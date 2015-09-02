package example.tiny.adapter;

import android.content.Context;
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
import com.avos.avoscloud.GetCallback;

import java.util.ArrayList;
import java.util.List;

import example.tiny.backetball.LiveDetailActivity;
import example.tiny.backetball.R;

/**
 * Created by tiny on 15-9-1.
 */
public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {
    private static final String LOG_TAG = "SimpleAdapter";

    public static int mPlayerSpan = 4;
    public static int mScoreSpan = 6;
    public static int SCORE_HEIGHT = 3;
    public static int PLAYER_A_HEIGHT = 4;
    public static int PLAYER_B_HEIGHT = 4;


    private final Context mContext;
    private final List<String> mItems;

    public SimpleAdapter(Context context) {
        super();
        mContext = context;
        mItems = new ArrayList<>();
        RequestStatisticsData();
    }



    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.layout_statistics_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        if (mItems.size() != 0) {
            holder.title.setText(mItems.get(position));
            Log.e("SimpleAdapter", "position:" + position);
        }
    }

    private void RequestStatisticsData() {
        AVObject data = null;
        AVQuery<AVObject> query = new AVQuery<>("Competition");
        query.getInBackground(((LiveDetailActivity) mContext).objectId, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    if (avObject != null) {
                        String statisticsString = avObject.getString("statistics");
                        JSONObject obj = JSON.parseObject(statisticsString);
                        JSONArray statistics = obj.getJSONArray("statistics");
                        JSONArray playerA = obj.getJSONArray("playerA");
                        JSONArray playerB = obj.getJSONArray("playerB");
                        //初始化各项参数
                        SCORE_HEIGHT = statistics.size();
                        PLAYER_A_HEIGHT = playerA.size();
                        PLAYER_B_HEIGHT = playerB.size();
                        JSONArray a = statistics.getJSONArray(0);
                        mPlayerSpan = a.size();
                        JSONArray b = playerA.getJSONArray(0);
                        mScoreSpan = b.size();
                        //解析得分统计
                        for(int i = 0; i < SCORE_HEIGHT; i++) {
                            JSONArray array = statistics.getJSONArray(i);
                            for(int j = 0; j < array.size(); j++) {
                                mItems.add(array.get(j).toString());
                            }
                        }
                        notifyDataSetChanged();
                        //解析球员数据
                        for(int i = 0; i < PLAYER_A_HEIGHT; i++) {
                            JSONArray array = playerA.getJSONArray(i);
                            for(int j = 0; j < array.size(); j++) {
                                mItems.add(array.get(j).toString());
                            }
                        }
                        //解析球员数据
                        for(int i = 0; i < PLAYER_B_HEIGHT; i++) {
                            JSONArray array = playerB.getJSONArray(i);
                            for(int j = 0; j < array.size(); j++) {
                                mItems.add(array.get(j).toString());
                            }
                        }
                        notifyDataSetChanged();
                    }
                } else {
                    Log.e("SimpleAdapter", "Exception in Request");
                }
            }
        });
    }


    //RecycleView.ViewHolder
    class SimpleViewHolder extends RecyclerView.ViewHolder{
        public final TextView title;

        public SimpleViewHolder(View itemView ) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.tv_statistics_item);
        }


    }
}
