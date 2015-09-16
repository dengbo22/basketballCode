package example.tiny.backetball;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnonymousUtils;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.tencent.mm.sdk.modelmsg.SendAuth;

import example.tiny.utils.NetworkUtils;

/**
 * Created by tiny on 15-9-15.
 */
public class MainLoginFragment extends Fragment {
    ImageView mImgLoginWx;
    ImageView mImgLoginPhone;
    TextView mTvLoginAnonymous;
    PhoneLoginFragment mInnerLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_login, container, false);
        mImgLoginWx = (ImageView) view.findViewById(R.id.btn_login_wx);
        mImgLoginPhone = (ImageView) view.findViewById(R.id.img_login_phone);
        mTvLoginAnonymous = (TextView) view.findViewById(R.id.tv_login_nouser);
        initEvent();
        return view;
    }


    private void initEvent() {
        mImgLoginWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isOnline(getActivity())) {
                    SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    BasketBallApp.api.sendReq(req);
                } else
                    Toast.makeText(getActivity(), "网络状态不可用", Toast.LENGTH_SHORT).show();
            }
        });

        mImgLoginPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
                mInnerLogin = new PhoneLoginFragment();
                transaction.add(R.id.fragment_main_login, mInnerLogin, "Tag");
                transaction.hide(((LoginActivity) getActivity()).mMainFragment);
                transaction.addToBackStack("TAG");
                transaction.commit();
            }
        });

        mTvLoginAnonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVAnonymousUtils.logIn(new LogInCallback<AVUser>() {
                    @Override
                    public void done(AVUser avUser, AVException e) {
                        LoginActivity.instance.setLoginState(true);
                        getActivity().finish();
                    }
                });
            }
        });

    }
}
