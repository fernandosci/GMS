package uk.ac.gla.dcs.gms.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import uk.ac.gla.dcs.gms.lms.R;

/**
 * Created by ito on 29/06/2015.
 */
public class APIHandler {


    public static void login(LMSLoginRequest loginRequest, String username, String password){
        loginRequest.execute(username,password);
    }

    public static void register(LMSRegisterRequest registerRequest, String username, String password, String email, String answer, String question){
        registerRequest.execute(username,password,email,answer,question);
    }

    //get

    //post



}
