package uk.ac.gla.dcs.gms.api.lms;

import android.content.Context;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import uk.ac.gla.dcs.gms.api.APIHttpResponse;
import uk.ac.gla.dcs.gms.api.HTTPCustomException;
import uk.ac.gla.dcs.gms.api.Security;
import uk.ac.gla.dcs.gms.lms.R;

/**
 * Created by ito on 08/07/2015.
 */
public abstract class LMSUserRequest extends LMSRequest<String, Integer, APIHttpResponse> {


    public LMSUserRequest(Context context) {
        super(context);
    }

    @Override
    protected APIHttpResponse doInBackground(String... params) {
        APIHttpResponse response;

        try {
            URL url = new URL(context.getResources().getString(R.string.lms_httpUrl) + context.getResources().getString(R.string.lms_endpointUser));


            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Cookie", "token=" + Security.getToken(context));
            //urlConnection.setConnectTimeout(2000); //FIXME

            InputStream in = urlConnection.getInputStream();
            response = new APIHttpResponse(IOUtils.toString(in, "UTF-8"), urlConnection.getHeaderFields(), false, null);

        } catch (Exception e) {
            e.printStackTrace();
            response = new APIHttpResponse(null, null, true, e);
        }

        return response;
    }

    public void saveUserInfo(APIHttpResponse response) throws HTTPCustomException {

        JSONObject objUser = (JSONObject)processResponse(response, HttpURLConnection.HTTP_OK);

        LMSUser user = LMSUser.getInstance();

        try {
            user.setEmail(objUser.getString("email"));
            user.setId(objUser.getString("id"));
            user.setQuestion(objUser.getString("question"));
            user.setToken(objUser.getString("token"));
            user.setTokenUpdate(objUser.getString("tokenupdate"));
            user.setUploadAsPrivate(objUser.getBoolean("uploadAsPrivate"));
            user.setUsername(objUser.getString("username"));
        } catch (JSONException e) {
            e.printStackTrace();        //FIXME: handle exception
        }
    }
}
