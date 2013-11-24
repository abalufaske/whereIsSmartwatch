package abalufaske.where.is;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SmartWatchReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.v("debug", "onReceive: " + intent.getAction());
        intent.setClass(context, SmartWatchService.class);
        context.startService(intent);
    }
    
}
