package example.tiny.backetball;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.File;

import example.tiny.backetball.R;
import example.tiny.data.User;

/**
 * Created by tiny on 15-8-20.
 */
public class BasketBallApp extends Application {
    private static final String AppId = "n4ibkpr4z9d8tkdlg7k0j6xywwwb28k2jw8fzmj5vrxeve4c";
    private static final String AppKey = "ehsn3wgg56185yofd51sdh9ccifhb1cpa4m8s1stm4slrbef";
    public static final String WxAppId = "wx900af100060a3a8e";
    public static final String WxAppKey = "cfdfb2a20820feb0d35673ecf6454a9d";
    public static IWXAPI api;
    private final int TIME_OUT = 5000;
    private User mUser = null;
    static DisplayImageOptions options;
    static DisplayImageOptions mTeamIconOptions;


    @Override
    public void onCreate() {
        super.onCreate();
//        AVOSCloud.setNetworkTimeout(TIME_OUT);
//        AVOSCloud.setDebugLogEnabled(true);
        AVOSCloud.initialize(this, AppId, AppKey);
        api = WXAPIFactory.createWXAPI(this, WxAppId);
        api.registerApp(WxAppId);
        CrashReport.initCrashReport(this, "900009188", false);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .memoryCacheExtraOptions(480, 800) // max width, max height
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)  //降低线程的优先级保证主UI线程不受太大影响
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(5 * 1024 * 1024)) //建议内存设在5-10M,可以有比较好的表现
                .memoryCacheSize(5 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100) //缓存的文件数量
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)
                .build();

        ImageLoader.getInstance().init(config);

    }

    public static DisplayImageOptions getBackGroundOptions() {
        if (options == null)
            options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_background)
                .showImageForEmptyUri(R.drawable.default_background)
                .showImageOnFail(R.drawable.default_background)
                    .cacheInMemory(true)
                    .cacheOnDisc(true)
                    .imageScaleType(ImageScaleType.NONE)
                    .bitmapConfig(Bitmap.Config.RGB_565)//设置为RGB565比起默认的ARGB_8888要节省大量的内存
                    .delayBeforeLoading(100)//载入图片前稍做延时可以提高整体滑动的流畅度
                    .build();
        return options;
    }

    public static DisplayImageOptions getTeamIconOptions() {
        if (mTeamIconOptions == null)
            mTeamIconOptions = new DisplayImageOptions.Builder()
                    .showStubImage(R.drawable.default_team_logo)
                    .showImageForEmptyUri(R.drawable.default_team_logo)
                    .showImageOnFail(R.drawable.default_team_logo)
                    .cacheInMemory(true)
                    .cacheOnDisc(true)
                    .imageScaleType(ImageScaleType.NONE)
                    .bitmapConfig(Bitmap.Config.RGB_565)//设置为RGB565比起默认的ARGB_8888要节省大量的内存
                    .delayBeforeLoading(100)//载入图片前稍做延时可以提高整体滑动的流畅度
                    .build();
        return mTeamIconOptions;
    }



}
