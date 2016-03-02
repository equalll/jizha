package com.example.sony.jizha.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.sony.jizha.R;
import com.example.sony.jizha.system.JzApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：头像加载工具类，使用universal-image-loader
 * 创建人：sony
 * 创建时间：2015/12/30 15:42
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class ImageUtil {

    /**
     * 图片显示
     *
     * @param url       图片的url
     * @param imageView 图片控件
     * @param options   图片显示设置
     */
    public static void displayImage(String url, ImageView imageView, DisplayImageOptions options) {
        JzApplication.getInstance().getmImgLoader().displayImage(url, imageView, options);
    }

    /**
     * 图片显示
     *
     * @param url       图片的url
     * @param imageView 图片控件
     */
    public static void displayImage(String url, ImageView imageView) {
        JzApplication.getInstance().getmImgLoader().displayImage(url, imageView);
    }

    /**
     * 图片显示
     *
     * @param url       图片的url
     * @param imageView 图片控件
     */
    public static void displayImageUseDefOptions(String url, ImageView imageView) {
        JzApplication.getInstance().getmImgLoader().displayImage(url, imageView, getDisplayOptions());
    }

    /**
     * 图片显示设置
     *
     * @return
     */
    public static DisplayImageOptions getDisplayOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_head) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_head)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_head)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.NONE)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                        //                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .displayer(new RoundedBitmapDisplayer(5))
                .build();//构建完成
        return options;
    }
}
