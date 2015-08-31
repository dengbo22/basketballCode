package example.tiny.data;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by tiny on 15-8-26.
 */
public class User {
    private String objectId;
    private String wechatId;
    private String nickname;

    private String avatorUrl;
    private boolean emailVerified;
    private int gender;
    private String mobilePhoneNumber;
    private String username;
    private String password;
    private boolean mobilePhoneVerified;



    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getMobilePhoneVerified() {
        return mobilePhoneVerified;
    }

    public void setMobilePhoneVerified(boolean mobilePhoneVerified) {
        this.mobilePhoneVerified = mobilePhoneVerified;
    }



    public static User FromJSon(JSONObject obj) {
        User user = new User();
        user.wechatId = obj.getString("wechatId");
        user.nickname = obj.getString("nickname");
        user.username = obj.getString("username");
        user.avatorUrl = obj.getString("avatorUrl");
        user.emailVerified = obj.getBoolean("emailVerified");
        user.gender = obj.getInteger("gender");
        user.mobilePhoneVerified = obj.getBoolean("mobilePhoneVerified");
        return user;
    }

    @Override
    public String toString() {
        return "wechatId:"+ wechatId +
         "\tnickname:" + nickname +
         "\tusername" +username +
        "\tavatorUrl" + avatorUrl +
                "\temailVerified" +emailVerified +
                "\tgender" + gender
        +"\tmobilePhoneVerified" +mobilePhoneVerified;
    }

    public String getAvatorUrl() {
        return avatorUrl;
    }

    public void setAvatorUrl(String avatorUrl) {
        this.avatorUrl = avatorUrl;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
