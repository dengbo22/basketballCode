package example.tiny.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import example.tiny.backetball.R;

/**
 * Created by tiny on 15-9-12.
 */
public class CountDownButton extends Button {
    private static final String LOG_TAG = "CountDownButton";
    private static final int MSG_COUNTING_DOWN = 0;
    private static final int MSG_COUNTING_STOP = 1;

    private static final int DEF_TIME = 5;
    private boolean isCountDown = false;
    private int mCurrentTime = DEF_TIME;
    private Handler mHandler = null;
    Timer timer = null;
    TimerTask task = null;

    public CountDownButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHandler = new CountDonwHandler(this);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    if (listener.onClick(v)) {
                        if (!isCountDown)
                            startCountDown();
                        else
                            stopCountDown();
                    }

                }

            }
        });
    }


    private void stopCountDown() {
        setEnabled(true);
        setBackgroundResource(R.drawable.btn_get_auth);
        setText("");
        if (timer != null && task != null) {
            task.cancel();
            timer.cancel();
            timer = null;
            task = null;
        }
        isCountDown = false;

    }

    private void startCountDown() {
        setEnabled(false);
        setBackgroundResource(R.drawable.btn_auth_disabled);
        mCurrentTime = DEF_TIME;
        if (timer == null)
            timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                mCurrentTime--;
                if (mCurrentTime <= 0) {
                    mHandler.sendEmptyMessage(MSG_COUNTING_STOP);
                } else {
                    mHandler.sendEmptyMessage(MSG_COUNTING_DOWN);
                }
            }
        };
        timer.schedule(task, 0, 1000);
        isCountDown = true;

    }

    private static class CountDonwHandler extends Handler {
        private final WeakReference<CountDownButton> mButton;

        public CountDonwHandler(CountDownButton button) {
            super();
            mButton = new WeakReference<CountDownButton>(button);
        }

        @Override
        public void handleMessage(Message msg) {
            final CountDownButton button = mButton.get();
            switch (msg.what) {
                case MSG_COUNTING_DOWN:
                    button.setText(button.mCurrentTime + "秒");
                    break;
                case MSG_COUNTING_STOP:
                    mButton.get().stopCountDown();
                    break;
                default:
                    Log.e("CountDownButton", "Error Message:" + msg.what);
            }
        }
    }

    private CountDownButtonListener listener;

    public interface CountDownButtonListener {
        public boolean onClick(View view);

    }


    public void setCountDownClickListener(CountDownButtonListener listener) {
        this.listener = listener;
    }


}
