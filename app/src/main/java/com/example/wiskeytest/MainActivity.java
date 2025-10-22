package com.example.wiskeytest;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final String TAG = "PendingIntentTest";
    // 声明 PendingIntent 引用，用于后续取消
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 创建一个简单的 Intent（目标可以是任意组件，这里用本 Activity 举例）
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction("com.example.pendingintent.TEST_ACTION");

        // 1. 创建带 FLAG_ONE_SHOT 的 PendingIntent 并发送
        Button btnOneShot = findViewById(R.id.btn_one_shot);
        btnOneShot.setOnClickListener(v -> {
            Log.i(TAG, "click FLAG_ONE_SHOT button");
            if (pendingIntent != null) {
                int flags = PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE;
                pendingIntent = PendingIntent.getActivity(
                        this, 1234, intent, flags
                );
            } else {
                int flags = PendingIntent.FLAG_UPDATE_CURRENT;    // 非空，复用
                pendingIntent = PendingIntent.getActivity(
                        this, 1234, intent, flags
                );
            }
            try {
                pendingIntent.send();
                Log.i(TAG, "Succeed to send intent_FLAG_ONE_SHOT");
            } catch (PendingIntent.CanceledException e) {
                Log.i(TAG, "Failed to send intent_FLAG_ONE_SHOT", e);
            }
        });

        // 2. 创建 PendingIntent，先取消，再发送
        Button triggerButton = findViewById(R.id.btn_trigger);
        triggerButton.setOnClickListener(v -> {
            Log.i(TAG, "click cancel&send button");
            int flags = PendingIntent.FLAG_IMMUTABLE;
            pendingIntent = PendingIntent.getActivity(this, 6789, intent, flags);
            if (pendingIntent != null) {
                pendingIntent.cancel();   // 第一步：取消 PendingIntent（设置 canceled 状态为 true）
                Log.i(TAG, "已调用 cancel() 取消 PendingIntent");
                try {
                    pendingIntent.send();  // 第二步：尝试发送已取消的 PendingIntent
                    Log.i(TAG, "Succeed to send intent_Canceled（不符合预期）");
                } catch (PendingIntent.CanceledException e) {
                    Log.e(TAG, "Failed to send intent_Canceled（符合预期）", e);
                }
            }
        });

        // 3. 发送按钮：点击时触发 PendingIntent
        Button sendButton = findViewById(R.id.btn_send);
        sendButton.setOnClickListener(v -> {
            Log.i(TAG, "click send button");
            if (pendingIntent == null) {   // 空，创建一个新的
                Log.i(TAG, "pendingIntent == null, create");
                int flags = PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT;
                /*
                FLAG_UPDATE_CURRENT  复用当前的intent
                系统判断两个 PendingIntent 是否为 “同一实例” 并决定是否复用，取决于 3 个核心条件（必须全部相同）：
                相同的上下文（Context）：两次创建使用的 Context 必须是同一个（如都是 this 指向的 Activity）。
                相同的 requestCode：两次创建的 requestCode 必须完全一致（整数相等）。
                相同的 Intent 核心结构：通过 Intent.filterEquals() 判断，即 Action、Data、Type、Component、Categories 必须相同（extras 等附加数据不同不影响）。
                */
                pendingIntent = PendingIntent.getActivity(
                        this, 1234, intent, flags
                );
            } else {
                Log.i(TAG, "pendingIntent != null, reuse");
            }
            if (pendingIntent != null) {
                try {
                    pendingIntent.send();
                    Log.i(TAG, "Succeed to send intent");
                } catch (PendingIntent.CanceledException e) {
                    Log.i(TAG, "Failed to send intent");
                    e.printStackTrace();
                }
            } else {
                Log.i(TAG, "send pendingIntent==null");
            }
        });

        // 4. 取消按钮：点击时调用 cancel()
        Button cancelButton = findViewById(R.id.btn_cancel);
        cancelButton.setOnClickListener(v -> {
            Log.i(TAG, "click cancel button");
            if (pendingIntent == null) {   // 空，创建一个新的
                Log.i(TAG, "pendingIntent == null, create");
                int flags = PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT;
                pendingIntent = PendingIntent.getActivity(
                        this, 1234, intent, flags
                );
            } else {
                Log.i(TAG, "pendingIntent != null, reuse");
                int flags = PendingIntent.FLAG_UPDATE_CURRENT;    // 非空，复用
                pendingIntent = PendingIntent.getActivity(
                        this, 1234, intent, flags
                );
            }
            Log.i(TAG, "Canceled test");
            if (pendingIntent != null) {
                pendingIntent.cancel();   // 主动取消 PendingIntent（这会触发系统的 cancel 逻辑）
                Log.i(TAG, "Canceled sending intent");
            } else {
                Log.i(TAG, "Canceled sending pendingIntent==null");
            }
        });
    }
}