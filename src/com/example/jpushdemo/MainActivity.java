package com.example.jpushdemo;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import com.liuchaoamo.whitebig.R;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 主界面，实现了 推送通知的启动；
 */
public class MainActivity extends InstrumentedActivity implements OnClickListener, TagAliasCallback {

    private Button mInit;
    private Button mSetting;
    private Button mStopPush;
    private Button mResumePush;
    private EditText msgText;

    public static boolean isForeground = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        registerMessageReceiver();  // used for receive msg
    }

    private void initView() {
        TextView mImei = (TextView) findViewById(R.id.tv_imei);
        String udid = ExampleUtil.getImei(getApplicationContext(), "");
        if (null != udid) mImei.setText("IMEI: " + udid);

        TextView mAppKey = (TextView) findViewById(R.id.tv_appkey);
        String appKey = ExampleUtil.getAppKey(getApplicationContext());
        if (null == appKey) appKey = "AppKey异常";
        mAppKey.setText("AppKey: " + appKey);

        String packageName = getPackageName();
        TextView mPackage = (TextView) findViewById(R.id.tv_package);
        mPackage.setText("PackageName: " + packageName);

        String versionName = ExampleUtil.GetVersion(getApplicationContext());
        TextView mVersion = (TextView) findViewById(R.id.tv_version);
        mVersion.setText("Version: " + versionName);

        mInit = (Button) findViewById(R.id.init);
        mInit.setOnClickListener(this);

        mStopPush = (Button) findViewById(R.id.stopPush);
        mStopPush.setOnClickListener(this);

        mResumePush = (Button) findViewById(R.id.resumePush);
        mResumePush.setOnClickListener(this);

        mSetting = (Button) findViewById(R.id.setting);
        mSetting.setOnClickListener(this);

        msgText = (EditText) findViewById(R.id.msg_rec);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.init:
                init();
                break;
            case R.id.setting:
                Intent intent = new Intent(MainActivity.this, PushSetActivity.class);
                startActivity(intent);
                break;
            case R.id.stopPush:
                JPushInterface.stopPush(getApplicationContext());
                break;
            case R.id.resumePush:
                JPushInterface.resumePush(getApplicationContext());
                break;
        }
    }

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void init() {
        JPushInterface.init(getApplicationContext());
    }


    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();

        // TODO 设置一下 Tag，给当前设备设置标签

        Set<String> tags = new LinkedHashSet<String>();

        // 设置Tag，可以设置多个
        tags.add("90后");
        tags.add("程序员");
        tags.add("挨踢男");
        tags.add("攻城狮");

        // 参数1 上下文
        // 参数2 Tag的set集合
        // 参数3 接口回调,回调是否设置成功
        JPushInterface.setTags(this, tags, this);

        JPushInterface.setAlias(this, "Android", this);
    }

    /**
     * @param i   设置是否成功的标识
     * @param s   这个参数在 设置设备别名的时候，有效
     * @param set 这个参数在设置设备标签的时候，有效
     */
    @Override
    public void gotResult(int i, String s, Set<String> set) {
        Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }


    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }


    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                setCostomMsg(showMsg.toString());
            }
        }
    }

    private void setCostomMsg(String msg) {
        if (null != msgText) {
            msgText.setText(msg);
            msgText.setVisibility(View.VISIBLE);
        }
    }

}