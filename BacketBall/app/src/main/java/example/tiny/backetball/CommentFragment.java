package example.tiny.backetball;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FunctionCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import example.tiny.adapter.CommentListAdapter;
import example.tiny.data.Comment;
import example.tiny.data.CommentGroup;
import example.tiny.data.User;
import example.tiny.utils.DateFormat;
import example.tiny.utils.LeanCloudParser;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by tiny on 15-8-24.
 */
public class CommentFragment extends Fragment {
    private static final String LOG_TAG = "CommentFragment";

    private CommentListAdapter mCommentAdapter;
    private ArrayList<CommentGroup> mCommentData;
    private ImageView backImage;
    private TextView mTvCommentCount;
    private EditText mEdtTxtCommentInfo;
    private int mClickItem = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* getArguments() */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        backImage = (ImageView) view.findViewById(R.id.background_image);
        StickyListHeadersListView mStickyListView = (StickyListHeadersListView) view.findViewById(R.id.id_stickynavlayout_innerscrollview);
        mTvCommentCount = (TextView) view.findViewById(R.id.tv_comment_count);

        if (mCommentAdapter == null) {
            mStickyListView.setAdapter(new CommentListAdapter(getActivity()));
            mCommentAdapter = (CommentListAdapter) mStickyListView.getAdapter();
            if (mCommentAdapter.getDataList() != null) {
                mCommentData = mCommentAdapter.getDataList();
            }
        }
        mStickyListView.setOnScrollListener(new LoadMoreScrollListener());
        mStickyListView.setOnItemClickListener(new CommentItemClickListener());
        mEdtTxtCommentInfo = ((LiveDetailActivity) getActivity()).getEdtTxtComment();
        TextView mTvCommentSend = ((LiveDetailActivity) getActivity()).getTvCommentSend();
        mTvCommentSend.setOnClickListener(new SendCommentOnlineClickListener());

        RequestCommentData();
        return view;
    }


    private void LoadMoreRequest() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("competitionId", ((LiveDetailActivity) getActivity()).objectId);
        parameters.put("count", 10);
        int lastDataIndex = mCommentAdapter.getDataList().size() - 1;
        CommentGroup lastData = (CommentGroup) mCommentAdapter.getDataList().get(lastDataIndex);
        Date date = lastData.getComment().getCreatedAt();
        parameters.put("createdAt", DateFormat.toLeanCloudDateString(date));
        AVCloud.callFunctionInBackground("getOldComment", parameters, new FunctionCallback<Object>() {
            @Override
            public void done(Object o, AVException e) {
                new AddDataTask().execute(o);
            }
        });
    }

    private void RequestCommentData() {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("competitionId", ((LiveDetailActivity) getActivity()).objectId);
        parameters.put("count", 5);
        AVCloud.callFunctionInBackground("commentInit", parameters, new FunctionCallback<Object>() {
            @Override
            public void done(Object o, AVException e) {
                if (e == null) {
                    new AddDataTask().execute(o);
                } else
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void AddCommentDataIntoAdapter(Object fastJson) {
        //得到的是一个JSONObject，调用CommentInit的情况
        if (JSON.toJSON(fastJson) instanceof JSONObject) {
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
            if (hotArray != null) {
                for (int i = 0; i < hotArray.size(); i++) {
                    JSONObject hotObj = hotArray.getJSONObject(i);
                    CommentGroup hotComment = CommentGroup.fromJson(hotObj);
                    hotComment.setRecent(false);
                    mCommentAdapter.add(hotComment);
                }
            }
            Collections.sort(mCommentAdapter.getDataList());
            if (mCommentAdapter.getDataList().size() != 0) {
                backImage.post(new Runnable() {
                    @Override
                    public void run() {
                        backImage.setVisibility(View.GONE);
                    }
                });

                mTvCommentCount.post(new Runnable() {
                    @Override
                    public void run() {
                        mTvCommentCount.setVisibility(View.VISIBLE);
                    }
                });

            }
        } else if (JSON.toJSON(fastJson) instanceof JSONArray) {
            JSONArray oldCommentArray = (JSONArray) JSON.toJSON(fastJson);
            if (oldCommentArray != null && oldCommentArray.size() != 0) {
                for (int i = 0; i < oldCommentArray.size(); i++) {
                    JSONObject commentObj = oldCommentArray.getJSONObject(i);
                    CommentGroup comment = CommentGroup.fromJson(commentObj);
                    //设置数据类型为：Recent数据
                    comment.setRecent(true);
                    mCommentAdapter.add(comment);
                }
            }
        } else {
            Log.e(LOG_TAG, "Unexpected Exception:" + fastJson);
        }


    }

    class LoadMoreScrollListener implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case SCROLL_STATE_IDLE:
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                LoadMoreRequest();
                            }
                        }).start();
                    }
            }
        }

        //firstVisibleItem表示第一个列表项的索引值，visibleItemCount表示当前课件的列表项的个数，totalItemCount表示列表中的总数
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            //当totalItemCount = firstVisibleItem + visibleItemCount时ListView滑动到底部
            Log.v("CommentFragment", "first->" + firstVisibleItem + "\tvisible->" + visibleItemCount + "\ttotal->" + totalItemCount);

        }
    }

    class CommentItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (id == -1) {
                Toast.makeText(getActivity(), "你点击了Header/Footer!", Toast.LENGTH_SHORT).show();
                return;
            }
            int realPosition = (int) id;
            mClickItem = realPosition;
            CommentGroup obj = mCommentData.get(realPosition);
            mEdtTxtCommentInfo.requestFocus();
            mEdtTxtCommentInfo.setHint("回复" + obj.getUser().getNickname() + " : ");
        }
    }

    class SendCommentClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            CommentGroup newData = new CommentGroup();
            //设置当前用户

            newData.setUser(((BasketBallApp) getActivity().getApplication()).getUser());
            //设置Comment
            Comment comment = new Comment();
            Date cur = new Date();
            comment.setCreatedAt(cur);
            comment.setUpdateAt(cur);
            LiveDetailActivity activity = (LiveDetailActivity) getActivity();
            comment.setCompetitionId(activity.objectId);
            comment.setContent(mEdtTxtCommentInfo.getText().toString());
            comment.setLikes(0);
            newData.setComment(comment);
            //设置recent
            newData.setRecent(true);


            //是否有atUser字段, 如果有则添加
            if (mClickItem != -1) {
                User atUser = mCommentData.get(mClickItem).getUser();
                newData.setAtUser(atUser);
            }

            //发送信息
            mCommentAdapter.add(newData);
            Collections.sort(mCommentAdapter.getDataList());
            mCommentAdapter.notifyDataSetChanged();
            //收起键盘,删除editText的内容
            mEdtTxtCommentInfo.getText().clear();
            mEdtTxtCommentInfo.setHint("");
            mEdtTxtCommentInfo.clearFocus();
            Log.e(LOG_TAG, getActivity().getCurrentFocus() + "focus");
        }
    }

    class SendCommentOnlineClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            final AVObject comment = new AVObject("Comment");
            comment.put("userId", AVUser.getCurrentUser() );
            comment.put("content", mEdtTxtCommentInfo.getText().toString().trim() );
            comment.put("likes", 0);
            comment.put("competitionId", AVObject.createWithoutData("Competition", ((LiveDetailActivity)getActivity()).objectId ) );
            if(mClickItem != -1) {
                User atUser = mCommentData.get(mClickItem).getUser();
                comment.put("atUser", AVObject.createWithoutData("_User", atUser.getObjectId()));
            }
            //Save
            comment.setFetchWhenSave(true);
            comment.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    //加入到列表中，此时已经有ObjectId了
                    CommentGroup newData = new CommentGroup();
                    //设置当前用户
                    newData.setUser(LeanCloudParser.AVUserToUser(AVUser.getCurrentUser() ) );
                    //设置Comment
                    Comment local = new Comment();
                    local.setCreatedAt(comment.getCreatedAt());
                    local.setUpdateAt(comment.getUpdatedAt());

                    LiveDetailActivity activity = (LiveDetailActivity) getActivity();
                    local.setCompetitionId(activity.objectId);
                    local.setContent(comment.getString("content"));
                    local.setLikes(comment.getInt("likes"));
                    newData.setComment(local);
                    //设置recent
                    newData.setRecent(true);
                    //是否有atUser字段, 如果有则添加
                    if (mClickItem != -1) {
                        User atUser = mCommentData.get(mClickItem).getUser();
                        newData.setAtUser(atUser);
                    }
                    //发送信息
                    mCommentAdapter.add(newData);
                    Collections.sort(mCommentAdapter.getDataList());
                    mCommentAdapter.notifyDataSetChanged();
                    //收起键盘,删除editText的内容
                    mEdtTxtCommentInfo.getText().clear();
                    mEdtTxtCommentInfo.setHint("");
                    mEdtTxtCommentInfo.clearFocus();




                }
            });

        }
    }

    class AddDataTask extends AsyncTask<Object, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Object... params) {
            AddCommentDataIntoAdapter(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mCommentAdapter.notifyDataSetChanged();
        }
    }
}
