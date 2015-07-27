package uk.ac.gla.dcs.gms.api.lms;


import android.content.Context;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import uk.ac.gla.dcs.gms.api.AuthenticationProvider;
import uk.ac.gla.dcs.gms.api.CredentialContainer;
import uk.ac.gla.dcs.gms.api.GMSException;
import uk.ac.gla.dcs.gms.api.GMSUser;
import uk.ac.gla.dcs.gms.api.OnCredentialsRequiredListener;
import uk.ac.gla.dcs.gms.api.RegistrationProvider;
import uk.ac.gla.dcs.gms.api.ServiceProvider;
import uk.ac.gla.dcs.gms.api.http.HTTPProgressStatus;
import uk.ac.gla.dcs.gms.api.http.HTTPResponseListener;


public class LMSService implements ServiceProvider, AuthenticationProvider, RegistrationProvider {

    private static final String TAG = "LMSService";

    public static final String SERVICENAME = "LMS Service";
    private static final String USERFILENAME = "lmsUser";

    public static final int OPERATION_REGISTER = 0;
    public static final int OPERATION_LOCAL = 1;
    public static final int OPERATION_FACEBOOK = 2;

    private static final String[] supportedAuthProviders = { "local"};
    private static final String[] requiredRegisterFields = { "username", "password", "email", "answer", "question"};

    private Context context;
    private OnCredentialsRequiredListener credListener;
    private HTTPResponseListener responseListener;

    private GMSUser gmsUser;


    public LMSService(Context context) {
        this.context = context.getApplicationContext();
        this.credListener = null;
        this.responseListener = null;

        try {
            gmsUser = GMSUser.fromFile(context, USERFILENAME);
        } catch (Exception e) {
           gmsUser = new GMSUser(SERVICENAME);
        }
    }

    @Override
    public void register(Map<String, String> fields) throws GMSException {
        //first, check for all fields

        boolean checkResult = true;
        for (String str : requiredRegisterFields){
            if (!fields.containsKey(str)){
                checkResult = false;
                break;
            }
        }

        if (checkResult){
            getToken(OPERATION_REGISTER,fields);
        }else{
            throw new GMSException("Missing fields.",null,-1);
        }

    }

    @Override
    public void loginWithCredentials(CredentialContainer container) throws GMSException {

        if (container.getProvider() == "local") {
            if (container.getFields().contains("username") && container.getFields().contains("password"))
                getToken(OPERATION_LOCAL, container.getInfo());
            else
                throw new GMSException("Missing fields.", null, -1);
        }else{
            throw  new UnsupportedOperationException("not supported yet");
        }
    }

    private void getToken(int operation, Map<String, String> data){

        LMSSessionImp tmpSession = new LMSSessionImp(context,this);
        tmpSession.setOnHttpResponseListener(new HTTPResponseListener() {
            @Override
            public void onResponse(int requestCode, boolean successful, HashMap<String, Object> data, Exception exception) {

                if (requestCode == OPERATION_REGISTER || requestCode == OPERATION_LOCAL) {

                    if (successful) {
                        gmsUser.getUser().put("token", data.get("token").toString());
                    }
                }else
                    exception =  new UnsupportedOperationException();

                if (responseListener != null)
                    responseListener.onResponse(requestCode, successful, data, exception);
            }

            @Override
            public void onProgress(int requestCode, HTTPProgressStatus progressStatus, HashMap<String, Object> newdata) {
            }
        });


        switch (operation){
            case OPERATION_REGISTER:{
                tmpSession.register(OPERATION_REGISTER, data.get("username"), data.get("password"), data.get("email"), data.get("answer"), data.get("question"));
            }break;
            case OPERATION_LOCAL: {
                tmpSession.login(OPERATION_LOCAL, data.get("username"), data.get("password"));
            }break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public void setOnCredentialsRequiredListener(OnCredentialsRequiredListener listener) {
        this.credListener = listener;
    }

    @Override
    public void setOnHttpResponseListener(HTTPResponseListener listener) {
        this.setOnHttpResponseListener(listener);
    }

    @Override
    public boolean isLoggedIn() {
        //FIXME think in a better way to do it

        return gmsUser.getUser().containsKey("token");
    }

    @Override
    public Set<String> getLoggedProviders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> getSupportedProviders() {
        return new HashSet<String>(Arrays.asList(supportedAuthProviders));
    }

    @Override
    public Set<String> getRequiredRegisterFields() {
        return new HashSet<String>(Arrays.asList(requiredRegisterFields));
    }

    @Override
    public Set<String> getUserFields() {
        return gmsUser.getUser().keySet();
    }

    @Override
    public boolean hasUserField(String field) {
        return gmsUser.getUser().containsKey(field);
    }

    @Override
    public String getUserValue(String field) {
        return gmsUser.getUser().get(field);
    }

    @Override
    public boolean updateUserValue(String field, String value) throws GMSException {
        HashMap<String, String> tmp = new HashMap<>();

        tmp.put(field,value);
        return updateUserValues(tmp);
    }

    @Override
    public boolean updateUserValues(Map<String, String> map) throws GMSException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getServiceName() {
        return SERVICENAME;
    }

    @Override
    public boolean isServiceFullAvailable() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isServicePartiallyAvailable() {
        throw new UnsupportedOperationException();
    }

    public LMSSession getLMSSession() throws GMSException{
        if (isLoggedIn()) {
            return new LMSSessionImp(context, this, gmsUser.getUser().get("token"));
        }else
            throw new GMSException("Not logged in", null, -1);
    }


    @Override
    public String toString() {
        return SERVICENAME;
    }
}
