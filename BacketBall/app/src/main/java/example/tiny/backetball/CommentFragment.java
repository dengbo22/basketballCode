package example.tiny.backetball;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.FunctionCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import example.tiny.adapter.CommentListAdapter;
import example.tiny.data.CommentGroup;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by tiny on 15-8-24.
 */
public class CommentFragment extends Fragment {
    private static final String LOG_TAG = "CommentFragment";

    private StickyListHeadersListView mStickyListView;
    private CommentListAdapter mCommentAdapter;
    private ImageView backImage;
    private TextView mTvCommentCount;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /* getArguments() */
        if(mCommentAdapter == null)
            mCommentAdapter = new CommentListAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        backImage = (ImageView) view.findViewById(R.id.background_image);
        mStickyListView = (StickyListHeadersListView) view.findViewById(R.id.id_stickynavlayout_innerscrollview);
        mTvCommentCount = (TextView) view.findViewById(R.id.tv_comment_count);

        mStickyListView.setAdapter( mCommentAdapter );
        mStickyListView.setOnScrollListener(new LoadMoreScrollListener());
        RequestCommentData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume()");
    }

    class LoadMoreScrollListener implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case SCROLL_STATE_IDLE:
                    if(view.getLastVisiblePosition() == (view.getCount() -1)) {

                    }
            }
        }

        //firstVisibleItem表示第一个列表项的索引值，visibleItemCount表示当前课件的列表项的个数，totalItemCount表示列表中的总数
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            //当totalItemCount = firstVisibleItem + visibleItemCount时ListView滑动到底部
            Log.v("CommentFragment", "first->" + firstVisibleItem + "\tvisible->" + visibleItemCount +"\ttotal->" + totalItemCount );

        }
    }

    private void RequestCommentData() {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("competitionId", ((LiveDetailActivity)getActivity()).objectId );
        parameters.put("count", 5);
        AVCloud.callFunctionInBackground("commentInit", parameters, new FunctionCallback<Object>() {
            @Override
            public void done(Object o, AVException e) {
                if(e == null)
                    AddCommentDataIntoAdapter(o);
                else
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void AddCommentDataIntoAdapter(Object fastJson)  {
        JSONObject obj = (JSONObject) JSON.toJSON(fastJson);
        //获取最新评论, 并将其解析成CommentGroup
        JSONArray recentArray = obj.getJSONArray("recent");
        //获取每条具体评论内容
        for (int i = 0; i < recentArray.size(); i++) {
            JSONObject commentObj = recentArray.getJSONObject(i);
            CommentGroup comment = CommentGroup.fromJson(commentObj);
            //设置数据类型为：Recent数据
            comment.setRecent(true);
            mCommentAdapter.add(comment);
        }

        //获取最热评论，并将其解析成CommentGroup
        JSONArray hotArray = obj.getJSONArray("hot");
        for (int i = 0; i < hotArray.size(); i++) {
            JSONObject hotObj = hotArray.getJSONObject(i);
            CommentGroup hotComment = CommentGroup.fromJson(hotObj);
            hotComment.setRecent(false);
            mCommentAdapter.add(hotComment);
        }
        Collections.sort(mCommentAdapter.getDataList());
        if(mCommentAdapter.getDataList().size() != 0) {
            backImage.setVisibility(View.GONE);
            mTvCommentCount.setVisibility(View.VISIBLE);
        }


        mCommentAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "评论加载完毕", Toast.LENGTH_SHORT).show();
    }


}
