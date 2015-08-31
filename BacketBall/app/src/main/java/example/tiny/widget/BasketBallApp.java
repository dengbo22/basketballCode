package example.tiny.widget;

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

import java.io.File;

import example.tiny.backetball.R;
import example.tiny.data.User;

/**
 * Created by tiny on 15-8-20.
 */
public class BasketBallApp extends Application {
    private String AppId = "n4ibkpr4z9d8tkdlg7k0j6xywwwb28k2jw8fzmj5vrxeve4c";
    private String AppKey = "ehsn3wgg56185yofd51sdh9ccifhb1cpa4m8s1stm4slrbef";
    private final int TIME_OUT = 5000;
    private User mUser = null;

    @Override
    public void onCreate() {
        super.onCreate();
//        AVOSCloud.setNetworkTimeout(TIME_OUT);
//        AVOSCloud.setDebugLogEnabled(true);
        AVOSCloud.initialize(this, AppId, AppKey);
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache"); //缓存文件的存放地址
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


    public void setUser(User mUser) {
        if(mUser == null) {
            //在sharedpreference中保存用户名和密码
        }

        this.mUser = mUser;
    }

    public User getUser() {
        return mUser;
    }


}
