package com.example.sony.jizha.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sony.jizha.R;
import com.example.sony.jizha.activity.FriendSearchResultActivity;
import com.example.sony.jizha.model.Contact;
import com.example.sony.jizha.utils.ImageUtil;
import com.example.sony.jizha.utils.ToastUtils;

import java.util.List;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：好友搜索结果适配器
 * 创建人：sony
 * 创建时间：2015/12/30 15:42
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class FriendAdapter extends AbstractAdapter<Contact> {

    /**
     * 构造函数
     *
     * @param context 上下文
     * @param datas   数据
     */
    public FriendAdapter(Context context, List<Contact> datas) {
        super(context, datas);
    }

    /**
     * 获取viewHolder
     *
     * @param viewType 视图类型
     * @return
     */
    @Override
    IViewHolder<Contact> getViewHolder(int viewType) {
        return new ViewHolder();
    }

    /**
     * 获取布局
     *
     * @param viewType 视图类型
     * @return
     */
    @Override
    int getViewLayout(int viewType) {
        return R.layout.template_friend;
    }

    class ViewHolder implements IViewHolder<Contact> {
        //头像
        private ImageView imageviewHead;
        //姓名
        private TextView txtName;

        public void bindData(Contact data, int position) {

            if (data != null) {
                txtName.setText(data.getName());
                ImageUtil.displayImageUseDefOptions(data.getHeadbig(), imageviewHead);
            }else{
                ToastUtils.show(context, R.string.empty_result);
            }
        }
    }
}
