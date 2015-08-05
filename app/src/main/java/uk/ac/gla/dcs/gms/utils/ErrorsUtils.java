package uk.ac.gla.dcs.gms.utils;

import android.content.Context;

import java.util.ArrayList;
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
            ArrayList<String> errors = (ArrayList<String>) data.get(context.getString(R.string.lms_api_StandardFieldErrorsKey));
            if (errors.size() > 0) {
                Utils.shortToast(context, errors.get(0));
                return;
            }
        }

        if (exception!= null) {
            exception.printStackTrace();
            Utils.shortToast(context, exception.getMessage());
        }
    }
}
