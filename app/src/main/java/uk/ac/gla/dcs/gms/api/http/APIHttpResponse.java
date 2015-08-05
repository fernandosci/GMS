package uk.ac.gla.dcs.gms.api.http;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class APIHttpResponse {
    private static final String TAG = "APIHttpResponse";

    private HttpURLConnection urlConnection;
    private String responseData;
    private Exception exception;
    private boolean failed;


    public APIHttpResponse(HttpURLConnection urlConnection, String data) {
        this.urlConnection = urlConnection;
        this.failed = false;
        this.exception = null;
        this.responseData = data;
    }

    public APIHttpResponse(HttpURLConnection urlConnection, boolean failed, Exception exception) {
        this.urlConnection = urlConnection;
        this.failed = failed;
        this.exception = exception;
        this.responseData = null;
    }


    public HttpURLConnection getUrlConnection() {
        return urlConnection;
    }

    public JSONObject getResponseAsJSON() {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(responseData);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
            jsonObject = null;
        }
        return jsonObject;
    }

    public Exception getException() {
        return exception;
    }

    public boolean isFailed() {
        return failed;
    }

    public String getResponseData() {
        return responseData;
    }
}
