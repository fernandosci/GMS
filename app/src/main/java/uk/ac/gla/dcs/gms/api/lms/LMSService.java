package uk.ac.gla.dcs.gms.api.lms;


import android.content.Context;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import uk.ac.gla.dcs.gms.api.AuthenticationProvider;
import uk.ac.gla.dcs.gms.api.CredentialAdapter;
import uk.ac.gla.dcs.gms.api.GMSException;
import uk.ac.gla.dcs.gms.api.GMSUser;
import uk.ac.gla.dcs.gms.api.OnCredentialsRequiredListener;
import uk.ac.gla.dcs.gms.api.RegistrationProvider;
import uk.ac.gla.dcs.gms.api.ServiceProvider;
import uk.ac.gla.dcs.gms.api.http.HTTPProgressStatus;
import uk.ac.gla.dcs.gms.api.http.HTTPResponseListener;
import uk.ac.gla.dcs.gms.lms.R;


public class LMSService implements ServiceProvider, AuthenticationProvider, RegistrationProvider {
    private static final String TAG = "LMSService";

    public static final String SERVICENAME = "LMS Service";

    public static final int OPERATION_REGISTER = 0;
    public static final int OPERATION_LOCAL = 1;
    public static final int OPERATION_FACEBOOK = 2;

    private static final String USERFILENAME = "lmsUser";
    private static final String[] supportedAuthProviders = {CredentialAdapter.PROVIDER_LOCAL};
    private static final String[] requiredRegisterFields = {GMSUser.FIELD_USERNAME, GMSUser.FIELD_PASSWORD, GMSUser.FIELD_EMAIL,  GMSUser.FIELD_ANSWER,  GMSUser.FIELD_QUESTION};

    private Context context;
    private LMSSessionImp lmsSession;
    private LocalCredentialsRenewHandler credentialsRenewHandler;
    private OnCredentialsRequiredListener credListener;

    private LMSUser lmsUser;


    public LMSService(Context context) {
        this.context = context.getApplicationContext();
        this.lmsSession = null;
        this.credentialsRenewHandler = null;
        this.credListener = null;

        try {
            lmsUser = (LMSUser) GMSUser.fromFile(context, USERFILENAME);
        } catch (Exception e) {
            lmsUser = new LMSUser();
        }
    }

    @Override
    public void register(HTTPResponseListener httpResponseListener, int requestCode, Map<String, String> fields) throws GMSException {
        //first, check for all fields

        boolean checkResult = true;
        for (String str : requiredRegisterFields) {
            if (!fields.containsKey(str)) {
                checkResult = false;
                break;
            }
        }

        if (checkResult) {
            getToken(httpResponseListener, OPERATION_REGISTER, fields);
        } else {
            throw new GMSException("Missing fields.", null, -1);
        }

    }

    @Override
    public void loginWithCredentials(HTTPResponseListener httpResponseListener, int requestCode, CredentialAdapter credAdapter) throws GMSException {

        if (credAdapter.getProvider().equals(CredentialAdapter.PROVIDER_LOCAL)) {
            if (credAdapter.getFields().contains(GMSUser.FIELD_PASSWORD) && credAdapter.getFields().contains(GMSUser.FIELD_PASSWORD))
                getToken(httpResponseListener, OPERATION_LOCAL, credAdapter.getMap());
            else
                throw new GMSException("Missing fields.", null, -1);
        } else {
            throw new UnsupportedOperationException("Provider not supported");
        }
    }

    private void getToken(HTTPResponseListener httpResponseListener, int operation, Map<String, String> data) {

        LMSSessionImp tmpSession = new LMSSessionImp(context);

        HTTPResponseListener proxyListener = new LocalProxyResponseListener(httpResponseListener);
        switch (operation) {
            case OPERATION_REGISTER: {
                tmpSession.register(proxyListener, OPERATION_REGISTER,
                        data.get(GMSUser.FIELD_USERNAME),
                        data.get(GMSUser.FIELD_PASSWORD),
                        data.get(GMSUser.FIELD_EMAIL),
                        data.get(GMSUser.FIELD_ANSWER),
                        data.get(GMSUser.FIELD_QUESTION));
            }
            break;
            case OPERATION_LOCAL: {
                tmpSession.login(proxyListener, OPERATION_LOCAL,
                        data.get(GMSUser.FIELD_USERNAME),
                        data.get(GMSUser.FIELD_PASSWORD));
            }
            break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public void setOnCredentialsRequiredListener(OnCredentialsRequiredListener listener) {
        this.credListener = listener;
    }

    @Override
    public void isLoggedIn(HTTPResponseListener httpResponseListener, int requestCode) {
        //FIXME think in a better way to do it, maybe check if token is valid. Although facebook app startup as logged even if there is no internet connection
        httpResponseListener.onResponse(requestCode, !lmsUser.getToken().isEmpty(), new HashMap<String, Object>(0), null);
    }

    @Override
    public void getLoggedProviders(HTTPResponseListener httpResponseListener) {
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
        return lmsUser.getUser().keySet();
    }

    @Override
    public boolean hasUserField(String field) {
        return lmsUser.getUser().containsKey(field);
    }

    @Override
    public String getUserValue(String field) {
        return lmsUser.getUser().get(field);
    }

    @Override
    public boolean updateUserValue(String field, String value) throws GMSException {
        HashMap<String, String> tmp = new HashMap<>();

        tmp.put(field, value);
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

    public LMSSession getLMSSession() throws GMSException {
        if (lmsUser.getToken().isEmpty()) {
            if (lmsSession == null) {
                //lazy session initialization
                credentialsRenewHandler = new LocalCredentialsRenewHandler();
                lmsSession = new LMSSessionImp(context,credentialsRenewHandler, lmsUser.getToken());
            }
            //todo verify if any other check is needed about the session validation
            return lmsSession;
        } else
            throw new GMSException("Not logged in", null, -1);
    }


    @Override
    public String toString() {
        return SERVICENAME;
    }

    protected void tokenExpired(LMSSessionImp sessionImp) {
        //TODO - try to renew token
        //TODO - if cant cal loncredentialsRequired

        if (credListener != null) {
            credListener.onCredentialsRequired(this, getSupportedProviders());
        }

    }


    private class LocalProxyResponseListener implements HTTPResponseListener {
        private HTTPResponseListener responseListener;

        public LocalProxyResponseListener(HTTPResponseListener responseListener) {
            this.responseListener = responseListener;
        }

        @Override
        public void onResponse(int requestCode, boolean successful, HashMap<String, Object> data, Exception exception) {

            if (requestCode == OPERATION_REGISTER || requestCode == OPERATION_LOCAL) {


                if (successful) {
                    lmsUser.setToken((String) data.get(context.getResources().getString(R.string.LMS_DATAKEY_TOKEN)));
                    try {
                        lmsUser.saveToFile(context, USERFILENAME);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else
                exception = new UnsupportedOperationException();

            if (responseListener != null)
                responseListener.onResponse(requestCode, successful, data, exception);
        }

        @Override
        public void onProgress(int requestCode, HTTPProgressStatus progressStatus, HashMap<String, Object> newdata) {
        }
    }

    private class LocalCredentialsRenewHandler implements OnCredentialsRequiredListener{

        @Override
        public void onCredentialsRequired(ServiceProvider service, Set<String> supportedProviders) {
            //TODO IMPLEMENT SESSION RENEW
        }
    }

}
