package com.example.sony.jizha.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.sony.jizha.R;
import com.example.sony.jizha.model.Contact;
import com.example.sony.jizha.system.JzApplication;
import com.example.sony.jizha.utils.ImageUtil;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

import java.util.List;

/**
 * 联系人列表适配器
 * Created by sony on 2015/9/7.
 */
public class ContactAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {

    public static final String TAG = "ContactAdapter";
    //上下文环境
    private Context mContext;
    //联系人列表
    private List<Contact> mContacts;
    //添加布局
    private LayoutInflater mInflater;

    /**
     * 构造函数
     *
     * @param context  上下文环境application
     * @param contacts 联系人列表
     */
    public ContactAdapter(Context context, List<Contact> contacts) {
        this.mContext = context;
        this.mContacts = contacts;

        Log.d(TAG, "------------------>Contacts" + contacts.size());
        mInflater = LayoutInflater.from(context);
    }

    //联系人数量
    @Override
    public int getCount() {
        return mContacts == null ? 0 : mContacts.size();
    }

    //根据位置获取联系人
    @Override
    public Contact getItem(int position) {
        return mContacts.get(position);
    }

    //根据位置获取listView的item
    @Override
    public long getItemId(int position) {
        return getItem(position).getId() == null ? position : getItem(position).getId();
    }

    //1.ListView先请求一个type1视图(getView)，然后请求其他可见的项目。conVertView在getView中时null的
    //2.当item1滚出屏幕，并且一个新的项目从屏幕地段上来时，ListView再请求一个type1视图。convertView此时不是空值了，它的值是item1.
    //3.你只需要设定新的数据返回convertView,不必重新创建一个视图。这样直接使用convertView从而减少了很不不必要view的创建
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            //添加布局
            convertView = mInflater.inflate(R.layout.template_contact, parent, false);
            holder.imageViewHead = (ImageView) convertView.findViewById(R.id.imageviewHead);
            holder.textName = (TextView) convertView.findViewById(R.id.txtName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Log.d(TAG, "--------------------->getItem(position)" + getItem(position).toString());

        holder.textName.setText(getItem(position).getName());
        //holder.imageViewHead.setImageResource(R.drawable.default_head);
        //设置头像的显示
        ImageUtil.displayImageUseDefOptions(getItem(position).getHeadbig(), holder.imageViewHead);

        return convertView;
    }

    //设置侧边栏的view
    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.template_text, parent, false);
            holder.textView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        //设置姓名首字母
        CharSequence pinyin = getItem(position).getPinyin().subSequence(0, 1);
        Log.d(TAG, "------------>Contact.Pinyin" + pinyin);
        holder.textView.setText(pinyin);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return getItem(position).getPinyin().subSequence(0, 1).charAt(0);
    }


    @Override
    public Object[] getSections() {
        return null;
    }

    //根据侧边栏获取位置
    @Override
    public int getPositionForSection(int section) {
        if (section == '#') {
            return 0;
        }
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mContacts.get(i).getPinyin();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    //根据位置获取侧边栏
    @Override
    public int getSectionForPosition(int position) {
        return getItem(position).getPinyin().subSequence(0, 1).charAt(0);
    }

    /**
     * 清除数据
     */
    public void clear() {
        mContacts.clear();
        notifyDataSetChanged();
    }

    /**
     * 批量添加数据
     */
    public void addData(List<Contact> contacts) {

        if (contacts != null) {
            mContacts.addAll(contacts);
        }
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     *
     * @param contact
     */
    public void addData(Contact contact) {

        if (contact != null) {
            mContacts.add(contact);
        }
        notifyDataSetChanged();
    }

    /**
     * 联系人列表
     */
    class ViewHolder {
        //联系人姓名
        TextView textName;
        //头像
        ImageView imageViewHead;
    }

    /**
     * 侧边栏
     */
    class HeaderViewHolder {
        //字母显示
        TextView textView;
    }
}
