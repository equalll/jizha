package com.example.sony.jizha.Widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.example.sony.jizha.R;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：自定义好友请求对话框
 * 创建人：sony
 * 创建时间：2015/1/6 15:40
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class CustomDialog extends Dialog {
    //上下文
    private Context context;

    private View layoutView;

    public CustomDialog(Context context,View view) {
        super(context, R.style.dialog);
        this.context = context;
        this.layoutView = view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(layoutView);
    }
}
