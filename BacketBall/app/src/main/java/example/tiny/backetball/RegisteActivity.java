package example.tiny.backetball;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import example.tiny.widget.CountDownButton;

/**
 * Created by tiny on 15-9-12.
 */
public class RegisteActivity extends Activity {
    EditText mEdtTxtRegisterPhone;
    EditText mEdtTxtRegisterAuth;
    EditText mEdtTxtRegisterPassword;
    EditText mEdtTxtRegisterNickname;
    CountDownButton mImgRegisterGetAuth;
    ImageView mImgRegisterFinish;
    TextView mTvLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe);
        InitView();

    }



    void InitView() {
        mEdtTxtRegisterPhone = (EditText) findViewById(R.id.edtTxt_regist_phone_number);
        mEdtTxtRegisterAuth = (EditText) findViewById(R.id.edtTxt_regist_auth);
        mEdtTxtRegisterPassword = (EditText) findViewById(R.id.edtTxt_regist_password);
        mEdtTxtRegisterNickname = (EditText)findViewById(R.id.edtTxt_regist_nickname);
        mImgRegisterGetAuth = (CountDownButton) findViewById(R.id.img_registe_getauth);
        mImgRegisterFinish = (ImageView) findViewById(R.id.img_regist_finish);
        mTvLogin = (TextView) findViewById(R.id.tv_regist_login);
        RelativeLayout lHeader = (RelativeLayout) findViewById(R.id.rl_registe_header);
        TextView tv = (TextView) lHeader.findViewById(R.id.tv_header_text);
        tv.setText("注册账号");
        lHeader.findViewById(R.id.imgBtn_header_concern).setVisibility(View.GONE);

    }

}
