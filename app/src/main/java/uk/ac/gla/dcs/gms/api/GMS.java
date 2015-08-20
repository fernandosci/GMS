package uk.ac.gla.dcs.gms.api;

import android.content.Context;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import uk.ac.gla.dcs.gms.api.http.HTTPProgressStatus;
import uk.ac.gla.dcs.gms.api.http.HTTPResponseListener;
import uk.ac.gla.dcs.gms.api.lms.LMSService;
import uk.ac.gla.dcs.gms.api.lms.LMSSession;

public class GMS implements ServiceProvider, AuthenticationProvider, RegistrationProvider {
    private static final String TAG = "GMS";
    private static final String SERVICENAME = "GMS local Service";
    private static final String FILE_USERINFO = "gms_userinfo";
    private static GMS instance = null;
    private static Context context;
    private final Map<String, ServiceProvider> services;
    private int authProvCount;
    private int regProvCount;
    private OnCredentialsRequiredListener credListener;


    public static void initialize(Context context){
        if (instance == null) {
            GMS.context = context.getApplicationContext();
            instance = new GMS();
        }
    }


    public static GMS getInstance() {
        if (instance == null) {
            throw new ExceptionInInitializerError();
        }
        return instance;
    }

    private GMS() {
        services = new HashMap<>();
        credListener = null;
        authProvCount = 0;
        regProvCount = 0;
        this.init();
    }

    public static Context getApplicationContext(){
        return context;
    }

    private void init() {
        //add services
        //lms
        LMSService lmsService = new LMSService(context);
        services.put(LMSService.class.getSimpleName(), lmsService);

        Collection<ServiceProvider> values = services.values();
        for (ServiceProvider srv : values){
            if (srv instanceof AuthenticationProvider)
                authProvCount++;
            if (srv instanceof RegistrationProvider)
                regProvCount++;
        }
    }

    @Override
    public String getServiceName() {
        return SERVICENAME;
    }

    @Override
    public boolean isServiceFullAvailable() throws GMSException {
        boolean result = true;

        for (ServiceProvider srv : services.values()) {
            result = result && srv.isServiceFullAvailable();
        }

        return result;
    }

    @Override
    public boolean isServicePartiallyAvailable() throws GMSException {
        boolean result = false;

        for (ServiceProvider srv : services.values()) {
            result = result || srv.isServiceFullAvailable();
        }

        return result;
    }

    @Override
    public void register(HTTPResponseListener httpResponseListener, int requestCode, Map<String, String> fields) throws GMSException {

        boolean checkResult = true;
        Set<String> requiredRegisterFields = getRequiredRegisterFields();
        for (String str : requiredRegisterFields) {
            if (!fields.containsKey(str)) {
                checkResult = false;
                break;
            }
        }

        if (checkResult) {
            MyHTTPResponseListener internalProxyResponseListener = new MyHTTPResponseListener(httpResponseListener, requestCode, regProvCount);

            for (ServiceProvider srv : services.values()) {

                if (srv instanceof RegistrationProvider){
                    RegistrationProvider regProv = ((RegistrationProvider) srv);
                    regProv.register(internalProxyResponseListener, srv.hashCode(), fields);
                }
            }

        } else {
            throw new GMSException("Missing fields.", null, -1);
        }

    }

    @Override
    public void loginWithCredentials(HTTPResponseListener httpResponseListener, int requestCode, CredentialAdapter credAdapter) throws GMSException {
        if (getSupportedProviders().contains(credAdapter.getProvider())) {

            MyHTTPResponseListener internalProxyResponseListener = new MyHTTPResponseListener(httpResponseListener, requestCode, authProvCount);

            for (ServiceProvider srv : services.values()) {

                if (srv instanceof AuthenticationProvider) {
                    AuthenticationProvider regProv = ((AuthenticationProvider) srv);
                    regProv.loginWithCredentials(internalProxyResponseListener, srv.hashCode(), credAdapter);
                }
            }
        } else
            throw new GMSException("Unsupported credential provider", null, -1);
    }


    @Override
    public void setOnCredentialsRequiredListener(OnCredentialsRequiredListener listener) throws GMSException {
        this.credListener = listener;

    }


    @Override
    public Set<String> getRequiredRegisterFields() {
        Set<String> result = new HashSet<>();

        for (ServiceProvider srv : services.values()) {
            if (srv instanceof RegistrationProvider)
                result.addAll(((RegistrationProvider) srv).getRequiredRegisterFields());
        }

        return result;
    }

    @Override
    public Set<String> getUserFields() {
        throw new UnsupportedOperationException("not supported yet");
    }

    @Override
    public boolean hasUserField(String field) {
        throw new UnsupportedOperationException("not supported yet");
    }

    @Override
    public String getUserValue(String field) {
        throw new UnsupportedOperationException("not supported yet");
    }

    @Override
    public boolean updateUserValue(String field, String value) throws GMSException {
        throw new UnsupportedOperationException("not supported yet");
    }

    @Override
    public boolean updateUserValues(Map<String, String> map) throws GMSException {
        throw new UnsupportedOperationException("not supported yet");

//        HashMap<ServiceProvider,GMSException> failedList = new HashMap<>();

//        for (String key : fields.keySet()){
//            if (userDetails.containsKey(key))
//                userDetails.put(key,fields.get(key));
//        }
//
//        for (ServiceProvider srv : services.values())
//        {
//            try{
//                srv.updateUserFields(fields);
//            } catch (GMSException e){
//                failedList.put(srv,e);
//            }
//        }

        //TODO implement rollback in case of failure

        //TODO sync issues

        //FIXME improve error message
//        if (failedList.size() > 0 && failedList.size() < services.size())
//            throw new GMSExceptionBuilder(null, context.getResources().getInteger(R.integer.gms_error_fieldUpdatePartialFailure), "partial failure" ).addExtra("failedList",failedList).build();
//        else if (failedList.size() == services.size())
//            throw new GMSExceptionBuilder(null, context.getResources().getInteger(R.integer.gms_error_fieldUpdateFailure), "full failure" ).addExtra("failedList",failedList).build();
//
//        return true;
    }


    public LMSSession getLMSSession() throws GMSException {
        LMSService lmsService = (LMSService) services.get(LMSService.class.getSimpleName());
        return lmsService.getLMSSession();
    }

    @Override
    public String toString() {
        return SERVICENAME;
    }


    @Override
    public void isLoggedIn(HTTPResponseListener httpResponseListener, int requestCode) throws GMSException {
        MyHTTPResponseListener internalProxyResponseListener = new MyHTTPResponseListener(httpResponseListener, requestCode, authProvCount);

        for (ServiceProvider srv : services.values()) {
            if (srv instanceof AuthenticationProvider) {
                AuthenticationProvider regProv = ((AuthenticationProvider) srv);
                regProv.isLoggedIn(internalProxyResponseListener, regProv.hashCode());
            }
        }
    }

    @Override
    public void getLoggedProviders(HTTPResponseListener httpResponseListener) throws GMSException {
        throw new UnsupportedOperationException("not supported yet");
    }

    @Override
    public Set<String> getSupportedProviders() {
        Set<String> result = new HashSet<>();
        boolean initial = true;
        for (ServiceProvider srv : services.values()) {
            if (srv instanceof AuthenticationProvider) {
                AuthenticationProvider regProv = ((AuthenticationProvider) srv);

                if (initial){
                    initial = false;
                    result.addAll(regProv.getSupportedProviders());
                }else
                    result.retainAll(regProv.getSupportedProviders());
            }
        }

        return result;
    }


    private class MyHTTPResponseListener implements HTTPResponseListener {

        private HTTPResponseListener externalResponseListener;
        private int externalRequestCode;
        private AtomicInteger responseCount;
        private int expectedResponses;
        private Boolean answered;

        public MyHTTPResponseListener(HTTPResponseListener externalResponseListener, int externalRequestCode, int expectedResponses) {
            this.externalResponseListener = externalResponseListener;
            this.externalRequestCode = externalRequestCode;
            this.expectedResponses = expectedResponses;
            this.answered = false;
            this.responseCount = new AtomicInteger(0);
        }

        @Override
        public void onResponse(int requestCode, boolean successful, HashMap<String, Object> data, Exception exception) {

            if ((!successful || responseCount.incrementAndGet() == expectedResponses)){
                //ensure only 1 response
                synchronized (answered) {
                    if (!answered){

                        externalResponseListener.onResponse(externalRequestCode,successful, data, exception);

                        answered = true;
                    }
                }
            }
        }

        @Override
        public void onProgress(int requestCode, HTTPProgressStatus progressStatus, HashMap<String, Object> newdata) {

        }
    }
}
