package com.example.sony.jizha.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sony.jizha.R;
import com.example.sony.jizha.model.ChatMsgEx;
import com.example.sony.jizha.utils.ImageUtil;
import com.example.sony.jizha.utils.PrettyDateFormat;

import java.util.List;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：聊天信息列表显示适配器
 * 创建人：sony
 * 创建时间：2016/1/5 17:27
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class ChatListAdapter1 extends AbstractAdapter<ChatMsgEx> {

    PrettyDateFormat dateFormat = new PrettyDateFormat("@", "yyyy-MM-dd HH:mm:dd");

    public ChatListAdapter1(Context context, List<ChatMsgEx> datas) {
        super(context, datas);
    }


    @Override
    public IViewHolder<ChatMsgEx> getViewHolder(int viewType) {
        return new ViewHolder();
    }

    @Override
    int getViewLayout(int viewType) {
        return R.layout.template_chat_list;
    }

    class ViewHolder implements IViewHolder<ChatMsgEx> {

        private TextView txtName;
        private TextView txtTime;
        private TextView txtMsg;
        private TextView txtBadge;
        private ImageView imageviewHead;

        @Override
        public void bindData(ChatMsgEx data, int position) {

            txtTime.setText(dateFormat.format(data.getChattime()));
            txtMsg.setText(data.getChatMsg());

            Log.d(TAG,"---------------->date:" + data
            .toString());
            txtName.setText(data.getContact().getName());

            if (data.getUnreadCount() <= 0)
                txtBadge.setVisibility(View.GONE);
            else {
                txtBadge.setVisibility(View.VISIBLE);
                if (data.getUnreadCount() > 99)
                    txtBadge.setText("99+");
                else
                    txtBadge.setText(data.getUnreadCount().toString());
            }

            ImageUtil.displayImageUseDefOptions(data.getContact().getHeadbig(), imageviewHead);
        }
    }
}
