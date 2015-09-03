package example.tiny.backetball;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import example.tiny.data.User;
import example.tiny.widget.BasketBallApp;

/**
 * Created by tiny on 15-8-31.
 */
public class LoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    public void startMain(View v) {
        AVUser.logInInBackground("test", "123456", new Login());
    }


    private class Login extends LogInCallback {
        @Override
        public void done(AVUser avUser, AVException e) {
            if(avUser != null) {
                User user = new User();
                user.setObjectId(avUser.getObjectId());
                user.setWechatId(avUser.getString("wechatId"));
                user.setNickname(avUser.getString("nickname"));
                user.setAvatorUrl(avUser.getString("avatorUrl"));
                user.setEmailVerified(avUser.getBoolean("emailVerified"));
                user.setGender(avUser.getInt("gender"));
                user.setMobilePhoneVerified(avUser.getBoolean("mobilePhoneVerified"));
                ((BasketBallApp)getApplicationContext()).setUser(user);
                Toast.makeText(getApplicationContext(), "登陆成功！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            } else {
                Toast.makeText(getApplicationContext(), "登陆失败！" + e, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
