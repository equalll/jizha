package com.example.sony.jizha.adapter;

import android.view.View;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：viewHolder接口
 * 创建人：sony
 * 创建时间：2016/1/4 18:57
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public interface IViewHolder<T> {

    /**
     * 绑定数据
     *
     * @param t
     * @param position
     */
    public void bindData(T t, int position);
}
