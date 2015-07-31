package uk.ac.gla.dcs.gms.api.lms;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

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

import uk.ac.gla.dcs.gms.api.GMSException;
import uk.ac.gla.dcs.gms.api.GMSExceptionBuilder;
import uk.ac.gla.dcs.gms.api.OnCredentialsRequiredListener;
import uk.ac.gla.dcs.gms.api.http.APIHttpJSONResponse;
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
    private LMSService lmsService;
    private String token;
    private boolean expired;
    private int timeout;

    private OnCredentialsRequiredListener credentialsRequiredListener;

    public LMSSessionImp(Context context, LMSService lmsService) {
        this.lmsService = lmsService;
        this.context = context.getApplicationContext();
        this.credentialsRequiredListener = null;
        token = "";
        expired = false;
        timeout = 5000;
    }

    public LMSSessionImp(Context context, LMSService lmsService, String token) {
        this.context = context;
        this.lmsService = lmsService;
        this.token = token;
        this.expired = false;
        timeout = 5000;
    }

    private static Object getField(APIHttpJSONResponse response, String fieldName) throws GMSException {
        try {
            if (response.getJsonResponse() != null) {
                if (response.getJsonResponse().has(fieldName)) {
                    return response.getJsonResponse().get(fieldName);
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

        LMSHttpJsonRequest request = new LMSHttpJsonRequest(listener, requestCode, processCode);
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

        LMSHttpJsonRequest request = new LMSHttpJsonRequest(listener, requestCode, processCode);
        request.execute(urlConnection);
    }

    private class LMSHttpJsonRequest extends AsyncTask<HttpURLConnection, HTTPProgressStatus, APIHttpJSONResponse> {
        private static final String TAG = "LMSHttpRequest";
        private final HTTPResponseListener httpResponseListener;
        private int requestCode;
        private int processCode;

        public LMSHttpJsonRequest(HTTPResponseListener httpResponseListener, int requestCode, int processCode) {
            this.requestCode = requestCode;
            this.processCode = processCode;
            this.httpResponseListener = httpResponseListener;
        }

        @Override
        protected APIHttpJSONResponse doInBackground(HttpURLConnection... params) {

            APIHttpJSONResponse response = null;
            HTTPProgressStatus ps = new HTTPProgressStatus(params.length);


            HttpURLConnection urlConnection = params[0];

            InputStream in = null;

            try {
                in = urlConnection.getInputStream();
                response = new APIHttpJSONResponse(urlConnection, IOUtils.toString(in, "UTF-8"));
                ps.incProcessed();
            } catch (IOException e) {
                ps.incFailed();
                e.printStackTrace();
                response = new APIHttpJSONResponse(urlConnection, true, e);
            }
            publishProgress(new HTTPProgressStatus(ps));
            return response;
        }

        @Override
        protected void onPostExecute(APIHttpJSONResponse response) {
            GMSExceptionBuilder exceptionBuilder = null;
            Integer status = null;
            String[] errorMessages = null;
            Object dataObj = null;

            if (response.isFailed()) {
                //check if had an exception, encapsulate to give a response
                exceptionBuilder = new GMSExceptionBuilder(null, -1, getResources().getString(R.string.lms_messages_errors_genericNetworkFail));//FIXME error code
            } else if (response.getJsonResponse() == null) {
                //json requests always
                exceptionBuilder = new GMSExceptionBuilder(null, -1, "Json response is invalid or not found.");//FIXME error code
            } else {
                //process json data, check for status, errors and data
                try {
                    //get status
                    Object statusObj = getField(response, getResources().getString(R.string.lms_api_StandardFieldStatusKey));
                    if (statusObj instanceof Integer) {
                        status = (Integer) statusObj;

                        //check for errors
                        Object errorsObj = getField(response, getResources().getString(R.string.lms_api_StandardFieldErrorsKey));
                        if (errorsObj instanceof JSONArray) {
                            JSONArray jArray = (JSONArray) errorsObj;

                            errorMessages = new String[jArray.length()];
                            for (int c = 0; c < errorMessages.length; c++) {
                                try {
                                    errorMessages[c] = jArray.getString(c);
                                } catch (JSONException e) {
                                    //also should never reach here
                                    errorMessages[c] = "";
                                    Log.e(TAG, "Error processing error field");
                                    e.printStackTrace();
                                }
                            }
                        }

                        //get data
                        dataObj = getField(response, getResources().getString(R.string.lms_api_StandardFieldDataKey));

                        //if reach here, no errors found yet
                    } else {
                        //should never reach here
                        exceptionBuilder = new GMSExceptionBuilder(null, -1, "Error processing status code.");//FIXME error code
                    }
                } catch (GMSException e) {
                    //should never reach here
                    Log.e(TAG, "Error processing response");
                    e.printStackTrace();
                    exceptionBuilder = new GMSExceptionBuilder(e, -1, "Error getting field from json content."); //FIXME error code
                }
            }
//
            if (exceptionBuilder == null) {
                //process data
                HashMap<String, Object> data = null;
                try {
                    data = processResponse(processCode, status, errorMessages, dataObj, response);

                } catch (Exception e) {
                    Log.e(TAG, "Error processing data from json content.");
                    e.printStackTrace();
                    exceptionBuilder = new GMSExceptionBuilder(e, -1, "Error processing data from json content.");
                }

                if (exceptionBuilder == null) {
                    //success!
                    httpResponseListener.onResponse(requestCode, errorMessages == null, data, null);
                    return;
                }
            }
            //here there was an exception, forward it...
            httpResponseListener.onResponse(requestCode, false, new HashMap<String, Object>(0), exceptionBuilder.addExtra("response", response).build());
        }

        @Override
        protected void onProgressUpdate(HTTPProgressStatus... values) {
            //TODO


        }

        private HashMap<String, Object> processResponse(int processCode, Integer status, String[] errorMessages, Object dataObj, APIHttpJSONResponse response) throws Exception {

            HashMap<String, Object> data = new HashMap<>();

            data.put(getResources().getString(R.string.lms_api_StandardFieldStatusKey), status);

            if (errorMessages != null && errorMessages.length > 0)

                data.put(getResources().getString(R.string.lms_api_StandardFieldErrorsKey), errorMessages);

            else {

                switch (processCode) {
                    case PROCESSCODE_LOGIN:
                    case PROCESSCODE_REGISTER: {
                        processAuthentication(data, dataObj);
                    }
                    break;
                    case PROCESSCODE_IMAGES: {
                        processGetImages(data, dataObj);
                    }
                    break;
                    case PROCESSCODE_STATS: {
                        processGetStats(data, dataObj);
                    }
                    break;
                    case PROCESSCODE_TRAILS: {
                        processGetTrails(data, dataObj);
                    }
                    break;
                    case PROCESSCODE_USERDETAIL: {
                        processUserInfo(data, dataObj);
                    }
                    break;
                    case PROCESSCODE_VIDEOS: {
                        processGetVideos(data, dataObj);
                    }
                    break;
                    default: {

                    }
                }
            }
            return data;
        }

        private void processUserInfo(HashMap<String, Object> data, Object dataObj) throws Exception {
            try {
                if (dataObj instanceof JSONObject) {
                    JSONObject objUser = (JSONObject) dataObj;

                    Iterator<String> keys = objUser.keys();

                    while (keys.hasNext()) {
                        String key = keys.next();
                        data.put(key, objUser.getString(key));
                    }
                }

            } catch (JSONException e) {
                Log.e(TAG, "Error processing user information from json content.");
                e.printStackTrace();
                throw new Exception("Error processing user information from json content.");
            }
            throw new Exception("Could not parse user information content.");
        }

        private void processAuthentication(HashMap<String, Object> data, Object dataObj) throws Exception {
            if (dataObj instanceof String && ((String) dataObj).length() > 0) {
                data.put("token", dataObj);
            } else {
                throw new Exception("Invalid token received");
            }
        }

        private void processGetImages(HashMap<String, Object> data, Object dataObj) throws Exception {

            if (dataObj instanceof JSONArray) {
                JSONArray jarray = (JSONArray) dataObj;
                ArrayList<String> urls = new ArrayList<>();
                for (int c = 0; c < jarray.length(); c++) {
                    try {
                        JSONObject o = (JSONObject) jarray.get(c);
                        urls.add(((String) o.get("url")).replace("..", getResources().getString(R.string.lms_httpUrl)));
                    } catch (JSONException e) {
                        Log.e(TAG, "Error processing images urls from json content.");
                        e.printStackTrace();
                        throw new Exception("Error processing images urls from json content.");
                    }
                }

                data.put("img_urls", urls);
            }
        }

        private void processGetStats(HashMap<String, Object> data, Object dataObj) throws Exception {
            //TODO

        }

        private void processGetTrails(HashMap<String, Object> data, Object dataObj) throws Exception {
            //TODO

        }

        private void processGetVideos(HashMap<String, Object> data, Object dataObj) throws Exception {
            //TODO

        }


    }

}
