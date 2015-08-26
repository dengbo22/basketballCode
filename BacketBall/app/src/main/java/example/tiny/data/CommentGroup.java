package example.tiny.data;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiny on 15-8-26.
 */
public class CommentGroup {
    Comment comment;
    boolean likes;
    User user;
    User atUser;

    @Override
    public String toString() {
        return "comment\t" + comment +
                "likes\t" + likes +
                "user\t" + user +
                "atUser\t" + atUser;

    }

    public static CommentGroup fromJson(JSONObject json) {
        CommentGroup c = new CommentGroup();
        Log.e("Comment", "json:" + json);
        //获取Comment；
        String commentString = json.getString("comment");
        if(! "".equals(commentString) )
            c.comment = Comment.FromJSon(JSON.parseObject(commentString) );
        //获取Like；
        c.likes = json.getBoolean("likes");
        //获取User的信息
        String userString = json.getString("user");
        if (userString != null && !userString.equals(""))
            c.user = User.FromJSon(JSON.parseObject(userString));
        //获取atUser的信息
        String atUserString = json.getString("atUser");
        if (! "".equals(atUserString) ) {
            c.atUser = User.FromJSon(JSON.parseObject(atUserString));
        }
        return c;
    }

}
