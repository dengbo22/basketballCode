package example.tiny.data;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * Created by tiny on 15-8-26.
 */
public class Comment {
    String competitionId;
    String content;
    Date createdAt;
    int likes;
    String objectId;
    Date updateAt;

    public static Comment FromJSon(JSONObject json) {
        Comment comment = new Comment();
        comment.content = json.getString("content");

        return comment;
    }

    @Override
    public String toString() {
        return "Comment.content:" + content;
    }
}
