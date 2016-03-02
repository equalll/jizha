package com.example.sony.jizha.model;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：客户端应用的信息实体
 * 创建人：sony
 * 创建时间：2016/1/1 15:59
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class AppInfo {

    //启动应用程序的Intent ，一般是Action为Main和Category为Lancher的Activity
    //应用程序所对应的包名
    private String pkgName;
    //应用程序所对应的版本号
    private int versionCode;
    //应用程序所对应的版本名
    private String versionName;

    public AppInfo() {
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
