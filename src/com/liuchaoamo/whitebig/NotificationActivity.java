package com.liuchaoamo.whitebig;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import cn.jpush.android.api.JPushInterface;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created
 * Author: Amo
 * Email: liuchao1993_3@163.com
 * Date: 2015/10/13
 */
public class NotificationActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);

        // ����֪ͨ�� ����
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            // ��ȡ���ӵ� JSON ���ݣ�
            String string = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (string != null) {
                try {
                    JSONObject json = new JSONObject(string);
                    String type = json.getString("type");

                    View container = findViewById(R.id.notification_container);

                    if (type.equals("1")) {
                        container.setBackgroundColor(Color.WHITE);
                    } else {
                        container.setBackgroundColor(Color.CYAN);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}