package com.example.sony.jizha.robot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.sony.jizha.R;
import com.example.sony.jizha.Widget.BaseActivity;
import com.example.sony.jizha.activity.HomeActivity;
import com.example.sony.jizha.system.JzApplication;

public class RobotChatActivity extends BaseActivity implements OnClickListener {
	private Button sendButton;
	private EditText message;
	private ListView listView;
	private List<dataTransfer> data = new ArrayList<dataTransfer>();
	private ListViewAdapter dataAdapter;//数据适配器

	private String userStr;
	private String robotStr;

	private RobotMessage rM;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_robotchat);

		initActionBarBasic();
		initView();

		dataAdapter = new ListViewAdapter(this, data);
		listView.setAdapter(dataAdapter);
		listView.setSelection(listView.getCount() - 1);

		//更新机器人数据
		updateRobotData();
	}

	static Handler dataHandler;

	public void updateRobotData()
	{
		dataHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				int what = msg.what;
				if (what == 3) {
					if(robotStr.length() > 0)
					{
						dataTransfer data0 = new dataTransfer();
						data0.setWord(robotStr);
						data0.setName("机器人Andy");
						data0.setRobotUser(0);
						data.add(data0);

						dataAdapter.notifyDataSetChanged();

						listView.setSelection(listView.getCount() - 1);
					}
				}
				super.handleMessage(msg);
			}

		};
	}

	public void initView()
	{
		sendButton = (Button) findViewById(R.id.btnSend);
		sendButton.setOnClickListener(this);
		message = (EditText) findViewById(R.id.etxtMsg);
		listView = (ListView) findViewById(R.id.listView);
		dataTransfer dataTransfer = new dataTransfer();
		dataTransfer.setWord("您好，我是机器人Andy.比如回复\"你会什么\"");
		data.add(dataTransfer);
	}


	@Override
	public void onClick(View arg0) {

		switch(arg0.getId())
		{
			case R.id.btnSend:
				try {
					send();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
		}
	}

	public void robotSend() throws IOException
	{
		rM = new RobotMessage();
		rM.main(userStr);
		robotStr = rM.getString();

		Message msg1 = new Message();
		msg1 = Message.obtain(RobotChatActivity.dataHandler, 3);
		RobotChatActivity.dataHandler.sendMessage(msg1);
	}

	public void send() throws IOException
	{
		userStr = message.getText().toString();
		if(userStr.length() > 0)
		{
			dataTransfer data0 = new dataTransfer();
			data0.setWord(userStr);
			data0.setName(JzApplication.getInstance().getmLoginMember().getName());
			data0.setRobotUser(1);
			data.add(data0);

			dataAdapter.notifyDataSetChanged();
			message.setText("");

			listView.setSelection(listView.getCount() - 1);
		}
		new Thread() {
			@Override
			public void run() {
				try {
					robotSend();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * 触发actionBar的返回按钮
	 *
	 * @param item
	 * @return
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			//触发返回事件
			//onBackPressed();

			//统一返回按钮以及actionbar上返回按钮的返回到homeActivity的聊天信息显示列表界面
			navigateUpTo();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 统一返回按钮以及actionbar上返回按钮的返回到homeActivity
	 */
	private void navigateUpTo() {

		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
	}
}
