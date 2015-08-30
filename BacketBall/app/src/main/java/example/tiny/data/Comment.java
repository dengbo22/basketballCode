package example.tiny.data;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import example.tiny.utils.DateFormat;

/**
 * Created by tiny on 15-8-26.
 */
public class Comment {
    //成员变量
    String competitionId;
    String content;
    Date createdAt;
    int likes;
    String objectId;
    Date updateAt;

    public String getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(String competitionId) {
        this.competitionId = competitionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getObjectId() {
        if(objectId == null)
            return "";
        else
            return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }



    public static Comment FromJSon(JSONObject json) {
        Comment comment = new Comment();
        comment.competitionId = json.getString("competitionId");
        comment.content = json.getString("content");
        comment.likes = json.getInteger("likes");
        comment.objectId = json.getString("objectId");
        String dateString = json.getString("createdAt");
        comment.createdAt = DateFormat.UtcToDate(dateString);
        String dateString2 = json.getString("updatedAt");
        if(dateString == null || dateString.equals(""))
            comment.updateAt = comment.createdAt;
        else
            comment.updateAt = DateFormat.UtcToDate(dateString2);
        return comment;
    }

    @Override
    public String toString() {
        return "CompetitionId:" + competitionId +"\tComment.content:" + content + "\tLikes:" + likes + "\tCreateDate:" + createdAt ;
    }
}
