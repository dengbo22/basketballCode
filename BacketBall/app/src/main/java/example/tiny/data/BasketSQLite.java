package example.tiny.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tiny on 15-8-23.
 * 在数据库中, boolean值mIsFinished用integer保存, Date值将其转化成long数据使用integer保存
 */
public class BasketSQLite extends SQLiteOpenHelper {
    private Context mContext;

    private static final String CREATE_COMPETITION_DATA = "create table Competition("
            + "ID integer primary key autoincrement, "
            + "objectId text, "
            + "mName text, "
            + "mCollege text, "
            + "mCampus text, "
            + "mFollowNumber integer,"
            + "mCoverUrl text,"
            + "mType text,"
            + "mIsFinished integer)";

    private static final String CREATE_LIVE_DATA = "create table Live("
            + "objectId text primary key, "
            + "mCampusData text, "
            + "mCompetitionTypeData text, "
            + "mCompetitionTypeIconData text, "
            + "mTeamAIconData text, "
            + "mTeamANameData text, "
            + "mTeamBIconData text, "
            + "mTeamBNameData text, "
            + "mGameNameData text, "
            + "mTeamAScoreData integer, "
            + "mTeamBScoreData integer, "
            + "mBeginTime integer, "
            + "mCompetitionStatusData )";

    public BasketSQLite(Context context, String name, android.database.sqlite.SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }


    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        db.execSQL(CREATE_COMPETITION_DATA);
        db.execSQL(CREATE_LIVE_DATA);
        Toast.makeText(mContext, "数据库创建", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void onInsertCompetitionData(CompetitionItemData data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //开始插入数据
        values.put("objectId", data.getID());
        values.put("mName", data.getName());
        values.put("mCollege", data.getCollege());
        values.put("mCampus", data.getCampus());
        values.put("mFollowNumber", data.getFollowNumber());
        values.put("mCoverUrl", data.getCoverUrl());
        values.put("mType", data.getType());
        values.put("mIsFinished", data.getIsFinished());
        db.insert("Competition", null, values);
        values.clear();
    }

    public void onInsertLiveData(LiveItemData data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //开始插入数据
        values.put("objectId", data.getObjectId());
        values.put("mCampusData", data.getCampusData());
        values.put("mCompetitionTypeData", data.getCompetitionTypeData());
        values.put("mCompetitionTypeIconData", data.getCompetitionTypeIconData());
        values.put("mTeamAIconData", data.getTeamAIconData());
        values.put("mTeamANameData", data.getTeamANameData());
        values.put("mTeamBIconData", data.getTeamBIconData());
        values.put("mTeamBNameData", data.getTeamBNameData());
        values.put("mGameNameData", data.getGameNameData());
        values.put("mTeamAScoreData", data.getTeamAScoreData());
        values.put("mTeamBScoreData", data.getTeamBScoreData());
        values.put("mBeginTime", data.getBeginTime().getTime() / 1000);

        values.put("mCompetitionStatusData", data.getCompetitionStatusData());
        db.insert("Live", null, values);
        values.clear();
    }

    public void onDeleteAllInCompetition() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Competition", null, null);
    }

    public void onDeleteAllInLive() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Live", null, null);
    }

    public ArrayList<CompetitionItemData> onGetAllInCompetition() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<CompetitionItemData> values = new ArrayList<CompetitionItemData>();
        Cursor cursor = db.query("Competition", null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do{
                String objectId = cursor.getString(cursor.getColumnIndex("objectId"));
                String mName = cursor.getString(cursor.getColumnIndex("mName"));
                String mCollege = cursor.getString(cursor.getColumnIndex("mCollege"));
                String mCampus = cursor.getString(cursor.getColumnIndex("mCampus"));
                int mFollowNumber = cursor.getInt(cursor.getColumnIndex("mFollowNumber"));
                String mCoverUrl = cursor.getString(cursor.getColumnIndex("mCoverUrl"));
                String mType = cursor.getString(cursor.getColumnIndex("mType"));
                int mIsFinished = cursor.getInt(cursor.getColumnIndex("mIsFinished"));
                CompetitionItemData data = new CompetitionItemData();
                data.setID(objectId);
                data.setName(mName);
                data.setCollege(mCollege);
                data.setCampus(mCampus);
                data.setFollowNumber(mFollowNumber);
                data.setCoverUrl(mCoverUrl);
                data.setType(mType);
                if(mIsFinished > 0)
                    data.setIsFinished(true);
                else
                    data.setIsFinished(false);

                values.add(data);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return values;
    }

    public ArrayList<LiveItemData> onGetAllInLive() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<LiveItemData> values = new ArrayList<LiveItemData>();
        Cursor cursor = db.query("Live", null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do{
                String objectId = cursor.getString(cursor.getColumnIndex("objectId"));
                String mCampusData = cursor.getString(cursor.getColumnIndex("mCampusData"));
                String mCompetitionTypeData = cursor.getString(cursor.getColumnIndex("mCompetitionTypeData"));
                String mCompetitionTypeIconData = cursor.getString(cursor.getColumnIndex("mCompetitionTypeIconData"));
                String mTeamAIconData = cursor.getString(cursor.getColumnIndex("mTeamAIconData"));
                String mTeamANameData = cursor.getString(cursor.getColumnIndex("mTeamANameData"));
                String mTeamBIconData = cursor.getString(cursor.getColumnIndex("mTeamBIconData"));
                String mTeamBNameData = cursor.getString(cursor.getColumnIndex("mTeamBNameData"));
                String mGameNameData = cursor.getString(cursor.getColumnIndex("mGameNameData"));
                int mTeamAScoreData = cursor.getInt(cursor.getColumnIndex("mTeamAScoreData"));
                int mTeamBScoreData = cursor.getInt(cursor.getColumnIndex("mTeamBScoreData"));
                long mBeginTime = cursor.getInt(cursor.getColumnIndex("mBeginTime"));
                String mCompetitionStatusData = cursor.getString(cursor.getColumnIndex("mCompetitionStatusData"));

                LiveItemData data = new LiveItemData();
                data.setObjectId(objectId);
                data.setCampusData(mCampusData);
                data.setCompetitionTypeData(mCompetitionTypeData);
                data.setCompetitionTypeIconData(mCompetitionTypeIconData);
                data.setTeamAIconData(mTeamAIconData);
                data.setTeamANameData(mTeamANameData);
                data.setTeamBIconData(mTeamBIconData);
                data.setTeamBNameData(mTeamBNameData);
                data.setGameNameData(mGameNameData);
                data.setTeamAScoreData(mTeamAScoreData);
                data.setTeamBScoreData(mTeamBScoreData);
                data.setBeginTime(new Date(mBeginTime * 1000));
                data.setCompetitionStatusData(mCompetitionStatusData);

                values.add(data);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return values;
    }

}
