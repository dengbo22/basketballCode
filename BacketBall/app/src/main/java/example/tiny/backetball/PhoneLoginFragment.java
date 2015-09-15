package example.tiny.backetball;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

/**
 * Created by tiny on 15-9-15.
 */
public class PhoneLoginFragment extends Fragment {
    EditText mEdtTxtLoginPhone = null;
    EditText mEdtTxtLoginPassword = null;
    TextView mTvLoginForgetPassword = null;
    ImageView mImgLogin = null;
    TextView mTvLoginRegister = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login_phone, container, false);
        mEdtTxtLoginPhone = (EditText) view.findViewById(R.id.edtTxt_login_phone_number);
        mEdtTxtLoginPassword = (EditText) view.findViewById(R.id.edtTxt_login_password);
        mTvLoginForgetPassword = (TextView) view.findViewById(R.id.tv_login_forget_password);
        mImgLogin = (ImageView) view.findViewById(R.id.img_login_ensure);
        mTvLoginRegister = (TextView) view.findViewById(R.id.tv_login_registe);
        RelativeLayout lHeader = (RelativeLayout) view.findViewById(R.id.rl_login_header);
        TextView lHeaderText = (TextView) lHeader.findViewById(R.id.tv_header_text);
        lHeaderText.setText("手机登陆");
        lHeader.findViewById(R.id.imgBtn_header_concern).setVisibility(View.GONE);

        initEvent();
        return view;

    }


    private void initEvent() {
        mTvLoginForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        mTvLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        mImgLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = mEdtTxtLoginPhone.getText().toString().trim();
                String password = mEdtTxtLoginPassword.getText().toString();
                if (!"".equals(user) && !"".equals(password))
                    AVUser.logInInBackground(user, password, new LogInCallback<AVUser>() {
                        @Override
                        public void done(AVUser avUser, AVException e) {
                            if (e == null) {
                                Toast.makeText(getActivity(), "登陆成功", Toast.LENGTH_SHORT).show();
                                LoginActivity.instance.setLoginState(true);
                                getActivity().finish();
                            }
                            else
                                Toast.makeText(getActivity(), "登陆错误：" + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                else
                    Toast.makeText(getActivity(), "账户或密码不能为空", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
