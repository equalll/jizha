package com.example.sony.jizha.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.sony.jizha.model.AppInfo;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：获取客户端应用信息的工具类
 * 创建人：sony
 * 创建时间：2016/1/1 15:59
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class AppUtil {

    /**
     * 获取客户端应用的信息
     *
     * @param context 上下文
     * @return 一应用信息
     */
    public static AppInfo getAppInfo(Context context) {
        //获取包管理对象
        PackageManager manager = context.getPackageManager();
        try {
            //获取包的信息
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            //包名
            String packageName = info.packageName;
            //版本号
            int versionCode = info.versionCode;
            //版本名
            String versionName = info.versionName;

            //创建应用信息对象
            AppInfo appInfo = new AppInfo();

            //设置包名
            appInfo.setPkgName(packageName);
            //设置版本号
            appInfo.setVersionCode(versionCode);
            //设置版本名
            appInfo.setVersionName(versionName);

            return appInfo;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
