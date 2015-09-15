package example.tiny.backetball;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

/**
 * Created by tiny on 15-9-12.
 */
public class PhoneLoginActivity extends Activity {
    public static PhoneLoginActivity instance;
    EditText mEdtTxtLoginPhone = null;
    EditText mEdtTxtLoginPassword = null;
    TextView mTvLoginForgetPassword = null;
    ImageView mImgLogin = null;
    TextView mTvLoginRegiste = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_login_phone);
        initViews();
        initEvent();
    }

    private void initViews() {
        mEdtTxtLoginPhone = (EditText) findViewById(R.id.edtTxt_login_phone_number);
        mEdtTxtLoginPassword = (EditText) findViewById(R.id.edtTxt_login_password);
        mTvLoginForgetPassword = (TextView) findViewById(R.id.tv_login_forget_password);
        mImgLogin = (ImageView) findViewById(R.id.img_login_ensure);
        mTvLoginRegiste = (TextView) findViewById(R.id.tv_login_registe);
        RelativeLayout lHeader = (RelativeLayout) findViewById(R.id.rl_login_header);
        TextView lHeaderText = (TextView) lHeader.findViewById(R.id.tv_header_text);
        lHeaderText.setText("手机登陆");
        lHeader.findViewById(R.id.imgBtn_header_concern).setVisibility(View.GONE);
    }

    private void initEvent() {
        mImgLogin.setOnClickListener(new LoginClickListener());

        mTvLoginForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhoneLoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
        mTvLoginRegiste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhoneLoginActivity.this, RegisteActivity.class);
                startActivity(intent);
            }
        });
    }

    class LoginClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String userString = mEdtTxtLoginPhone.getText().toString().trim();
            String password;
            if (userString.length() != 11 || !userString.matches("[0-9]+")) {
                Toast.makeText(PhoneLoginActivity.this, "手机号码输入有误", Toast.LENGTH_SHORT).show();
            } else {
                password = mEdtTxtLoginPassword.getText().toString().trim();
                if (password.length() < 6 || password.length() > 20) {
                    Toast.makeText(PhoneLoginActivity.this, "密码格式错误", Toast.LENGTH_SHORT).show();
                } else {

                    AVUser.logInInBackground(userString, password, new LogInCallback<AVUser>() {
                        @Override
                        public void done(AVUser avUser, AVException e) {
                            if (e == null) {
                                Intent intent = new Intent(PhoneLoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                PhoneLoginActivity.this.finish();
                            }else
                                Toast.makeText(PhoneLoginActivity.this, "登陆错误：" + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        instance = null;
        super.onDestroy();
    }
}
