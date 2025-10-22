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
            int flags = PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE; // 使用 FLAG_UPDATE_CURRENT 方便复用
            pendingIntent = PendingIntent.getActivity(
                    this, 12345, intent, flags
            );
            if (pendingIntent == null) {
                Toast.makeText(this, "PendingIntent 为空", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "pendingIntent == null");
                return;
            }
            try {
                pendingIntent.send();
                Log.i(TAG, "Succeed to send intent_FLAG_ONE_SHOT");
                Toast.makeText(this, "发送成功", Toast.LENGTH_SHORT).show();
            } catch (PendingIntent.CanceledException e) {
                Log.i(TAG, "Failed to send intent_FLAG_ONE_SHOT", e);
                Toast.makeText(this, "发送失败（已取消）", Toast.LENGTH_SHORT).show();
            }
        });

        // 2. 创建 PendingIntent，先取消，再发送
        Button triggerButton = findViewById(R.id.btn_trigger);
        triggerButton.setOnClickListener(v -> {
            int flags = PendingIntent.FLAG_UPDATE_CURRENT;
            pendingIntent = PendingIntent.getActivity(this, 6789, intent, flags);
            if (pendingIntent != null) {
                // 第一步：取消 PendingIntent（设置 canceled 状态为 true）
                pendingIntent.cancel();
                Log.d(TAG, "已调用 cancel() 取消 PendingIntent");

                // 第二步：尝试发送已取消的 PendingIntent
                try {
                    pendingIntent.send();
                    Log.d(TAG, "发送成功（不符合预期）");
                } catch (PendingIntent.CanceledException e) {
                    Log.e(TAG, "发送失败：PendingIntent 已取消（符合预期）", e);
                }
            }
        });

        // 3. 发送按钮：点击时触发 PendingIntent
        Button sendButton = findViewById(R.id.btn_send);
        sendButton.setOnClickListener(v -> {
            Log.i(TAG, "click send button");
            if (pendingIntent == null) {   // 空，创建一个新的
                int flags = PendingIntent.FLAG_IMMUTABLE;
                pendingIntent = PendingIntent.getActivity(
                        this, 12345, intent, flags
                );
            } else {
                int flags = PendingIntent.FLAG_UPDATE_CURRENT;   // 非空，复用当前的
                pendingIntent = PendingIntent.getActivity(
                        this, 12345, intent, flags
                );
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
            int flags = PendingIntent.FLAG_UPDATE_CURRENT;    // 复用示例（action一样，会复用）
            pendingIntent = PendingIntent.getActivity(
                    this, 12345, intent, flags
            );
            if (pendingIntent != null) {
                // 主动取消 PendingIntent（这会触发系统的 cancel 逻辑）
                pendingIntent.cancel();
                Toast.makeText(MainActivity.this, "已取消 PendingIntent", Toast.LENGTH_SHORT).show();
                // 取消后可以置空，避免重复取消
                // pendingIntent = null;
                // pendingIntent = PendingIntent.getActivity(this, 12345, intent, flags);
            } else {
                Toast.makeText(MainActivity.this, "PendingIntent 已被取消", Toast.LENGTH_SHORT).show();
            }
        });
    }
}