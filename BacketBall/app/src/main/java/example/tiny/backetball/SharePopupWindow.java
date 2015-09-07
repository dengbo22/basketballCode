package example.tiny.backetball;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;

import example.tiny.utils.ScreenUtils;

/**
 * Created by tiny on 15-9-7.
 */
public class SharePopupWindow extends Activity {
    Button mBtnShareCancel = null;
    ImageView mImgShareMoments = null;
    ImageView mImgShareWechat = null;
    ImageView mImgShareQQ = null;
    ImageView mImgShareWeibo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow_share);
        mBtnShareCancel = (Button) findViewById(R.id.btn_share_cancel);
        mImgShareMoments = (ImageView) findViewById(R.id.img_share_moments);
        mImgShareWechat = (ImageView) findViewById(R.id.img_share_wechat);
        mImgShareQQ = (ImageView)findViewById(R.id.img_share_qq);
        mImgShareWeibo = (ImageView) findViewById(R.id.img_share_weibo);
        mBtnShareCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mImgShareMoments.setOnClickListener(new ShareToMoments());
        mImgShareWechat.setOnClickListener(new ShareToWeChat());
        this.setFinishOnTouchOutside(true);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        View view = getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        Log.e("SharePopupWindow", "x:" + lp.x + "y:" + lp.y);
        lp.y = ScreenUtils.getScreenHeight(this) / 2 ;
        getWindowManager().updateViewLayout(view, lp);

    }

    private class ShareToMoments implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = "http://sharetest";
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = "测试Title";
            msg.description = "测试Description";
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.share_test_picture);
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150,150, true);
            bmp.recycle();
            msg.setThumbImage(thumbBmp);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = String.valueOf(System.currentTimeMillis());
            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
            BasketBallApp.api.sendReq(req);
        }
    }

    private class ShareToWeChat implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = "http://sharetest";
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = "测试Title";
            msg.description = "测试Description";
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.share_test_picture);
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150,150, true);
            bmp.recycle();
            msg.setThumbImage(thumbBmp);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = String.valueOf(System.currentTimeMillis());
            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneSession;
            BasketBallApp.api.sendReq(req);
        }
    }

    private class ShareToQQ implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }

    private class ShareToWeibo implements  View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }

}
