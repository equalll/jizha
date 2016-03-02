package com.example.sony.jizha.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.sony.jizha.Widget.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：
 * 创建人：sony
 * 创建时间：2016/1/5 16:30
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public abstract class CommonAdapter<T extends BaseModel> extends BaseAdapter {
    //添加布局对象
    private LayoutInflater inflater;
    //布局id
    private int layoutId;
    //listView数据
    private List<T> mlist = new ArrayList<T>();

    /**
     * 构造函数
     *
     * @param context
     * @param layoutId
     * @param list
     */
    public CommonAdapter(Context context, int layoutId, List<T> list) {
        super();
        //初始化数据
        this.inflater = LayoutInflater.from(context);
        this.layoutId = layoutId;
        this.mlist = list;
    }

    /**
     * 往顶部添加数据
     *
     * @param list
     */
    public void add2Head(List<T> list) {
        mlist.addAll(0, list);
        notifyDataSetChanged();
    }

    /**
     * 清除所有数据
     */
    public void clearAll() {
        mlist.clear();
        notifyDataSetChanged();
    }

    /**
     * 获取所有的数据
     *
     * @return
     */
    public List<T> getAllList() {
        return mlist;
    }

    /**
     * 往底部批量添加数据
     *
     * @param list
     */
    public void add2Bottom(List<T> list) {
        mlist.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 往底部添加一条对象数据
     *
     * @param t
     */
    public void add2Bottom(T t) {
        mlist.add(t);
        notifyDataSetChanged();
    }


    /**
     * @param list 设定文件
     * @return void 返回类型
     * @throws
     * @Title: updateListView
     * @Description: TODO(更新BaseAdapter中的数据)
     */
    public void updateListView(List<T> list) {
        mlist = list;
        notifyDataSetChanged();
    }

    /**
     * 获取listView数据数量
     *
     * @return
     */
    @Override
    public int getCount() {
        return mlist.size();
    }

    /**
     * 根据位置获取具体的对象
     *
     * @param position
     * @return
     */
    @Override
    public T getItem(int position) {
        return mlist.get(position);
    }

    /**
     * 根据位置获取具体对象的id
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    /**
     * 实际显示View的方法，使用抽象方法强制调用者覆写！
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = ViewHolder.getViewHolder(parent, convertView,
                inflater, layoutId);
        convert(viewHolder, mlist.get(position));
        return viewHolder.getConvertView();

    }

    /**
     * convert的抽象
     *
     * @param viewHolder
     * @param t
     */
    public abstract void convert(ViewHolder viewHolder, T t);

}
