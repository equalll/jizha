package com.example.sony.jizha.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：ViewHolder的公共类
 * 创建人：sony
 * 创建时间：2016/1/5 16:31
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class ViewHolder {

    //SparseArray是android里为<Interger,Object>这样的Hashmap而专门写的class,目的是提高效率，其核心是折半查找函数（binarySearch）
    //存储的数值都是按键值从小到大的顺序排列好的
    private SparseArray<View> views;
    //视图对象
    private View convertView;

    /**
     * 构造函数
     *
     * @param parent
     * @param inflater
     * @param layoutId
     */
    public ViewHolder(ViewGroup parent, LayoutInflater inflater, int layoutId) {
        //初始化view对象
        this.views = new SparseArray<View>();
        //添加布局
        this.convertView = inflater.inflate(layoutId, parent, false);
        //Tag的作用就是设置标签，利用缓存convertView尽可能少实例化同样结构体的对象；
        this.convertView.setTag(this);
    }

    /**
     * 得到viewHolder
     *
     * @param parent
     * @param convertView
     * @param inflater
     * @param layoutId
     * @return
     */
    public static ViewHolder getViewHolder(ViewGroup parent, View convertView, LayoutInflater inflater, int layoutId) {
        if (convertView == null) {
            return new ViewHolder(parent, inflater, layoutId);
        }
        return (ViewHolder) convertView.getTag();
    }

    /**
     * 得到convertView
     *
     * @return
     */
    public View getConvertView() {
        return convertView;
    }

    /**
     * 根据Id得到view
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);

        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 根据id得到TextView
     *
     * @return
     */
    public TextView getTextView(int viewId) {
        return getView(viewId);
    }

    /**
     * 根据id得到ImageView
     *
     * @return
     */
    public ImageView getImageView(int viewId) {
        return getView(viewId);
    }

    /**
     * 根据id得到Button
     *
     * @return
     */
    public Button getButton(int viewId) {
        return getView(viewId);
    }

    /**
     * 根据id得到RadioButton
     *
     * @return
     */
    public RadioButton getRadioButton(int viewId) {
        return getView(viewId);
    }

    /**
     * 根据id得到CheckBox
     *
     * @return
     */
    public CheckBox getCheckBox(int viewId) {
        return getView(viewId);
    }

    /**
     * 根据id得到ImageButton
     *
     * @return
     */
    public ImageButton getImageButton(int viewId) {
        return getView(viewId);
    }

    /**
     * 根据id得到ImageButton
     *
     * @return
     */
    public EditText getEditText(int viewId) {
        return getView(viewId);
    }

    /**
     * 根据id得到RelativeLayout
     *
     * @return
     */
    public RelativeLayout getRelativeLayout(int viewId) {
        return getView(viewId);
    }
}
