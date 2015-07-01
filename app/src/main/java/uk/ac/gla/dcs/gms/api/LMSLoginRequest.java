package uk.ac.gla.dcs.gms.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import uk.ac.gla.dcs.gms.lms.R;

/**
 * Created by ito on 29/06/2015.
 */
public abstract class LMSLoginRequest extends AsyncTask<String, Integer, APIHttpResponse> {

    private Context context;

    protected LMSLoginRequest(Context context) {
        this.context = context;
    }

    @Override
    protected APIHttpResponse doInBackground(String... params) {
        APIHttpResponse response;

        try {
            URL url = new URL(context.getResources().getString(R.string.lms_httpUrl) + context.getResources().getString(R.string.lms_httpUrlLogin));

            String authString = params[0] + ":" + params[1];

            byte[] authEncBytes = Base64.encode(authString.getBytes(), Base64.DEFAULT);
            String authStringEnc = new String(authEncBytes);

            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setConnectTimeout(2000);

            InputStream in = urlConnection.getInputStream();

            response = new APIHttpResponse(IOUtils.toString(in, "UTF-8"),urlConnection.getHeaderFields(),false,null);
        } catch (Exception e) {
            e.printStackTrace();
            response = new APIHttpResponse(null,null,true,e);
        }

        return response;
    }
}
