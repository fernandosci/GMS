package uk.ac.gla.dcs.gms.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import uk.ac.gla.dcs.gms.GMSBroadcastManager;

/**
 * Created by ito.
 */
public abstract class BackPressedListener extends BroadcastReceiver {

    public static final String BACKPRESSEDACTION = "asldfjasldfj";
    public static final String BACKPRESSEDRESPONSE = "asldfjaSSSsldfj";
    public static final String KEY_ID = "ID";
    public static final String KEY_CONSUMED = "C";



    @Override
    public final void onReceive(Context context, Intent intent) {
        //filter here
        Bundle extras = intent.getExtras();
        int anInt = extras.getInt(KEY_ID);

        boolean result = onBackPressed();

        Intent i = new Intent(BACKPRESSEDRESPONSE);
        i.putExtra(KEY_ID,anInt);
        i.putExtra(KEY_CONSUMED, result);

        GMSBroadcastManager broadcastManager = GMSBroadcastManager.getInstance(context);
        broadcastManager.unregisterReceiver(this);
        broadcastManager.sendBroadcast(i);
    }

    public abstract boolean onBackPressed();
}
