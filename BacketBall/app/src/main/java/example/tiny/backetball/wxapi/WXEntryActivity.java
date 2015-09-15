package example.tiny.backetball.wxapi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import org.json.JSONException;
import org.json.JSONObject;

import example.tiny.backetball.BasketBallApp;
import example.tiny.backetball.LoginActivity;
import example.tiny.backetball.MainActivity;
import example.tiny.backetball.R;
import example.tiny.data.ChineseStringRequest;

/**
 * Created by tiny on 15-9-5.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String LOG_TAG = "WXEntryActivity";
    private ProgressDialog dialog = null;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        mQueue = Volley.newRequestQueue(this);
        BasketBallApp.api.handleIntent(getIntent(), this);

    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        BasketBallApp.api.handleIntent(intent, this);
    }



    @Override
    public void onReq(BaseReq baseReq) {
        Log.e(LOG_TAG, "onRep called");
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if(baseResp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    dialog = new ProgressDialog(this);
                    dialog.setTitle("登陆");
                    dialog.setMessage("正在使用微信登陆,请稍后...");
                    dialog.show();
                    String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + BasketBallApp.WxAppId + "&secret=" + BasketBallApp.WxAppKey +
                            "&code=" + ((SendAuth.Resp) baseResp).code + "&grant_type=authorization_code";
                    StringRequest request = new StringRequest(URL, new WxLoginSuccessListener(), new WxLoginFailListener());
                    mQueue.add(request);
                    break;
                //用户取消
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    Toast.makeText(WXEntryActivity.this, "用户取消！", Toast.LENGTH_SHORT).show();
                    WXEntryActivity.this.finish();
                    break;

                //用户拒绝授权
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    Toast.makeText(WXEntryActivity.this, "用户拒绝授权！", Toast.LENGTH_SHORT).show();
                    WXEntryActivity.this.finish();
                    break;
            }
        } else if(baseResp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            //分享请求
            Log.i(LOG_TAG, "onResp回调分享");
            WXEntryActivity.this.finish();
        }else {
            Log.e(LOG_TAG, "未知的onResp调用" + baseResp);
        }
    }

    class WxLoginSuccessListener implements Response.Listener {
        @Override
        public void onResponse(Object response) {
            try {
                JSONObject json = new JSONObject((String) response);
                String access = json.getString("access_token");
                String refresh = json.getString("refresh_token");
                String openId = json.getString("openid");
                String Url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access + "&openid=" + openId;
                ChineseStringRequest request = new ChineseStringRequest(Url, new WxGetUserInfoSuccessListener(), new WxGetUserInfoFailListener() );
                mQueue.add(request);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class WxLoginFailListener implements Response.ErrorListener{
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(WXEntryActivity.this, "Error:" + error, Toast.LENGTH_SHORT).show();
        }
    }

    class WxGetUserInfoSuccessListener implements Response.Listener {
        @Override
        public void onResponse(Object response) {
            try {
                Log.e(LOG_TAG, "resp:" + response);
                JSONObject json = new JSONObject((String) response);
                String name = json.getString("nickname");
                int gender = json.getInt("sex");
                String avator = json.getString("headimgurl");
                final String unionid = json.getString("unionid");
                AVUser user = new AVUser();
                user.setUsername(unionid);
                user.setPassword(unionid);
                user.put("wechatId", unionid);
                user.put("nickname", name);
                user.put("avatorUrl", avator);
                user.put("gender", gender);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e == null) {
                            //将Application中的当前用户设置
                            Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
                            dialog.dismiss();
                            startActivity(intent);
                            WXEntryActivity.this.finish();
                        }else if(e.getCode() == AVException.USERNAME_TAKEN) {
                            AVUser.logInInBackground(unionid, unionid, new LogInCallback<AVUser>() {
                                @Override
                                public void done(AVUser avUser, AVException e) {
                                    if(avUser != null) {
                                        Log.e(LOG_TAG, "Login Success");
                                        //修改当前用户
                                        //登陆成功
                                        Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
                                        dialog.dismiss();
                                        LoginActivity.instance.setLoginState(true);
                                        WXEntryActivity.this.finish();
                                    }else {
                                        Log.e(LOG_TAG, "用户名已存在，但是使用以存在的用户名登陆错误");
                                    }
                                }
                            });
                        }else {
                            Log.e(LOG_TAG, "其他错误：" + e);
                        }
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    class WxGetUserInfoFailListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    }


}
