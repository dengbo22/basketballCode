package example.tiny.backetball;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
        Log.e(LOG_TAG, "from:" + getIntent().getStringExtra("from"));
        if(mMainFragment == null)
            mMainFragment = new MainLoginFragment();
        getFragmentManager().beginTransaction().add(R.id.fragment_main_login, mMainFragment).commit();
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
    protected void onStop() {
        if (progress.isShowing())
            progress.dismiss();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        instance = null;
        Intent result = new Intent();
        result.putExtra("login", mIsLogin);
        setResult(RESULT_OK, result);
        super.onBackPressed();

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
