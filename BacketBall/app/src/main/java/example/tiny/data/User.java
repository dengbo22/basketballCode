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
    private String mobilePhoneVerified;


    public User() {
        super();
        avatorUrl = "";
        gender = 0;
        nickname = "Tiny";

    }

    public static User FromJSon(JSONObject obj) {
        User user = new User();
        user.wechatId = obj.getString("wechatId");
        user.nickname = obj.getString("nickname");
        user.username = obj.getString("username");
        user.avatorUrl = obj.getString("avatorUrl");
        user.emailVerified = obj.getBoolean("emailVerified");
        user.gender = obj.getInteger("gender");
        user.mobilePhoneVerified = obj.getString("mobilePhoneVerified");
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
