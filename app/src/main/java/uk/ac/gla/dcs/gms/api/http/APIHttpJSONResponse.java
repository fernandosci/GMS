package uk.ac.gla.dcs.gms.api.http;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class APIHttpJSONResponse {
    private static final String TAG = "APIHttpResponse";

    private HttpURLConnection urlConnection;
    private JSONObject jsonResponse;
    private Exception exception;
    private boolean failed;


    public APIHttpJSONResponse(HttpURLConnection urlConnection, String jsonStr) {
        this.urlConnection = urlConnection;
        this.failed = false;
        this.exception = null;

        try {
            this.jsonResponse = new JSONObject(jsonStr);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            failed = true;
            exception = e;
        }
    }

    public APIHttpJSONResponse(HttpURLConnection urlConnection, boolean failed, Exception exception) {
        this.urlConnection = urlConnection;
        this.failed = failed;
        this.exception = exception;
        this.jsonResponse = null;
    }


    public HttpURLConnection getUrlConnection() {
        return urlConnection;
    }

    public JSONObject getJsonResponse() {
        return jsonResponse;
    }

    public Exception getException() {
        return exception;
    }

    public boolean isFailed() {
        return failed;
    }
}
