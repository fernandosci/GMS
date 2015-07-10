package uk.ac.gla.dcs.gms.api.lms;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidParameterException;

import uk.ac.gla.dcs.gms.api.APIHttpResponse;
import uk.ac.gla.dcs.gms.api.HTTPCustomException;
import uk.ac.gla.dcs.gms.lms.R;

/**
 * Created by ito on 29/06/2015.
 */
public abstract class LMSAuthenticationRequest extends LMSRequest<String, Integer, APIHttpResponse> {

    private static final String TAG = "LMSLoginRequest";
    public static final int LOGIN = 0;
    public static final int REGISTER = 1;
    private int requestType;

    protected LMSAuthenticationRequest(Context context, int requestType) {
        super(context);
        this.requestType = requestType;
    }

    @Override
    protected APIHttpResponse doInBackground(String... params) {
        APIHttpResponse response;

        try {
            URL url;


            String authString;

            if (requestType == LOGIN) {
                url = new URL(context.getResources().getString(R.string.lms_httpUrl) + context.getResources().getString(R.string.lms_httpUrlLogin));
                authString = params[0] + ":" + params[1];
            } else if (requestType == REGISTER) {
                url = new URL(context.getResources().getString(R.string.lms_httpUrl) + context.getResources().getString(R.string.lms_httpUrlRegister));
                StringBuilder builder = new StringBuilder();
                for (String str : params) {
                    builder.append(str + ":");
                }
                builder.deleteCharAt(builder.length() - 1);
                authString = builder.toString();
            } else
                throw new InvalidParameterException("Invalid request type.");


            byte[] authEncBytes = Base64.encode(authString.getBytes(), Base64.DEFAULT);
            String authStringEnc = new String(authEncBytes);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setConnectTimeout(2000); //FIXME

            InputStream in = urlConnection.getInputStream();

            response = new APIHttpResponse(IOUtils.toString(in, "UTF-8"), urlConnection.getHeaderFields(), false, null);
        } catch (Exception e) {
            Log.e(TAG, "Login request error: " + e.getMessage());
            e.printStackTrace();
            response = new APIHttpResponse(null, null, true, e);
        }

        return response;
    }

    protected String getTokenFromResponse(APIHttpResponse response) throws HTTPCustomException {

        return (String) processResponse(response, HttpURLConnection.HTTP_OK);
    }

    @Override
    protected Object onSuccessfulProcessedResponse(APIHttpResponse response, int statusCode, Object data) throws HTTPCustomException {
        if (data instanceof String && !((String) data).isEmpty()) {
            return data;
        } else
            throw new HTTPCustomException(response, statusCode, "Invalid Token received.");
    }
}
