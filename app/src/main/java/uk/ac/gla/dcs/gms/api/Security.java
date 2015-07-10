package uk.ac.gla.dcs.gms.api;

import android.content.Context;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by ito on 02/07/2015.
 */
public class Security {

    private static final String TAG = "Security";
    private static final String FILENAME = "policy";

    public static void initialize(Context context) throws SecurityException {

        if (false)
        {
            Log.e(TAG,"Failed to initialize security.");
        }
    }

    public static void setToken(Context context, String token) throws SecurityException {
        saveToken(context,token);
    }

    public static String getToken(Context context) throws SecurityException {

        return loadToken(context);
    }

    public static void removeToken(Context context)
    {
        File f = new File(context.getFilesDir().getAbsolutePath(),FILENAME);
        if (f.exists())
            f.delete();
    }
    public static boolean hasToken(Context context) {
        File f = new File(context.getFilesDir().getAbsolutePath(),FILENAME);
        return f.exists();
    }

    private static void saveToken(Context context, String token) throws SecurityException{
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fileOutputStream.write(token.getBytes());
            fileOutputStream.close();
            Log.v(TAG, "Token saved in local private storage");
        } catch (java.io.IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error when saving token in local private storage");
            throw new SecurityException("Failed to write on local private storage.");
        }
    }

    private static String loadToken(Context context) throws SecurityException{
        if (hasToken(context)) {
            try {
                FileInputStream fileInputStream = context.openFileInput(FILENAME);
                String result = IOUtils.toString(fileInputStream);
                Log.v(TAG, "Token loaded from local private storage");
                return result;
            } catch (java.io.IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Error when loading token from local private storage");
                throw new SecurityException("Failed to read from local private storage.");
            }
        }
        else
            throw new SecurityException("No token found.");
    }


}
