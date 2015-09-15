package example.tiny.backetball;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.UpdatePasswordCallback;

import example.tiny.widget.CountDownButton;

/**
 * Created by tiny on 15-9-12.
 */
public class ResetPasswordActivity extends Activity {
    EditText mEdtTxtPhoneNumber;
    EditText mEdtTxtAuthCode;
    EditText mEdtTxtPassword;
    EditText mEdtTxtPasswordEnsure;
    CountDownButton mBtnGetAuth;
    ImageView mImgReset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initView();
        initEvent();
    }


    private void initView() {
        RelativeLayout header = (RelativeLayout) findViewById(R.id.rl_reset_password_header);
        ImageView headerImg = (ImageView) header.findViewById(R.id.imgBtn_header_concern);
        headerImg.setVisibility(View.GONE);
        TextView headerTxt = (TextView) header.findViewById(R.id.tv_header_text);
        headerTxt.setText("忘记密码");
        mEdtTxtPhoneNumber = (EditText) findViewById(R.id.edtTxt_phone_number);
        mBtnGetAuth = (CountDownButton) findViewById(R.id.img_getauth);
        mEdtTxtAuthCode = (EditText) findViewById(R.id.edtTxt_auth_code);
        mEdtTxtPassword = (EditText) findViewById(R.id.edtTxt_password);
        mEdtTxtPasswordEnsure = (EditText) findViewById(R.id.edtTxt_reset_ensurepwd);
        mImgReset = (ImageView) findViewById(R.id.img_reset_password_finish);

    }

    private void initEvent() {
        mBtnGetAuth.setCountDownClickListener(new CountDownButton.CountDownButtonListener() {
            @Override
            public boolean onClick(View v) {
                String phone = mEdtTxtPhoneNumber.getText().toString().trim();
                Log.e("ResetPasswordActivity", phone);
                if (phone.matches("[1][358]\\d{9}")) {
                    AVUser.requestPasswordResetBySmsCodeInBackground(mEdtTxtPhoneNumber.getText().toString(), new RequestMobileCodeCallback() {
                        @Override
                        public void done(AVException e) {
                            if(e != null)
                                Toast.makeText(ResetPasswordActivity.this, "请求验证码错误：" + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return true;
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "手机输入有误！", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });


        mImgReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ValidCheck()) {
                    AVUser.resetPasswordBySmsCodeInBackground(mEdtTxtAuthCode.getText().toString(), mEdtTxtPassword.getText().toString(), new UpdatePasswordCallback() {
                        @Override
                        public void done(AVException e) {
                            if(e == null) {
                                finish();
                            }else
                                Toast.makeText(ResetPasswordActivity.this, "验证错误！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    private boolean ValidCheck() {
        String phone = mEdtTxtPhoneNumber.getText().toString().trim();
        //PhoneNumber通过验证
        if (phone.matches("[1][358]\\d{9}")) {

            String password = mEdtTxtPassword.getText().toString();
            if (password.length() >= 6 && password.length() <= 20) {
            //密码长度验证通过
                if(password.equals(mEdtTxtPasswordEnsure.getText().toString()))
                    return true;
                else
                    Toast.makeText(ResetPasswordActivity.this, "两次输入密码不一致！", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(ResetPasswordActivity.this, "密码长度只能在6~20位！", Toast.LENGTH_SHORT).show();

        }else
            Toast.makeText(ResetPasswordActivity.this, "非法的手机号码！", Toast.LENGTH_SHORT).show();
        return false;
    }


}
