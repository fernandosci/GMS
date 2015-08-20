package uk.ac.gla.dcs.gms;

import android.app.Application;

import uk.ac.gla.dcs.gms.api.GMS;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        GMS.initialize(getApplicationContext());
    }
}
