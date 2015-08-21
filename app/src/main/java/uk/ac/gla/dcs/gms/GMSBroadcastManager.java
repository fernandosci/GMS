package uk.ac.gla.dcs.gms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by ito.
 */
public class GMSBroadcastManager {

    private static GMSBroadcastManager instance = null;
    private static final Object mLock = new Object();

    private HashMap<String, Integer> actionCount;
    private HashMap<BroadcastReceiver, IntentFilter> receivers;
    private LocalBroadcastManager lBroadcastMInstance;


    private GMSBroadcastManager(Context context) {
        lBroadcastMInstance = LocalBroadcastManager.getInstance(context);
        actionCount = new HashMap<>();
        receivers = new HashMap<>();
    }

    public static GMSBroadcastManager getInstance(Context context) {
        Object var1 = mLock;
        synchronized(mLock) {
            if(instance == null) {
                instance = new GMSBroadcastManager(context.getApplicationContext());
            }
            return instance;
        }
    }

    public synchronized void registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {

        Iterator<String> stringIterator = filter.actionsIterator();

        while (stringIterator.hasNext()){
            String next = stringIterator.next();

            Integer count = actionCount.get(next);
            if (count == null){
                count = new Integer(0);
            }
            count++;
            actionCount.put(next,count);
        }


        receivers.put(receiver,filter);

        lBroadcastMInstance.registerReceiver(receiver, filter);
    }

    public synchronized void unregisterReceiver(BroadcastReceiver receiver) {

        IntentFilter filter = receivers.get(receiver);
        if (filter == null)
            return;

        Iterator<String> stringIterator = filter.actionsIterator();

        while (stringIterator.hasNext()){
            String next = stringIterator.next();

            Integer count = actionCount.get(next);
            if (count != null){
                count--;
                if (count == 0)
                    actionCount.remove(next);
                else
                    actionCount.put(next,count);
            }
        }

        receivers.remove(receiver);
        lBroadcastMInstance.unregisterReceiver(receiver);
    }

    public boolean sendBroadcast(Intent intent) {

        return lBroadcastMInstance.sendBroadcast(intent);
    }

    public void sendBroadcastSync(Intent intent) {

        sendBroadcastSync(intent);
    }

    public synchronized int getRegisteredActionCount(String action){

        Integer integer = actionCount.get(action);
        if (integer == null)
            return 0;
        else
            return integer;

    }
}
