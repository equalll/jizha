package com.example.sony.jizha.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sony.jizha.R;
import com.example.sony.jizha.http.BaseResponse;
import com.example.sony.jizha.http.RequestListener;
import com.example.sony.jizha.http.VolleyHttpClient;
import com.example.sony.jizha.model.Contact;
import com.example.sony.jizha.model.RequestAnswerMsg;
import com.example.sony.jizha.model.RequestMsg;
import com.example.sony.jizha.service.ContactService;
import com.example.sony.jizha.service.RequestMsgService;
import com.example.sony.jizha.system.Constant;
import com.example.sony.jizha.utils.ImageUtil;
import com.example.sony.jizha.utils.Pinyin4j;
import com.example.sony.jizha.utils.PreferencesUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：添加好友请求信息适配器
 * 创建人：sony
 * 创建时间：2015/12/30 15:42
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class RequestMsgAdapter extends AbstractAdapter<RequestMsg> {

    //网络请求对象
    private VolleyHttpClient mHttpClient;

    private RequestMsgService mRequesgMsgService;
    //联系人服务对象
    private ContactService mContactService;

    /**
     * 构造函数
     * @param context
     * @param datas
     */
    public RequestMsgAdapter(Context context, List<RequestMsg> datas) {
        super(context, datas);
        //初始化网络请求对象
        mHttpClient = new VolleyHttpClient(context);
        //初始化好友添加请求服务
        mRequesgMsgService = RequestMsgService.getInstance();
        //初始化联系人服务
        mContactService = ContactService.getInstance();
    }

    @Override
    IViewHolder<RequestMsg> getViewHolder(int viewType) {
        return new ViewHolder();
    }

    @Override
    int getViewLayout(int viewType) {
        return R.layout.template_request_msg;
    }


    class ViewHolder implements IViewHolder<RequestMsg> {

        //姓名
        private TextView txtName;
        //消息处理文本，显示处理的结果
        private TextView txtHandle;
        //添加好友请求信息
        private TextView txtMsg;
        //处理按钮
        private Button btnHandle;
        //头像
        private ImageView imageviewHead;

        @Override
        public void bindData(final RequestMsg data, final int position) {

            txtName.setText(data.getContactname());
            String requestMsg = TextUtils.isEmpty(data.getRequestmsg()) ? context.getString(R.string.friend_ask_you_be) : data.getRequestmsg();
            txtMsg.setText(requestMsg);

            if (data.getStatus() == RequestMsg.STATUS_UN_HANDLE) {
                btnHandle.setVisibility(View.VISIBLE);
                txtHandle.setVisibility(View.GONE);

                // 绑定点击事件
                btnHandle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //处理添加好友请求
                        acceptRequest(data, position);
                    }
                });
            } else {
                btnHandle.setVisibility(View.GONE);
                txtHandle.setVisibility(View.VISIBLE);
                if (data.getStatus() == RequestMsg.STATUS_AGREE)
                    txtHandle.setText(R.string.accepted);
                else if (data.getStatus() == RequestMsg.STATUS_REFUSE)
                    txtHandle.setText(R.string.refused);
            }

            ImageUtil.displayImageUseDefOptions(data.getContactheadmid(), imageviewHead);
        }


        /**
         * 处理添加好友请求
         * @param msg 好友添加请求的信息
         * @param position
         */
        private void acceptRequest(final RequestMsg msg, final int position) {

            //网络请求需要值
            Map<String, String> params = new HashMap<String, String>();
            params.put("requestid", msg.getRequestid() + "");
            params.put("answer", RequestAnswerMsg.TYPE_AGREE + "");
            params.put("token", PreferencesUtils.getString(context, Constant.ACCESS_TOKEN));

            //向服务端请求
            mHttpClient.post(Constant.API.URL_FRIENDS_REQUEST_ANSWER,
                    params,
                    R.string.sending,
                    new RequestListener() {
                        @Override
                        public void onPreRequest() {

                        }

                        @Override
                        public void onRequestSuccess(BaseResponse response) {

                            if (response.isSuccess()) {

                                msg.setStatus(RequestMsg.STATUS_AGREE);
                                // 更新消息内容
                                mRequesgMsgService.update(msg);

                                // 保存好友到通讯录
                                saveFriend(msg);

                                //移除好友添加消息
                                datas.remove(msg);
                                datas.add(position, msg);

                                //listViewg改变
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onRequestError(int code, String msg) {

                        }

                        @Override
                        public void onRequestFail(int code, String msg) {

                        }
                    });
        }

        /**
         * 保存好友到通讯录
         * @param msg
         */

        private void saveFriend(RequestMsg msg) {

            Contact friend = new Contact();

            friend.setMemberid(msg.getMemberid());
            friend.setContactid(msg.getContactid());

            // 把名字转换成拼音
            friend.setPinyin(Pinyin4j.getPingYin(msg.getContactname(), true).toUpperCase());

            friend.setCreatetime(new Date());
            friend.setEmail(msg.getContactemail());
            friend.setName(msg.getContactname());
            friend.setHeadbig(msg.getContactheadbig());
            friend.setHeadmid(msg.getContactheadmid());
            friend.setHeadsmall(msg.getContactheadsmall());

            mContactService.save(friend);
        }
    }
}
