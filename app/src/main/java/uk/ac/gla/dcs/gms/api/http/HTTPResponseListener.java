package uk.ac.gla.dcs.gms.api.http;

import java.util.HashMap;


public interface  HTTPResponseListener {

    void onResponse(int requestCode, boolean successful, HashMap<String, Object> data, Exception exception);

    void onProgress(int requestCode, HTTPProgressStatus progressStatus, HashMap<String, Object> newdata);
}
