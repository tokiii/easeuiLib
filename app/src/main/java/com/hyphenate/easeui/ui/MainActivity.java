package com.hyphenate.easeui.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.R;

public class MainActivity extends FragmentActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    private Context mContext;

    private final int LOGIN_SUCCESS = 1;//登录成功

    private final int LOGIN_ERROR = 2;//登录异常

    private final int NET_ERROR = 3;//网络异常

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOGIN_SUCCESS:
                    Log.d(TAG, "登录聊天服务器成功！");
                    Toast.makeText(mContext, "连接成功", Toast.LENGTH_SHORT).show();
                    break;
                case LOGIN_ERROR:
                    Log.d(TAG, "登录聊天服务器失败！");
                    Toast.makeText(mContext, "连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case NET_ERROR:
                    Log.d(TAG, "连接失败！");
                    Toast.makeText(mContext, "连接失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    /**
     * 登录成功--加载聊天界面
     */
    private void loadChatView() {

        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();


        //new出EaseChatFragment或其子类的实例
        EaseChatFragment chatFragment = new EaseChatFragment();
        //传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, "12345");
        chatFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        StateBarInitUtils.initImmersionTitleBar(this);
        mContext = this;
        initEaseUI();
        initView();

        initData();

    }

    private void initData() {
        loginHuanxin();
    }

    /**
     * 登录环信
     */
    private synchronized void loginHuanxin() {

        String hxUserLoginId = "12345678";//环信登录ID
        String passwordStr = "12345678";

        /**
         * 联网请求登录环信
         */
            if (hxUserLoginId != null && passwordStr != null) {
                requestloginHuanxinServer(hxUserLoginId, passwordStr);//登录环信
            } else {
                mHandler.sendEmptyMessage(LOGIN_ERROR);
            }
    }

    /**
     * 联网请求登录环信账户
     *
     * @param phone
     * @param upwd
     */
    private void requestloginHuanxinServer(final String phone, final String upwd) {


        EMClient.getInstance().login(phone, upwd, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                mHandler.sendEmptyMessage(LOGIN_SUCCESS);
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                mHandler.sendEmptyMessage(LOGIN_ERROR);
            }
        });
    }

    protected void initView() {
        loadChatView();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 初始化环信
     */
    private void initEaseUI() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        options.setAppKey("ctrchina#crowdphoto");
        EaseUI.getInstance().init(this, options);
    }
}
