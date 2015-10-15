package com.example.jpushdemo;

import android.net.Uri;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

/**
 * 推送通知点击之后,显示的详情页面
 */
public class TestActivity extends Activity {

    private static final String TAG = "Detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("用户自定义打开的Activity");

        // Intent 是由MyReceiver 收到通知点击的广播时，触发的。
        Intent intent = getIntent();
        if (null != intent) {

            // 获取 从服务器传递的推送消息；
            Bundle bundle = getIntent().getExtras();
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);

            // 获取推送消息的内容
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);

            // 遍历所有的参数
            Set<String> keySet = bundle.keySet();

            for (String key : keySet) {
                Log.d(TAG, key + "=" + bundle.get(key));
            }

            // 获取附加字段 JSON 格式
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);

            if (extra != null) {
                // 解析 JSON
                try {
                    JSONObject json = new JSONObject(extra);

                    String url = json.getString("url");

                    Intent intent1 = new Intent(Intent.ACTION_VIEW);
                    intent1.setData(Uri.parse(url));
                    startActivity(intent1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            tv.setText("Title : " + title + "  " + "Content : " + content);
        }
        addContentView(tv, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

}
