package example.tiny.backetball;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by tiny on 15-8-20.
 */
public class LiveItemData {

    String mCampusData;
    String  mCompetitionTypeData;
    String mCompetitionTypeIconData;
    String mTeamAIconData;
    String mTeamANameData;
    String mTeamBIconData;
    String mTeamBNameData;
    String mGameNameData;
    int mTeamAScoreData;
    int mTeamBScoreData;

    public Date getBeginTime() {
        return mBeginTime;
    }

    public void setBeginTime(Date mBeginTime) {
        this.mBeginTime = mBeginTime;
    }

    Date mBeginTime;

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

    String mCompetitionStatusData;


}