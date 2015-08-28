package example.tiny.data;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiny on 15-8-26.
 */
public class CommentGroup implements Comparable<CommentGroup> {
    Comment comment;
    boolean likes;
    User user;
    User atUser;
    boolean isRecent;


    public boolean isRecent() {
        return isRecent;
    }

    public void setRecent(boolean isRecent) {
        this.isRecent = isRecent;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public boolean isLikes() {
        return likes;
    }

    public void setLikes(boolean likes) {
        this.likes = likes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getAtUser() {
        return atUser;
    }

    public void setAtUser(User atUser) {
        this.atUser = atUser;
    }


    @Override
    public String toString() {
        return "comment\t" + comment +
                "likes\t" + likes +
                "user\t" + user +
                "atUser\t" + atUser;

    }

    public static CommentGroup fromJson(JSONObject json) {
        CommentGroup c = new CommentGroup();
        //获取Comment；
        String commentString = json.getString("comment");
        if (!"".equals(commentString))
            c.comment = Comment.FromJSon(JSON.parseObject(commentString));

        //获取Like；
        c.likes = json.getBoolean("likes");

        //获取User的信息
        String userString = json.getString("user");
        if (userString != null && !userString.equals(""))
            c.user = User.FromJSon(JSON.parseObject(userString));

        //获取atUser的信息
        String atUserString = json.getString("atUser");
        if (!"".equals(atUserString)) {
            c.atUser = User.FromJSon(JSON.parseObject(atUserString));
        }
        return c;
    }


    @Override
    public int compareTo(CommentGroup another) {
        int recent = isRecent ? 1 : 0;
        int code = (recent << 1) + (another.isRecent ? 1 : 0);
        switch (code) {
            case 0:
                //两个都是0的情况，即：两条数据均为Hot，使用like条数来排名。
                if (this.comment.likes > another.comment.likes)
                    return -1;
                else if (this.comment.likes < another.comment.likes)
                    return 1;
                else
                    return 0;
            case 1:
                //this所指向的数据是hot, another所指向的数据是recent
                return -1;
            case 2:
                //this所指向的数据是recent,another所指向的数据是hot
                return 1;

            case 3:
                //两条数据均为recent,根据时间先后来排名
                if (this.comment.getCreatedAt().after(another.comment.getCreatedAt()))
                    return -1;
                else if (this.comment.getCreatedAt().before(another.comment.getCreatedAt()))
                    return 1;
                else
                    return 0;
        }
        Log.e("CommentGroup", "出现异常情况：compareTo()函数异常，出现code异常：code" + code);
        return 0;
    }

}
