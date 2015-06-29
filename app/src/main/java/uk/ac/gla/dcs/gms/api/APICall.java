package uk.ac.gla.dcs.gms.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import uk.ac.gla.dcs.gms.lms.R;

/**
 * Created by ito on 28/06/2015.
 */
public abstract class APICall extends AsyncTask<String, Integer, String>{

    private static final String TAG = "APICall";
    private Exception exception;
    private boolean succeed;

    public APICall(){
        exception = null;
        succeed = true;
    }

    @Override
    protected String doInBackground(String... params) {
        String baseUrl=params[0];
        String urlString=params[1];
        String resultToDisplay;
        InputStream in = null;

        // HTTP Get
        try {
            URL url = new URL( baseUrl + "/" + urlString );
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());

            String jsonString = IOUtils.toString(in, "UTF-8");

            Log.v(TAG, jsonString);

            return jsonString;
            
        } catch (Exception e ) {
            exception = e;
            succeed = false;
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public Exception getException() {
        return exception;
    }

    public boolean isSucceed() {
        return succeed;
    }
}
