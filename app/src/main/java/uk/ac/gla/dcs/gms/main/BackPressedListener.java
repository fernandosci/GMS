package uk.ac.gla.dcs.gms.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by ito.
 */
public abstract class BackPressedListener extends BroadcastReceiver {

    public static final String BROADCAST_EVENT = "asldfjasldfj";
    public static final String BROADCAST_REPLY= "asldfjaSSSsldfj";
    public static final String KEY_ID = "ID";
    public static final String KEY_CONSUMED = "C";

    

    @Override
    public final void onReceive(Context context, Intent intent) {
        //filter here
        Bundle extras = intent.getExtras();
        int anInt = extras.getInt(KEY_ID);

        boolean result = onBackPressed();

        Intent i = new Intent(BROADCAST_REPLY);
        i.putExtra(KEY_ID,anInt);
        i.putExtra(KEY_CONSUMED,result);

        context.sendBroadcast(i);
    }

    public abstract boolean onBackPressed();
}
