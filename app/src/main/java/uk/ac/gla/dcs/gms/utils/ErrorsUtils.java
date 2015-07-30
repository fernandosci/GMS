package uk.ac.gla.dcs.gms.utils;

import android.content.Context;

import java.util.HashMap;

import uk.ac.gla.dcs.gms.Utils;
import uk.ac.gla.dcs.gms.lms.R;

/**
 * Created by ito.
 */
public class ErrorsUtils {

    public static void processHttpResponseError(Context context, HashMap<String, Object> data, Exception exception){

        if (data.containsKey(context.getString(R.string.lms_api_StandardFieldErrorsKey)))
        {
            String[] errors = (String[]) data.get(context.getString(R.string.lms_api_StandardFieldErrorsKey));

            Utils.shortToast(context, errors[0]);
        }else if (exception!= null) {
            exception.printStackTrace();

            Utils.shortToast(context, exception.getMessage());
        }
    }
}
