package example.tiny.data;


import android.util.Log;

import com.avos.avoscloud.AVObject;

import java.util.Date;

/**
 * Created by tiny on 15-8-20.
 */
public class LiveItemData implements Comparable<LiveItemData> {

    String mObjectId;
    String mCampusData;
    String mCompetitionTypeData;
    String mCompetitionTypeIconData;
    String mTeamAIconData;
    String mTeamANameData;
    String mTeamBIconData;
    String mTeamBNameData;
    String mGameNameData;
    int mTeamAScoreData;
    int mTeamBScoreData;
    Date mBeginTime;
    String mCompetitionStatusData;

    public static LiveItemData FromAVObject(AVObject avObject) {
        LiveItemData data = new LiveItemData();
        data.mObjectId = avObject.getObjectId();
        data.mBeginTime = avObject.getDate("beginTime");
        AVObject teamA = avObject.getAVObject("teamAId");
        AVObject teamB = avObject.getAVObject("teamBId");
        data.mTeamANameData = teamA.getString("name");
        data.mTeamAIconData = teamA.getString("logoUrl");
        data.mTeamBNameData = teamB.getString("name");
        data.mTeamBIconData = teamB.getString("logoUrl");
        AVObject game = avObject.getAVObject("gameId");
        String gameName = "";
        String campus = "";
        //执行异常检测
        if (game != null) {
            gameName = game.getString("name");
            AVObject c = game.getAVObject("campusId");
            if (c != null) {
                campus = c.getString("name");
            }
        }
        data.mGameNameData = gameName;
        data.mCampusData = campus;
        AVObject score = avObject.getAVObject("scoreId");
        int teamAScore = 0;
        int teamBScore = 0;
        if (score != null) {
            teamAScore = score.getInt("scoreA");
            teamBScore = score.getInt("scoreB");
        }
        data.mTeamAScoreData = teamAScore;
        data.mTeamBScoreData = teamBScore;
        data.mCompetitionTypeData = avObject.getString("type");
        data.mCompetitionStatusData = avObject.getString("status");

        return data;
    }


    public String getObjectId() {
        return mObjectId;
    }

    public void setObjectId(String objectId) {
        this.mObjectId = objectId;
    }

    public Date getBeginTime() {
        return mBeginTime;
    }

    public void setBeginTime(Date mBeginTime) {
        this.mBeginTime = mBeginTime;
    }

    public String getCompetitionStatusData() {
        return mCompetitionStatusData;
    }

    public void setCompetitionStatusData(String mCompetitionStatusData) {
        this.mCompetitionStatusData = mCompetitionStatusData;
    }

    public int getTeamBScoreData() {
        return mTeamBScoreData;
    }

    public void setTeamBScoreData(int mTeamBScoreData) {
        this.mTeamBScoreData = mTeamBScoreData;
    }

    public int getTeamAScoreData() {
        return mTeamAScoreData;
    }

    public void setTeamAScoreData(int mTeamAScoreData) {
        this.mTeamAScoreData = mTeamAScoreData;
    }

    public String getGameNameData() {
        return mGameNameData;
    }

    public void setGameNameData(String mGameNameData) {
        this.mGameNameData = mGameNameData;
    }

    public String getTeamBNameData() {
        return mTeamBNameData;
    }

    public void setTeamBNameData(String mTeamBNameData) {
        this.mTeamBNameData = mTeamBNameData;
    }

    public String getTeamBIconData() {
        return mTeamBIconData;
    }

    public void setTeamBIconData(String mTeamBIconData) {
        this.mTeamBIconData = mTeamBIconData;
    }

    public String getTeamANameData() {
        return mTeamANameData;
    }

    public void setTeamANameData(String mTeamANameData) {
        this.mTeamANameData = mTeamANameData;
    }

    public String getTeamAIconData() {
        return mTeamAIconData;
    }

    public void setTeamAIconData(String mTeamAIconData) {
        this.mTeamAIconData = mTeamAIconData;
    }

    public String getCompetitionTypeIconData() {
        return mCompetitionTypeIconData;
    }

    public void setCompetitionTypeIconData(String mCompetitionTypeIconData) {
        this.mCompetitionTypeIconData = mCompetitionTypeIconData;
    }

    public String getCompetitionTypeData() {
        return mCompetitionTypeData;
    }

    public void setCompetitionTypeData(String mCompetitionTypeData) {
        this.mCompetitionTypeData = mCompetitionTypeData;
    }

    public String getCampusData() {
        return mCampusData;
    }

    public void setCampusData(String mCampusData) {
        this.mCampusData = mCampusData;
    }

    @Override
    public String toString() {
        String str = mObjectId
                + "\t" + mCampusData
                + "\t" + mCompetitionTypeData
                + "\t" + mCompetitionTypeIconData
                + "\t" + mTeamAIconData
                + "\t" + mTeamANameData
                + "\t" + mTeamBIconData
                + "\t" + mTeamBNameData
                + "\t" + mGameNameData
                + "\t" + mTeamAScoreData
                + "\t" + mTeamBScoreData
                + "\t" + mBeginTime;
        return str;
    }


    @Override
    public int compareTo(LiveItemData another) {
        if (this.mBeginTime.after(another.mBeginTime))
            return 1;
        else if (this.mBeginTime.before(another.mBeginTime))
            return -1;
        else
            return 0;
    }
}
