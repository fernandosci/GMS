package uk.ac.gla.dcs.gms.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import uk.ac.gla.dcs.gms.lms.R;

/**
 * Created by ito on 29/06/2015.
 */
public abstract class LMSRegisterRequest extends AsyncTask<String, Integer, APIResponse> {

    private Context context;

    protected LMSRegisterRequest(Context context) {
        this.context = context;
    }

    @Override
    protected APIResponse doInBackground(String... params) {
        APIResponse response;

        try {
            URL url = new URL(context.getResources().getString(R.string.lms_httpUrl) + context.getResources().getString(R.string.lms_httpUrlRegister));

            StringBuilder builder = new StringBuilder();
            for(String str : params){
                builder.append(str + ":");
            }
            builder.deleteCharAt(builder.length()-1);

            byte[] authEncBytes = Base64.encode(builder.toString().getBytes(), Base64.DEFAULT);
            String authStringEnc = new String(authEncBytes);

            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

            InputStream in = urlConnection.getInputStream();
            response = new APIResponse(IOUtils.toString(in, "UTF-8"),urlConnection.getHeaderFields(),false,null);

        } catch (Exception e) {
            e.printStackTrace();
            response = new APIResponse(null,null,true,e);
        }

        return response;
    }
}
