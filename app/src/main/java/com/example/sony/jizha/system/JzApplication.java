package com.example.sony.jizha.system;

import android.app.Application;

import com.example.sony.jizha.dao.DaoMaster;
import com.example.sony.jizha.dao.DaoSession;
import com.example.sony.jizha.model.Member;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：自定义application
 * 创建人：sony
 * 创建时间：2015/12/30 15:42
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class JzApplication extends Application {

    //JzApplication单例对象
    private static JzApplication mInstance;

    //登陆的会员
    private Member mLoginMember;

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    //mImgLoader对象
    private ImageLoader mImgLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mInstance == null)
            mInstance = this;
    }

    /**
     * 获取登陆会员对象
     *
     * @return
     */
    public Member getmLoginMember() {
        return mLoginMember;
    }

    /**
     * 获取JzApplication单例对象
     */

    public static synchronized JzApplication getInstance() {
        return mInstance;
    }

    public void setmLoginMember(Member mLoginMember) {
        this.mLoginMember = mLoginMember;
    }

    /**
     * 取得daoMaster
     *
     * @return
     */
    public DaoMaster getDaoMaster() {
        if (mDaoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(getApplicationContext(), Constant.DB_NAME, null);
            mDaoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return mDaoMaster;
    }

    /**
     * 取得daoSession
     *
     * @return
     */
    public DaoSession getDaoSession() {
        if (mDaoSession == null) {
            if (mDaoMaster == null) {
                mDaoMaster = getDaoMaster();
            }
            mDaoSession = mDaoMaster.newSession();
        }
        return mDaoSession;
    }

    public ImageLoader getmImgLoader() {
        if (mImgLoader == null) {
            mImgLoader = ImageLoader.getInstance();
            mImgLoader.init(initImgloadConf());
        }
        return mImgLoader;
    }

    /**
     * imageLoader设置
     *
     * @return
     */
    private ImageLoaderConfiguration initImgloadConf() {
        //设置缓存的路径
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "img/cache");

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .memoryCacheExtraOptions(1024, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                        // .diskCacheExtraOptions(480, 800,null)  // Can slow ImageLoader, use it carefully (Better don't use it)/设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3)//线程池内加载的数量,最好是1-5
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()

                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // 你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(20)
                        //
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100) //缓存的文件数量
                .diskCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径

                        // 将缓存下来的文件以什么方式命名
                        // 里面可以调用的方法有
                        // 1.new Md5FileNameGenerator() //使用MD5对UIL进行加密命名
                        // 2.new HashCodeFileNameGenerator()//使用HASHCODE对UIL进行加密命名
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)

                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(this, 20 * 1000, 30 * 1000)) // connectTimeout (20 s), readTimeout (30 s)超时时间
                .imageDecoder(new BaseImageDecoder(false)) // default
                .writeDebugLogs() // Remove for release app
                .build();//开始构建

        return config;
    }
}
