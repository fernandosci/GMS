package uk.ac.gla.dcs.gms.api;

import android.content.Context;
import android.content.res.Resources;

import java.io.Serializable;
import java.util.HashMap;

import uk.ac.gla.dcs.gms.lms.R;
import uk.ac.gla.dcs.gms.utils.FileUtils;

public class GMSUser implements Serializable{
    public static final String FIELD_USERNAME = Resources.getSystem().getString(R.string.FIELD_USERNAME);
    public static final String FIELD_PASSWORD = Resources.getSystem().getString(R.string.FIELD_PASSWORD);
    public static final String FIELD_EMAIL = Resources.getSystem().getString(R.string.FIELD_EMAIL);
    public static final String FIELD_ANSWER = Resources.getSystem().getString(R.string.FIELD_ANSWER);
    public static final String FIELD_QUESTION = Resources.getSystem().getString(R.string.FIELD_QUESTION);



    protected String service;

    protected HashMap<String, String> user;

    public GMSUser(String service) {
        this.service = service;
        user = new HashMap<>();
    }

    public static GMSUser fromFile(Context context, String filename) throws Exception {
        return (GMSUser)FileUtils.readObjFromFile(context,filename);
    }

    public void saveToFile(Context context, String filename) throws Exception {
        FileUtils.writeObjToFile(context, filename, this);
    }

    public HashMap<String, String> getUser() {
        return user;
    }
}
