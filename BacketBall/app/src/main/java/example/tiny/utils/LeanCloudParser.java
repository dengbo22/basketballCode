package example.tiny.utils;

import com.avos.avoscloud.AVUser;

import example.tiny.data.User;

/**
 * Created by tiny on 15-9-9.
 */
public class LeanCloudParser {

    public static AVUser UserToAVUser (User user) {
        AVUser result = new AVUser();
        result.put("objectId", user.getObjectId());
        result.put("nickname", user.getNickname());
        result.put("avatorUrl", user.getAvatorUrl());
        result.put("gender", user.getGender());
        return result;
    }


    public static User AVUserToUser(AVUser user) {
        User result = new User();
        result.setObjectId(user.getObjectId());
        result.setNickname(user.getString("nickname"));
        result.setAvatorUrl(user.getString("avatorUrl"));
        result.setGender(user.getInt("gender"));
        result.setWechatId(user.getString("wechatId"));
        result.setUsername(user.getUsername());
        result.setEmailVerified(user.getBoolean("emailVerified"));
        result.setMobilePhoneVerified(user.getBoolean("mobilePhoneVerified"));


        return result;
    }
}
