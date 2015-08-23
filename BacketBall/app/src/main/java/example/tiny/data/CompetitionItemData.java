package example.tiny.data;

/**
 * Created by tiny on 15-8-22.
 */
public class CompetitionItemData  {


    String ID;
    String mName;
    String mCollege;
    String mCampus;
    int mFollowNumber;
    String mCoverUrl;
    String mType;
    boolean mIsFinished;

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }


    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getCollege() {
        return mCollege;
    }

    public void setCollege(String mCollege) {
        this.mCollege = mCollege;
    }

    public String getCampus() {
        return mCampus;
    }

    public void setCampus(String mCampus) {
        this.mCampus = mCampus;
    }

    public int getFollowNumber() {
        return mFollowNumber;
    }

    public void setFollowNumber(int mFollowNumber) {
        this.mFollowNumber = mFollowNumber;
    }

    public String getCoverUrl() {
        return mCoverUrl;
    }

    public void setCoverUrl(String mCoverUrl) {
        this.mCoverUrl = mCoverUrl;
    }

    public boolean getIsFinished() {
        return mIsFinished;
    }

    public void setIsFinished(boolean mIsFinished) {
        this.mIsFinished = mIsFinished;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        String returnValue ;
        returnValue = ID + "\t" + mName
        +"\t" + mCollege
         +"\t" + mCampus
        +"\t" + mFollowNumber
        +"\t" + mCoverUrl
        +"\t"+ mType
        + "\t" + mIsFinished;
        return returnValue;
    }
}
