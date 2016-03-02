package com.example.sony.jizha.adapter;

import android.content.Context;
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
 * 类描述：聊天信息列表显示
 * 创建人：sony
 * 创建时间：2016/1/5 16:48
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class ChatListAdapter extends CommonAdapter<ChatMsgEx> {

    PrettyDateFormat dateFormat = new PrettyDateFormat("@", "yyyy-MM-dd HH:mm:dd");

    public ChatListAdapter(Context context, int layoutId, List<ChatMsgEx> list) {
        super(context, layoutId, list);
    }

    @Override
    public void convert(ViewHolder viewHolder, ChatMsgEx t) {

        TextView txtName = viewHolder.getTextView(R.id.txtName);
        TextView txtTime = viewHolder.getTextView(R.id.txtTime);
        TextView txtMsg = viewHolder.getTextView(R.id.txtMsg);
        TextView txtBadge = viewHolder.getTextView(R.id.txtBadge);
        ImageView imageviewHead = viewHolder.getImageView(R.id.imageviewHead);

        txtName.setText(t.getContact().getName());
        txtTime.setText(dateFormat.format(t.getChattime()));
        txtMsg.setText(t.getChatMsg());

        if (t.getUnreadCount() <= 0) {
            txtBadge.setVisibility(View.GONE);
        } else {
            txtBadge.setVisibility(View.VISIBLE);
            if (t.getUnreadCount() > 99) {
                txtBadge.setText("99+");
            } else {
                txtBadge.setText(t.getUnreadCount().toString());
            }
        }
        ImageUtil.displayImageUseDefOptions(t.getContact().getHeadbig(), imageviewHead);
    }
}
