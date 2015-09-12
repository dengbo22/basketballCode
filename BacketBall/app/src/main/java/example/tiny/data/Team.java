package example.tiny.data;

import com.avos.avoscloud.AVObject;

/**
 * Created by tiny on 15-9-12.
 */
public class Team {
    private String mName;
    private String mLogoUrl;

    public Team() {
        super();
    }

    public Team(String name, String logo) {
        super();
        mName = name;
        mLogoUrl = logo;
    }

    public static Team FromAVObject(AVObject avObject) {
        Team team = new Team();
        team.mName = avObject.getString("name");
        team.mLogoUrl = avObject.getString("logoUrl");
        return team;
    }


    public String getName() {
        return mName;
    }

    public String getLogoUrl() {
        return mLogoUrl;
    }
}
