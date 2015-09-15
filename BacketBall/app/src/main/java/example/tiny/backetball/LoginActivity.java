package example.tiny.backetball;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;



/**
 * Created by tiny on 15-8-31.
 */
public class LoginActivity extends Activity {
    private static final String LOG_TAG = "LoginActivity";
    MainLoginFragment mMainFragment;
    private boolean mIsLogin = false;

    ProgressDialog progress;
    public static LoginActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(mMainFragment == null)
            mMainFragment = new MainLoginFragment();
        getFragmentManager().beginTransaction().add(R.id.fragment_main_login, mMainFragment).commit();
        Log.e(LOG_TAG, "add Finish");
        instance = this;
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mIsLogin)
            finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        if (progress.isShowing())
            progress.dismiss();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }


    void ShowDialog() {
        if (progress == null) {
            progress = new ProgressDialog(this);
            progress.setCancelable(false);
        }
        progress.setTitle("登陆");
        progress.setMessage("正在请求登陆...");
        progress.show();
    }

    void DismissDialog() {
        progress.dismiss();
    }

    public void setLoginState(boolean isLogin) {
        this.mIsLogin = isLogin;
    }

}
