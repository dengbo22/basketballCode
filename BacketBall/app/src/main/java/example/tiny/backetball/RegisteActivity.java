package example.tiny.backetball;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;

import example.tiny.widget.CountDownButton;

/**
 * Created by tiny on 15-9-12.
 */
public class RegisteActivity extends Activity {
    EditText mEdtTxtRegisterPhone;
    EditText mEdtTxtRegisterAuth;
    EditText mEdtTxtRegisterPassword;
    EditText mEdtTxtRegisterNickname;
    CountDownButton mBtnRegisterGetAuth;
    ImageView mImgRegisterFinish;
    TextView mTvLogin;
    String mInnerPhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe);
        InitView();
        InitEvent();
    }


    void InitView() {
        mEdtTxtRegisterPhone = (EditText) findViewById(R.id.edtTxt_phone_number);
        mEdtTxtRegisterAuth = (EditText) findViewById(R.id.edtTxt_auth_code);
        mEdtTxtRegisterPassword = (EditText) findViewById(R.id.edtTxt_password);
        mEdtTxtRegisterNickname = (EditText) findViewById(R.id.edtTxt_regist_nickname);
        mBtnRegisterGetAuth = (CountDownButton) findViewById(R.id.img_getauth);
        mImgRegisterFinish = (ImageView) findViewById(R.id.img_regist_finish);
        mTvLogin = (TextView) findViewById(R.id.tv_regist_login);
        RelativeLayout lHeader = (RelativeLayout) findViewById(R.id.rl_registe_header);
        TextView tv = (TextView) lHeader.findViewById(R.id.tv_header_text);
        tv.setText("注册账号");
        lHeader.findViewById(R.id.imgBtn_header_concern).setVisibility(View.GONE);

    }

    void InitEvent() {
        mBtnRegisterGetAuth.setCountDownClickListener(new CountDownButton.CountDownButtonListener() {
            @Override
            public boolean onClick(View view) {
                String phone = mEdtTxtRegisterPhone.getText().toString();
                if (phone.matches("[1][358]\\d{9}")) {
                    mInnerPhoneNumber = phone;
                    Toast.makeText(RegisteActivity.this, "输入正确，请求验证码！", Toast.LENGTH_SHORT).show();
                    AVOSCloud.requestSMSCodeInBackground(mInnerPhoneNumber, new RequestMobileCodeCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null)
                                Log.e("RegisteActivity", "请求验证码完成！");
                            else
                                Toast.makeText(RegisteActivity.this, "Error:" + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return true;
                } else {
                    Toast.makeText(RegisteActivity.this, "错误的手机号码！", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });


        mImgRegisterFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidCheck()) {
                    AVUser user = new AVUser();
                    user.setUsername(mInnerPhoneNumber);
                    user.setPassword(mEdtTxtRegisterPassword.getText().toString());
                    user.setMobilePhoneNumber(mInnerPhoneNumber);
                    String verifyCode = mEdtTxtRegisterAuth.getText().toString();
                    AVOSCloud.verifySMSCodeInBackground(verifyCode, mInnerPhoneNumber, new AVMobilePhoneVerifyCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                //保存用户
                                AVUser user = new AVUser();
                                user.setUsername(mInnerPhoneNumber);
                                user.setPassword(mEdtTxtRegisterPassword.getText().toString());
                                user.put("nickname", mEdtTxtRegisterNickname.getText().toString());
                                user.signUpInBackground(new SignUpCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e == null)
                                            Toast.makeText(RegisteActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(RegisteActivity.this, "SignUp错误:" + e, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                finish();
                            } else {
                                Toast.makeText(RegisteActivity.this, "VerifySMSCode错误：" + e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        mTvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private boolean ValidCheck() {
        String password = mEdtTxtRegisterPassword.getText().toString();
        if (password.length() < 6 || password.length() > 20) {
            Toast.makeText(RegisteActivity.this, "密码长度不正确，请重试！", Toast.LENGTH_SHORT).show();
            return false;
        }
        String nickname = mEdtTxtRegisterNickname.getText().toString().trim();
        if (nickname.contains(" ")) {
            Toast.makeText(RegisteActivity.this, "昵称不合法：包含空格！", Toast.LENGTH_SHORT).show();
            return false;
        } else if (nickname.length() > 20 || nickname.length() < 1) {
            Toast.makeText(RegisteActivity.this, "昵称长度不合理！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
