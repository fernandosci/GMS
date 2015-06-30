package uk.ac.gla.dcs.gms.api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by ito on 29/06/2015.
 */
public class APIResponse {

    private static final String TAG = "APIResponse";

    private String rawResponse;
    private Map<String, List<String>> headerFields;
    private Exception exception;
    private boolean failed;
    private JSONObject jsonObject;

    public APIResponse(String response, Map<String, List<String>> headerFields, boolean failed, Exception exception) {
        this.rawResponse = response;
        this.headerFields = headerFields;
        this.failed = failed;
        jsonObject = null;
        if (!failed) {
            try {
                jsonObject = new JSONObject(this.rawResponse);
            } catch (JSONException e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
            }
        }
    }

    public String getRawResponse() {
        return rawResponse;
    }

    public Map<String, List<String>> getHeaderFields() {
        return headerFields;
    }

    public boolean isFailed() {
        return failed;
    }

    public Exception getException() {
        return exception;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }
}
