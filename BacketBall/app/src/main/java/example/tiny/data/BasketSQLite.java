package example.tiny.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by tiny on 15-8-23.
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



    public BasketSQLite(Context context, String name, android.database.sqlite.SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }


    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        db.execSQL(CREATE_COMPETITION_DATA);
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

    public void onDeleteAllInCompetition() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Competition", null, null);
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
}
