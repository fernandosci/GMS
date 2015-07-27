package uk.ac.gla.dcs.gms.api;

import android.content.Context;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import uk.ac.gla.dcs.gms.api.http.HTTPResponseCaller;
import uk.ac.gla.dcs.gms.api.http.HTTPResponseListener;
import uk.ac.gla.dcs.gms.api.lms.LMSService;
import uk.ac.gla.dcs.gms.api.lms.LMSSession;
import uk.ac.gla.dcs.gms.lms.R;
import uk.ac.gla.dcs.gms.utils.FileUtils;

public class GMS implements ServiceProvider,AuthenticationProvider, RegistrationProvider {
    private static final String SERVICENAME = "GMS local Service";
    private static final String TAG = "GMS";
    private static final String FILE_USERINFO = "gms_userinfo";

    private static GMS instance = null;
    public static GMS getInstance(Context context) {
        if (instance == null) {
            instance = new GMS(context.getApplicationContext());
        }
        return instance;
    }

    private final Context context;

    private Map<String,ServiceProvider> services;

    private HTTPResponseListener responseListener;


    public GMS(Context context) {
        this.context = context.getApplicationContext();

        services = new HashMap<>();

        initialize();
    }

    private void initialize(){

        //add services
        //lms
        LMSService lmsService = new LMSService(context);
        services.put(LMSService.class.getSimpleName(), lmsService);
    }

    @Override
    public String getServiceName() {
        return SERVICENAME;
    }

    @Override
    public boolean isServiceFullAvailable() throws GMSException {
        boolean result = true;

        for (ServiceProvider srv : services.values())
        {
            result = result && srv.isServiceFullAvailable();
        }

        return result;
    }

    @Override
    public boolean isServicePartiallyAvailable() throws GMSException {
        boolean result = false;

        for (ServiceProvider srv : services.values())
        {
            result = result || srv.isServiceFullAvailable();
        }

        return result;
    }

    @Override
    public void loginWithCredentials(CredentialContainer container) throws GMSException {

    }

    @Override
    public void setOnCredentialsRequiredListener(OnCredentialsRequiredListener listener) throws GMSException {

    }

    @Override
    public void setOnHttpResponseListener(HTTPResponseListener listener) {

    }

    @Override
    public void login() throws GMSException {

        HashMap<ServiceProvider,GMSException> failedList = new HashMap<>();

        for (ServiceProvider srv : services.values())
        {
            try{
                srv.login();
            } catch (GMSException e){
                failedList.put(srv,e);
            }
        }

        //FIXME improve error message
        if (failedList.size() > 0 && failedList.size() < services.size())
            throw new GMSExceptionBuilder(null, context.getResources().getInteger(R.integer.gms_error_loginPartialFailure), "partialfailure" ).addExtra("failedList",failedList).build();
        else if (failedList.size() == services.size())
            throw new GMSExceptionBuilder(null, context.getResources().getInteger(R.integer.gms_error_loginFailure), "full failure" ).addExtra("failedList",failedList).build();
    }

    @Override
    public void register(Map<String, String> fields) throws GMSException {

        HashMap<ServiceProvider, GMSException> failedList = new HashMap<>();

        for (ServiceProvider srv : services.values()) {
            try {
                srv.register(fields);
            } catch (GMSException e) {
                failedList.put(srv, e);
            }
        }

        //FIXME improve error message
        if (failedList.size() > 0 && failedList.size() < services.size())
            throw new GMSExceptionBuilder(null, context.getResources().getInteger(R.integer.gms_error_loginPartialFailure), "partialfailure").addExtra("failedList", failedList).build();
        else if (failedList.size() == services.size())
            throw new GMSExceptionBuilder(null, context.getResources().getInteger(R.integer.gms_error_loginFailure), "full failure").addExtra("failedList", failedList).build();

        //TODO: copy fields from services to local GMS service
    }

    @Override
    public Set<String> getRequiredRegisterFields() {
        Set<String> result = new HashSet<>();

        for (ServiceProvider srv : services.values())
        {
            if (srv instanceof RegistrationProvider)
                result.addAll(((RegistrationProvider)srv).getRequiredRegisterFields());
        }

        return result;
    }

    @Override
    public Set<String> getUserFields() {
        throw  new UnsupportedOperationException("not supported yet");
    }

    @Override
    public boolean hasUserField(String field) {
        throw  new UnsupportedOperationException("not supported yet");
    }

    @Override
    public String getUserValue(String field) {
        throw  new UnsupportedOperationException("not supported yet");
    }

    @Override
    public boolean updateUserValue(String field, String value) throws GMSException {
        throw  new UnsupportedOperationException("not supported yet");
    }

    @Override
    public boolean updateUserValues(Map<String, String> map) throws GMSException {
        throw  new UnsupportedOperationException("not supported yet");

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
        LMSService lmsService = (LMSService)services.get(LMSService.class.getSimpleName());
        return lmsService.getLMSSession();
    }

    @Override
    public String toString() {
        return SERVICENAME;
    }



    @Override
    public boolean isLoggedIn() throws GMSException {
        throw  new UnsupportedOperationException("not supported yet");
    }

    @Override
    public Set<String> getLoggedProviders() throws GMSException {
        throw  new UnsupportedOperationException("not supported yet");
    }

    @Override
    public Set<String> getSupportedProviders() {
        throw  new UnsupportedOperationException("not supported yet");
    }


}
