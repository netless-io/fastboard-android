package io.agora.board.fast.sample.misc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import io.agora.board.fast.sample.MainActivity;

/**
 * author : fenglibin
 * date : 2024/6/13
 * description :
 * <p>
 * use adb command to send broadcast
 * <p>
 * adb shell am broadcast -a io.agora.board.fast.sample.ACTION --es extra_data "Hello Fastboard" io.agora.board.fast.sample/.misc.MyBroadcastReceiver
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";
    private static final String ACTION = "io.agora.board.fast.sample.ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Broadcast received");
        handleAction(context, intent);
        Log.i(TAG, "Broadcast processing complete");
    }

    private void handleAction(Context context, Intent intent) {
        if (ACTION.equals(intent.getAction())) {
            processAction(context, intent);
        } else {
            Log.i(TAG, "Unknown action received");
        }
    }

    private void processAction(Context context, Intent intent) {
        String extraData = intent.getStringExtra("extra_data");
        Log.i(TAG, "Received broadcast with data: " + extraData);

        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}