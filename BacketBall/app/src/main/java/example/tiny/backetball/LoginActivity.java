package example.tiny.backetball;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import example.tiny.data.User;
import example.tiny.utils.NetworkUtils;

/**
 * Created by tiny on 15-8-31.
 */
public class LoginActivity extends Activity  {
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progress = new ProgressDialog(this);
        progress.setCancelable(false);

        Button button = (Button) findViewById(R.id.btn_login_wx);
        button.setOnClickListener(new startWxListener());
    }


    public void startMain(View v) {
        progress.setTitle("登陆");
        progress.setMessage("正在登陆中..");
        progress.show();
        if(NetworkUtils.isOnline(this)) {
            AVUser.logInInBackground("test", "123456", new Login());
        }
        else
            Toast.makeText(LoginActivity.this, "网络状态不可用", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "登陆成功！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            } else {
                Toast.makeText(getApplicationContext(), "登陆失败！" + e, Toast.LENGTH_SHORT).show();
            }
            progress.dismiss();
        }
    }



    class startWxListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(NetworkUtils.isOnline(LoginActivity.this)) {
                progress.setTitle("登陆");
                progress.setMessage("正在等待微信响应...");
                progress.show();
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                BasketBallApp.api.sendReq(req);
            }else {
                Toast.makeText(LoginActivity.this, "网络状态不可用！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStop() {
        if(progress.isShowing())
            progress.dismiss();
        super.onStop();
    }
}
