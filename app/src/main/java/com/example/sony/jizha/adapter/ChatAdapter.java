package com.example.sony.jizha.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sony.jizha.R;
import com.example.sony.jizha.http.BaseResponse;
import com.example.sony.jizha.http.RequestListener;
import com.example.sony.jizha.http.VolleyHttpClient;
import com.example.sony.jizha.model.ChatMsg;
import com.example.sony.jizha.model.Contact;
import com.example.sony.jizha.model.Member;
import com.example.sony.jizha.service.ChatMsgService;
import com.example.sony.jizha.system.Constant;
import com.example.sony.jizha.system.JzApplication;
import com.example.sony.jizha.utils.ImageUtil;
import com.example.sony.jizha.utils.PrettyDateFormat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：聊天信息的适配器
 * 创建人：sony
 * 创建时间：2016/1/1 13:17
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class ChatAdapter extends BaseAdapter {

    public static final String TAG = "ContactAdapter";

    //初始化时间格式化工具
    private PrettyDateFormat dateFormat = new PrettyDateFormat("@", "yy-MM-dd HH:mm");

    //上下文环境
    private Context mContext;
    //聊天消息列表
    private List<ChatMsg> mChatMsgs;
    //添加布局
    private LayoutInflater mInflater;

    //当前用户，用于获取设置头像
    private Member mMember;
    //聊天的好友
    private Contact mFriend;

    //网络请求
    private VolleyHttpClient mHttpClient;

    //聊天信息的服务
    private ChatMsgService mChatMsgService;

    /**
     * 构造函数
     */
    public ChatAdapter(Context context, List<ChatMsg> chatMsgs, Contact friend) {
        this.mContext = context;
        this.mChatMsgs = chatMsgs;

        this.mInflater = LayoutInflater.from(context);
        //初始化当前用户
        this.mMember = JzApplication.getInstance().getmLoginMember();
        //初始化聊天的好友
        this.mFriend = friend;
        //初始化网络请求:
        mHttpClient = VolleyHttpClient.getInstance(context);
        //初始化聊天信息服务
        mChatMsgService = ChatMsgService.getInstance();
    }

    //返回数据的数量
    @Override
    public int getCount() {
        return mChatMsgs == null ? 0 : mChatMsgs.size();
    }

    //根据位置返回具体的对象
    @Override
    public ChatMsg getItem(int position) {
        return mChatMsgs.get(position);
    }

    //根据位置返回对象的id
    @Override
    public long getItemId(int position) {
        return getItem(position).getId() == null ? position : getItem(position).getId();
    }

    //不允许item的点击
    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    //listview中提供了getItemViewType和getViewTypeCount来实现同一个lsitview多种item布局风格
    //getItemViewType返回的是有参数position所决定的的view的id
    @Override
    public int getItemViewType(int position) {
        //是否发送，如果是布局显示发送的布局，否则显示的是接收的布局
        return getItem(position).getIsreceive();
    }

    //getViewTypeCount，就是返回不同布局的数目,有发送和接收两种模板
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取要显示的布局
        int viewType = getItemViewType(position);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            //判断聊天的布局
            convertView = mInflater.inflate(viewType == ChatMsg.RECEVIER ? R.layout.template_chat_receive : R.layout.template_chat_send, parent, false);
            holder.txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
            holder.imageviewHead = (ImageView) convertView.findViewById(R.id.imageviewHead);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //根据位置获取对象
        ChatMsg chatMsg = getItem(position);

        //设置聊天时间
        holder.txtTime.setText(dateFormat.format(chatMsg.getChattime()));
        //设置聊天信息
        holder.txtMsg.setText(chatMsg.getChatMsg());

        //设置头像显示
        if (viewType == ChatMsg.SEND) {
            //显示发送者的头像
            //Log.d(TAG, "----------------->mMember:" + mMember.toString());

            ImageUtil.displayImageUseDefOptions(mMember.getHeadbig(), holder.imageviewHead);
        } else {
            //显示好友的头像
            ImageUtil.displayImageUseDefOptions(mFriend.getHeadbig(), holder.imageviewHead);
        }

        //获取消息列表时，判断消息是否已经发送，如果没有发送消息，重新发送消息

        if (chatMsg.getIsreceive() == ChatMsg.SEND && chatMsg.getStatus() == ChatMsg.STATUS_NOSEND) {
            sendMsg(chatMsg, position);
        }

        return convertView;
    }

    /**
     * 将未发送的消息发送
     *
     * @param chatMsg  未发送的消息
     * @param position 消息位置
     */
    private void sendMsg(final ChatMsg chatMsg, final int position) {

        //传入的值
        Map<String, String> param = new HashMap<String, String>();

        //服务器端会通过会员id获取推送通道
        param.put("memberId", mMember.getId() + "");
        //通过friendId确定要推送的好友
        param.put("friendId", mFriend.getId() + "");
        //要推送到推送服务器的消息
        param.put("msg", chatMsg.getChatMsg());

        //token令牌
        //param.put("token", mMember.getToken());

        Log.d(TAG, "---------------->map:" + mMember.getId() + " : " + chatMsg.getChatMsg() + " : " + mFriend.getId());

        //向好友发送信息
        mHttpClient.post(Constant.API.URL_CHART_SEND, param, 0, new RequestListener() {
            @Override
            public void onPreRequest() {

                //设置聊天信息当前登录用户的id
                chatMsg.setMemberid(mMember.getId());

                Log.d(TAG, "----------------->chatMsg:" + chatMsg.toString());

                //保存聊天信息到手机端数据库
                long id = mChatMsgService.save(chatMsg);
                //设置聊天信息的id
                chatMsg.setId(id);
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {

                if (response.isSuccess()) {
                    //发送消息成功
                    //设置聊天信息的状态为发送成功，否则设置聊天消息的状态为发送失败
                    chatMsg.setStatus(ChatMsg.STATUS_SEND_SUCCESS);
                    Log.d(TAG, "---------------->msg send success");
                } else {
                    chatMsg.setStatus(ChatMsg.STATUS_SEND_FAIL);
                }

                //更新保存的聊天信息
                mChatMsgService.update(chatMsg);

                //移除位置
                mChatMsgs.remove(position);
                //添加一个位置显示消息
                mChatMsgs.add(position, chatMsg);
            }

            @Override
            public void onRequestError(int code, String errMsg) {
                //消息发送失败
                //设置消息的状态为发送失败
                chatMsg.setStatus(ChatMsg.STATUS_SEND_FAIL);
                //更新手机端数据库中消息的状态
                mChatMsgService.update(chatMsg);
            }

            @Override
            public void onRequestFail(int code, String errMsg) {
                //消息发送失败
                //设置消息的状态为发送失败
                chatMsg.setStatus(ChatMsg.STATUS_SEND_FAIL);
                //更新手机端数据库中消息的状态
                mChatMsgService.update(chatMsg);
            }
        });
    }

    /**
     * 往listView中添加聊天信息
     *
     * @param chatMsg
     */
    public void addData(ChatMsg chatMsg) {
        if (chatMsg != null) {
            mChatMsgs.add(chatMsg);
        }

        //刷新列表显示
        notifyDataSetChanged();
    }

    /**
     * 聊天信息列表
     */
    class ViewHolder {
        //聊天时间
        TextView txtTime;
        //聊天信息
        TextView txtMsg;
        //头像
        ImageView imageviewHead;
    }
}
