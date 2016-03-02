package com.example.sony.jizha.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.sony.jizha.Widget.BaseModel;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：抽象的adapter,adapter封装
 * 创建人：sony
 * 创建时间：2016/1/4 18:57
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public abstract class AbstractAdapter<T extends BaseModel> extends BaseAdapter {
    //初始化上下文环境
    protected Context context;

    protected String TAG;

    //添加布局对象
    @Inject
    protected LayoutInflater mInflater;

    //listView数据
    protected List<T> datas;

    /**
     * 构造函数
     *
     * @param context 上下文
     * @param datas   适配器数据
     */
    public AbstractAdapter(Context context, List<T> datas) {
        this.context = context;
        //实例化TAG,获取实际的类名
        TAG = this.getClass().getName();
        if (datas == null) {
            //listView数据为空时
            //初始化listView数据
            this.datas = new ArrayList<T>();
        } else {
            this.datas = datas;
        }
        //初始化LayoutInflater对象
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 获取listView数据的数目
     *
     * @return
     */
    @Override
    public int getCount() {
        return datas.size();
    }

    /**
     * 根据位置获取具体的对象
     *
     * @param position listView中的数据
     * @return 具体的对象
     */
    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    /**
     * 通过位置获取对象的id
     *
     * @param position listView中的位置
     * @return 对象的id
     */
    @Override
    public long getItemId(int position) {
        // 添加一个null 判断
        Long id = getItem(position).getId();
        //存在该id时返回id,否则返回0
        return id == null ? 0 : id;
    }

    /**
     * 获取视图
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //getItemViewType返回的是有参数position所决定的的view的id，然后根据视图id找出具体的view
        int viewType = getItemViewType(position);

        //viewHolder接口
        IViewHolder<T> holder;
        if (convertView == null) {
            //视图id找出具体的view
            holder = getViewHolder(viewType);
            //添加布局文件
            convertView = mInflater.inflate(getViewLayout(viewType), parent, false);

            ViewInjector.injectView(holder, convertView);

            convertView.setTag(holder);
        } else {
            holder = (IViewHolder<T>) convertView.getTag();
        }
        //绑定数据
        holder.bindData(getItem(position), position);
        return convertView;
    }

    /**
     * 通过viewType返回viewHolder
     *
     * @param viewType 视图类型
     * @return
     */
    abstract IViewHolder<T> getViewHolder(int viewType);

    /**
     * 通过viewType获取布局
     *
     * @param viewType 视图类型
     * @return
     */
    abstract int getViewLayout(int viewType);

    /**
     * 添加多个数据
     *
     * @param arrayData
     */
    public void addData(List<T> arrayData) {

        if (arrayData != null) {
            datas.addAll(arrayData);
        }
        notifyDataSetChanged();
    }

    /**
     * 添加一个数据
     *
     * @param data
     */
    public void addData(T data) {

        if (data != null) {
            datas.add(data);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除数据
     */
    public void clear() {
        datas.clear();
        notifyDataSetChanged();
    }
}
