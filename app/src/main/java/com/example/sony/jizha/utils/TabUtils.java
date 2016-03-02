package com.example.sony.jizha.utils;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.sony.jizha.R;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：设置聊天数量在Tab上的显示
 * 创建人：sony
 * 创建时间：2015/12/30 15:42
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class TabUtils {
    public static View renderTabView(Context context, int titleResource, int badgeNumber) {
        //将tab_badge.xml文件读取进来
        FrameLayout view = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.tab_badge, null);
        // We need to manually set the LayoutParams here because we don't have a view root
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //设置View的Text
        ((TextView) view.findViewById(R.id.tabText)).setText(titleResource);
        //更新聊天显示的数量
        updateTabBadge((TextView) view.findViewById(R.id.tabBadge), badgeNumber);
        return view;
    }

    /*
    * 更新聊天显示的数量
    * */
    public static void updateTabBadge(ActionBar.Tab tab, int badgeNumber) {
        updateTabBadge((TextView) tab.getCustomView().findViewById(R.id.tabBadge), badgeNumber);
    }


    private static void updateTabBadge(TextView view, int badgeNumber) {
        //信息数量大于0，View显示，否则不显示
        if (badgeNumber > 0) {
            view.setVisibility(View.VISIBLE);
            //设置View显示的信息数量
            view.setText(Integer.toString(badgeNumber));
        } else {
            view.setVisibility(View.GONE);
        }
    }
}
