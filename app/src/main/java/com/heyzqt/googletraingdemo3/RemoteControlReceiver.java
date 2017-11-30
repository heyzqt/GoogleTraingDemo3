package com.heyzqt.googletraingdemo3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Created by heyzqt on 11/29/2017.
 */

public class RemoteControlReceiver extends BroadcastReceiver {

    private static final String TAG = "RemoteControlReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: ");
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            KeyEvent event = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (KeyEvent.KEYCODE_MEDIA_PLAY == event.getKeyCode()) {
                Log.i(TAG, "onReceive: media pley");
            } else if (KeyEvent.KEYCODE_MEDIA_NEXT == event.getKeyCode()) {
                Log.i(TAG, "onReceive: media next");
            }
        }
    }
}
