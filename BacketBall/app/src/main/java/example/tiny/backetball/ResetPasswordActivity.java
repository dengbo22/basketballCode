package example.tiny.backetball;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    }


    private void initView() {
        RelativeLayout header = (RelativeLayout) findViewById(R.id.rl_reset_password_header);
        ImageView headerImg = (ImageView) header.findViewById(R.id.imgBtn_header_concern);
        headerImg.setVisibility(View.GONE);
        TextView headerTxt = (TextView) header.findViewById(R.id.tv_header_text);
        headerTxt.setText("忘记密码");
        mEdtTxtPhoneNumber = (EditText)findViewById(R.id.edtTxt_phone_number);
        mBtnGetAuth = (CountDownButton)findViewById(R.id.img_getauth);
        mEdtTxtAuthCode = (EditText) findViewById(R.id.edtTxt_auth_code);
        mEdtTxtPassword = (EditText)findViewById(R.id.edtTxt_password);
        mEdtTxtPasswordEnsure = (EditText) findViewById(R.id.edtTxt_reset_ensurepwd);
        mImgReset = (ImageView) findViewById(R.id.img_reset_password_finish);

    }


    private void initEvent() {
        mBtnGetAuth.setCountDownClickListener(new CountDownButton.CountDownButtonListener() {
            @Override
            public boolean onClick(View v) {
                String phone = mEdtTxtPhoneNumber.getText().toString().trim();
                if(phone.length() != 11 || !phone.matches("[0-9]+")) {
                    Toast.makeText(ResetPasswordActivity.this, "手机输入有误！", Toast.LENGTH_SHORT).show();
                    return false;
                }else {
                    //获取验证码——LeanCloud
                    return true;
                }
            }
        });




        mImgReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mEdtTxtPhoneNumber.getText().toString().trim();
                if(phone.length() != 11 || !phone.matches("[0-9]+")) {
                    Toast.makeText(ResetPasswordActivity.this, "手机输入有误！", Toast.LENGTH_SHORT).show();
                }else {
                    //检查密码和确认密码
                    String password = mEdtTxtPassword.getText().toString();
                    String ensurePassword = mEdtTxtPasswordEnsure.getText().toString();
                    if(password.length() <= 20 && password.length() >= 6 && password.equals(ensurePassword)) {
                        //检查正常，执行操作 -》获取验证码




                    }else {
                        Toast.makeText(ResetPasswordActivity.this, "确认密码有误或长度不满足6~20位，请修改后重试！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
