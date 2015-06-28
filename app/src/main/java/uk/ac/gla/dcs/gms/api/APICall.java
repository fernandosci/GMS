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
    private Context context;

    public APICall(Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String urlString=params[0];
        String resultToDisplay;
        InputStream in = null;

        // HTTP Get
        try {
            String baseUrl = context.getResources().getString(R.string.gmsrest_url);
            URL url = new URL( baseUrl+"/"+urlString );
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());

            InputStreamReader isr = new InputStreamReader(in);

            String jsonString = IOUtils.toString(in, "UTF-8");

            Log.v(TAG, jsonString);
            
        } catch (Exception e ) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }

        return null;
    }
}
