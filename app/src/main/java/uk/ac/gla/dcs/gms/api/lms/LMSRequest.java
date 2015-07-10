package uk.ac.gla.dcs.gms.api.lms;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.HttpURLConnection;

import uk.ac.gla.dcs.gms.api.APIHttpResponse;
import uk.ac.gla.dcs.gms.api.HTTPCustomException;
import uk.ac.gla.dcs.gms.lms.R;

/**
 * Created by ito on 07/07/2015.
 */
public abstract class LMSRequest<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    private static final String TAG = "LMSRequest";

    protected Context context;

    public LMSRequest(Context context) {
        this.context = context.getApplicationContext();
    }

    protected Resources getResources() {
        return context.getResources();
    }

    protected void getCookie() {
//        HttpCookie cookie = new HttpCookie("lang", "fr");
//        cookie.setDomain("twitter.com");
//        cookie.setPath("/");
//        cookie.setVersion(0);
//        cookieManager.getCookieStore().add(new URI("http://twitter.com/"), cookie);
    }


    protected Object getErrorField(APIHttpResponse response) throws HTTPCustomException {
        return getField(response, getResources().getString(R.string.lms_api_StandardFieldErrorsKey));
    }

    protected Object getDataField(APIHttpResponse response) throws HTTPCustomException {
        return getField(response, getResources().getString(R.string.lms_api_StandardFieldDataKey));
    }

    protected Object getStatusField(APIHttpResponse response) throws HTTPCustomException {
        return getField(response, getResources().getString(R.string.lms_api_StandardFieldStatusKey));
    }

    protected final Object getField(APIHttpResponse response, String fieldName) throws HTTPCustomException {
        try {
            if (response.getJsonObject().has(fieldName)){
            return response.getJsonObject().get(fieldName);}
            else
                return null;
        } catch (JSONException e) {
            Log.w(TAG, "Failed to get " + fieldName + "field from JSON Object.");
            throw new HTTPCustomException(response, 0, "Failed to get " + fieldName + "field from JSON Object.");
        }
    }

    protected final Object processResponse(APIHttpResponse response, int expectedStatusCode) throws HTTPCustomException {
        if (response == null || response.isFailed()) {
            //check if had an exception
            throw new HTTPCustomException(response, 0, getResources().getString(R.string.error_messages_genericNetworkFail)); //FIXME error code
        } else if (response.getJsonObject() == null) {
            //check if it has json data
            throw new HTTPCustomException(response, HttpURLConnection.HTTP_NO_CONTENT, getResources().getString(R.string.error_messages_genericNetworkFail));
        } else {
            Object statusField = getStatusField(response);
            //check for status field
            if (statusField instanceof Integer) {
                int status = (int) statusField;

                //check for errors
                Object errors = getErrorField(response);
                if (errors instanceof JSONArray) {
                    String errorMsg = "";
                    try {
                        JSONArray jArray = (JSONArray) errors;
                        errorMsg = jArray.getString(0);         //FIXME: improve for multiple messages
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to get error message");
                        e.printStackTrace();
                        errorMsg = "Unknown error. (" + e.getMessage() + ")";
                    }
                    throw new HTTPCustomException(response, status, errorMsg);
                } else {
                    //get data
                    if (status == expectedStatusCode) {
                        Object dataField = getDataField(response);
                        return onSuccessfulProcessedResponse(response, status, dataField);
                    } else {
                        //wrong status code
                        throw new HTTPCustomException(response, status, "Unexpected response from server");
                    }
                }
            } else
                throw new HTTPCustomException(response, 0, "Invalid status code."); //FIXME error code
        }
    }

    protected Object onSuccessfulProcessedResponse(APIHttpResponse response, int statusCode, Object data) throws HTTPCustomException {
        return data;
    }

}
