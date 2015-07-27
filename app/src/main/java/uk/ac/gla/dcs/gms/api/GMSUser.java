package uk.ac.gla.dcs.gms.api;

import android.content.Context;

import java.util.HashMap;

import uk.ac.gla.dcs.gms.utils.FileUtils;

public class GMSUser {
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
