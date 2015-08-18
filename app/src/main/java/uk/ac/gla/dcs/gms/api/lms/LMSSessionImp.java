package uk.ac.gla.dcs.gms.api.lms;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import uk.ac.gla.dcs.gms.api.GMSException;
import uk.ac.gla.dcs.gms.api.GMSExceptionBuilder;
import uk.ac.gla.dcs.gms.api.OnCredentialsRequiredListener;
import uk.ac.gla.dcs.gms.api.http.APIHttpResponse;
import uk.ac.gla.dcs.gms.api.http.HTTPProgressStatus;
import uk.ac.gla.dcs.gms.api.http.HTTPResponseListener;
import uk.ac.gla.dcs.gms.lms.R;


public class LMSSessionImp implements LMSSession {

    private static final String TAG = "LMSSessionImp";

    private static final int PROCESSCODE_LOGIN = 1001;
    private static final int PROCESSCODE_REGISTER = 1002;
    private static final int PROCESSCODE_USERDETAIL = 1003;
    private static final int PROCESSCODE_STATS = 1004;
    private static final int PROCESSCODE_VIDEOS = 1005;
    private static final int PROCESSCODE_IMAGES = 1006;
    private static final int PROCESSCODE_TRAILS = 1007;


    private Context context;
    private String token;
    private boolean expired;
    private int timeout;

    private OnCredentialsRequiredListener credentialsRequiredListener;

    public LMSSessionImp(Context context) {
        this.context = context.getApplicationContext();
        this.credentialsRequiredListener = null;
        token = "";
        expired = false;
        timeout = 5000;
    }

    public LMSSessionImp(Context context, OnCredentialsRequiredListener onCredentialsRequiredListener, String token) {
        this.context = context.getApplicationContext();
        this.credentialsRequiredListener = onCredentialsRequiredListener;
        this.token = token;
        this.expired = false;
        timeout = 5000;
    }

    private static Object getField(JSONObject response, String fieldName) throws GMSException {
        try {
            if (response != null) {
                if (response.has(fieldName)) {
                    return response.get(fieldName);
                } else
                    return null;
            } else {
                Log.w(TAG, "Null JsonResponse.");
                return null;
            }
        } catch (JSONException e) {
            //should never reach here...
            Log.w(TAG, "Failed to get " + fieldName + "field from JSON Object.");
            e.printStackTrace();
            throw new GMSException("Failed to get " + fieldName + "field from JSON Object.", e, -1);//FIXME error code
        }
    }

    protected Resources getResources() {
        return context.getResources();
    }

    public void login(HTTPResponseListener listener, int requestCode, String username, String password) {
        requestWithAuthorization(listener, PROCESSCODE_LOGIN, requestCode, getUrlConnection(listener, requestCode, getResources().getString(R.string.lms_endpointLogin), ""), username + ":" + password);
    }

    public void register(HTTPResponseListener listener, int requestCode, String username, String password, String email, String answer, String question) {
        requestWithAuthorization(listener, PROCESSCODE_REGISTER, requestCode, getUrlConnection(listener, requestCode, getResources().getString(R.string.lms_endpointRegister), ""), username + ":" + password + ":" + email + ":" + answer + ":" + question);
    }

    public void getUserDetails(HTTPResponseListener listener, int requestCode) {
        HttpURLConnection urlConnection = getUrlConnection(listener, requestCode, getResources().getString(R.string.lms_endpointUser), "");
        requestWithCookie(listener, PROCESSCODE_USERDETAIL, requestCode, urlConnection, "GET");
    }

    @Override
    public void getStats(HTTPResponseListener listener, int requestCode, String urlParameters) {
        HttpURLConnection urlConnection = getUrlConnection(listener, requestCode, getResources().getString(R.string.lms_endpointStats), urlParameters);
        requestWithCookie(listener, PROCESSCODE_STATS, requestCode, urlConnection, "GET");

    }

    @Override
    public void getVideos(HTTPResponseListener listener, int requestCode, String urlParameters) {
        HttpURLConnection urlConnection = getUrlConnection(listener, requestCode, getResources().getString(R.string.lms_endpointGetVideos), urlParameters);
        requestWithCookie(listener, PROCESSCODE_VIDEOS, requestCode, urlConnection, "GET");

    }

    @Override
    public void getImages(HTTPResponseListener listener, int requestCode, String urlParameters) {
        HttpURLConnection urlConnection = getUrlConnection(listener, requestCode, getResources().getString(R.string.lms_endpointGetImages), urlParameters);
        requestWithCookie(listener, PROCESSCODE_IMAGES, requestCode, urlConnection, "GET");

    }

    @Override
    public void getTrails(HTTPResponseListener listener, int requestCode, String urlParameters) {
        HttpURLConnection urlConnection = getUrlConnection(listener, requestCode, getResources().getString(R.string.lms_endpointGetTrails), urlParameters);
        requestWithCookie(listener, PROCESSCODE_TRAILS, requestCode, urlConnection, "GET");

    }

    @Override
    public boolean isExpired() {
        return token.isEmpty() || expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    protected void renewSession(String newToken){
        this.token = newToken;
        this.expired = false;
        //TODO maybe do some verification, but not sure if needed
    }

    private HttpURLConnection getUrlConnection(HTTPResponseListener listener, int requestCode, String complement, String parameters) {
        try {
            return (HttpURLConnection) new URL(getResources().getString(R.string.lms_httpUrl) + complement + parameters).openConnection();
        } catch (IOException e) {
            //should never reach here
            e.printStackTrace();
            if (listener != null)
                listener.onResponse(requestCode, false, new HashMap<String, Object>(0), e);
            return null;
        }
    }

    private void requestWithAuthorization(HTTPResponseListener listener, int processCode, int requestCode, HttpURLConnection urlConnection, String authString) {
        if (urlConnection == null)
            return;
        try {
            urlConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            //should never reach here
            e.printStackTrace();
            if (listener != null)
                listener.onResponse(requestCode, false, new HashMap<String, Object>(0), e);
            return;
        }
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setConnectTimeout(timeout);
        String basicAuth = "Basic " + new String(Base64.encode(authString.getBytes(), Base64.NO_WRAP | Base64.URL_SAFE));
        urlConnection.setRequestProperty("Authorization", basicAuth);

        LMSHttpRequest request = new LMSHttpRequest(listener, requestCode, processCode);
        request.execute(urlConnection);
    }

    private void requestWithCookie(HTTPResponseListener listener, int processCode, int requestCode, HttpURLConnection urlConnection, String requestMethod) {
        if (urlConnection == null)
            return;

        try {
            urlConnection.setRequestMethod(requestMethod);
        } catch (ProtocolException e) {
            //should never reach here
            e.printStackTrace();
            if (listener != null)
                listener.onResponse(requestCode, false, new HashMap<String, Object>(0), e);
            return;
        }
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setConnectTimeout(timeout);

        urlConnection.setRequestProperty("Cookie", "token=" + token);

        LMSHttpRequest request = new LMSHttpRequest(listener, requestCode, processCode);
        request.execute(urlConnection);
    }

    private class LMSHttpRequest extends AsyncTask<HttpURLConnection, HTTPProgressStatus, APIHttpResponse> {
        private static final String TAG = "LMSHttpRequest";
        private final HTTPResponseListener httpResponseListener;
        private final int requestCode;
        private final int processCode;

        private final HashMap<String, Object> dataResult;
        private final ArrayList<String> errorMessages;
        private Integer status;
        private GMSExceptionBuilder exceptionBuilder;



        public LMSHttpRequest(HTTPResponseListener httpResponseListener, int requestCode, int processCode) {
            this.requestCode = requestCode;
            this.processCode = processCode;
            this.httpResponseListener = httpResponseListener;
            this.errorMessages = new ArrayList<>();
            this.dataResult = new HashMap<>();
        }

        @Override
        protected APIHttpResponse doInBackground(HttpURLConnection... params) {
            APIHttpResponse httpResponse = null;

            HttpURLConnection urlConnection = params[0];

            InputStream in = null;

            try {
                in = urlConnection.getInputStream();
                httpResponse = new APIHttpResponse(urlConnection, IOUtils.toString(in, "UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
                httpResponse = new APIHttpResponse(urlConnection, true, e);
            }

            //set status, errorMessages, data and exceptions if any
            processHttpResponse(httpResponse);

            return httpResponse;
        }

        @Override
        protected void onPostExecute(APIHttpResponse response) {
            Exception e;
            if (exceptionBuilder == null) {
                e = null;
            }else{
                e = exceptionBuilder.addExtra("response", response).build();
            }

            //return the results, successful if no error messages and no exceptions
            httpResponseListener.onResponse(requestCode, errorMessages.isEmpty() && e == null, dataResult, e);
        }

        private void processHttpResponse(APIHttpResponse response){
            exceptionBuilder = null;
            status = null;

            if (response.isFailed()) {
                //check if had an exception, encapsulate to give a response
                exceptionBuilder = new GMSExceptionBuilder(null, -1, getResources().getString(R.string.lms_messages_errors_genericNetworkFail));
                status = -1;
            } else {
                //check if its a json object
                JSONObject responseAsJSON = response.getResponseAsJSON();
                if (responseAsJSON != null) {
                    //set fields status and error messages, fill exception builder if necessary and get json data field
                    Object dataObj = processJsonData(responseAsJSON);
                    if (dataObj != null){
                        fillDataResult(dataObj);
                    }
                }
                else{
                    //non json response data.. not sure what to do to validate...
                    //set status field
                    try {
                        status = response.getUrlConnection().getResponseCode();
                    } catch (IOException e) {
                        status = -1;   //FIXME put a status code that makes sense...
                    }
                    // there is no error message from response, get only the data content...
                    if (!response.getResponseData().isEmpty())
                        fillDataResult(response.getResponseData());
                }
            }

            dataResult.put(getResources().getString(R.string.lms_api_StandardFieldStatusKey), status);
            if ( errorMessages.size() > 0)
                dataResult.put(getResources().getString(R.string.lms_api_StandardFieldErrorsKey), errorMessages);

        }



        private Object processJsonData(JSONObject responseAsJSON){
            Object dataObj = null;
            //process json data, check for status, errors and data
            try {
                //get status
                Object statusObj = getField(responseAsJSON, getResources().getString(R.string.lms_api_StandardFieldStatusKey));
                if (statusObj instanceof Integer) {
                    status = (Integer) statusObj;

                    //check for errors
                    Object errorsObj = getField(responseAsJSON, getResources().getString(R.string.lms_api_StandardFieldErrorsKey));
                    if (errorsObj instanceof JSONArray) {
                        JSONArray jArray = (JSONArray) errorsObj;

                        for (int c = 0; c < jArray.length(); c++) {
                            try {
                                errorMessages.add(jArray.getString(c));
                            } catch (JSONException e) {
                                //also should never reach here
                                Log.e(TAG, "Error processing error field index " + c + "/" + jArray.length());
                                e.printStackTrace();
                            }
                        }
                    }

                    //get data
                    dataObj = getField(responseAsJSON, getResources().getString(R.string.lms_api_StandardFieldDataKey));

                    //if reach here, no errors found yet
                } else {
                    //should never reach here
                    exceptionBuilder = new GMSExceptionBuilder(null, -1, "Error processing status code.");//FIXME error code
                }
            } catch (GMSException e) {
                //should never reach here
                Log.e(TAG, "Error processing json response");
                e.printStackTrace();
                exceptionBuilder = new GMSExceptionBuilder(e, -1, "Error getting field from json content."); //FIXME error code
            }

            return dataObj;
        }


        @Override
        protected void onProgressUpdate(HTTPProgressStatus... values) {
            //TODO might not be necessary..
        }

        private void fillDataResult(Object dataObj) {
            try {
                switch (processCode) {
                    case PROCESSCODE_LOGIN:
                    case PROCESSCODE_REGISTER: {
                        processAuthentication(dataObj);
                    }
                    break;
                    case PROCESSCODE_IMAGES: {
                        processGetImages(dataObj);
                    }
                    break;
                    case PROCESSCODE_STATS: {
                        processGetStats(dataObj);
                    }
                    break;
                    case PROCESSCODE_TRAILS: {
                        processGetTrails(dataObj);
                    }
                    break;
                    case PROCESSCODE_USERDETAIL: {
                        processUserInfo( dataObj);
                    }
                    break;
                    case PROCESSCODE_VIDEOS: {
                        processGetVideos(dataObj);
                    }
                    break;
                    default: {

                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error processing data");
                e.printStackTrace();
                exceptionBuilder = new GMSExceptionBuilder(e, -1, "Error processing data content");
            }
        }

        private void processUserInfo( Object dataObj) throws Exception {
            try {
                if (dataObj instanceof JSONObject) {
                    JSONObject objUser = (JSONObject) dataObj;

                    Iterator<String> keys = objUser.keys();

                    while (keys.hasNext()) {
                        String key = keys.next();
                        dataResult.put(key, objUser.getString(key));
                    }
                }

            } catch (JSONException e) {
                Log.e(TAG, "Error processing user information from json content.");
                e.printStackTrace();
                throw new Exception("Error processing user information from json content.");
            }
            throw new Exception("Could not parse user information content.");
        }

        private void processAuthentication( Object dataObj) throws Exception {
            if (dataObj instanceof String && ((String) dataObj).length() > 0) {
                dataResult.put(context.getResources().getString(R.string.LMS_DATAKEY_TOKEN), dataObj);
            } else {
                throw new Exception("Invalid token received");
            }
        }

        private void processGetImages( Object dataObj) throws Exception {

            if (dataObj instanceof JSONArray) {
                JSONArray jarray = (JSONArray) dataObj;
                ArrayList<String> urls = new ArrayList<>();
                
                for (int c = 0; c < jarray.length(); c++) {
                    try {
                        JSONObject o = (JSONObject) jarray.get(c);

                        urls.add(((String) o.get("url")).replace("..", getResources().getString(R.string.lms_httpUrl)));

                        Iterator<String> it = o.keys();
                        while (it.hasNext()) {
                            String n = it.next();
//                            // TODO: add other values here
//                            pairs.put(n, o.getString(n));
                        }


                    } catch (JSONException e) {
                        Log.e(TAG, "Error processing images urls from json content.");
                        e.printStackTrace();
                        throw new Exception("Error processing images urls from json content.");
                    }
                }

                dataResult.put(context.getResources().getString(R.string.LMS_DATAKEY_UrlImgList), urls);
            }
        }

        private void processGetStats( Object dataObj) throws Exception {
            //TODO

        }

        private void processGetTrails( Object dataObj) throws Exception {
            //not the best way to check type but...
            if (dataObj instanceof String && ((String) dataObj).startsWith("<?xml")){
                dataResult.put(context.getResources().getString(R.string.LMS_DATAKEY_XMLTRAILS), dataObj);
            }else{
                JSONArray jCoords = (JSONArray)dataObj;
                List<LatLng> heatmap = new ArrayList<>();

                for (int c = 0 ;c < jCoords.length(); c++){
                    JSONArray coord = (JSONArray) jCoords.get(c);
                    heatmap.add(new LatLng(coord.getDouble(0),coord.getDouble(1)));
                }

                dataResult.put(context.getResources().getString(R.string.LMS_DATAKEY_HEATMAP), heatmap);
            }
        }

        private void processGetVideos(Object dataObj) throws Exception {
            //TODO

        }


    }

}
