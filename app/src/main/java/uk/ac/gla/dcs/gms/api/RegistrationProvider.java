package uk.ac.gla.dcs.gms.api;

import java.util.Map;
import java.util.Set;

import uk.ac.gla.dcs.gms.api.http.HTTPResponseListener;

/**
 * Created by ito on 23/07/2015.
 */
public interface RegistrationProvider{

    void register(HTTPResponseListener httpResponseListener, int requestCode, Map<String, String> fields) throws GMSException;

    Set<String> getRequiredRegisterFields();

    Set<String> getUserFields();

    boolean hasUserField(String field);

    String getUserValue(String field);

    boolean updateUserValue(String field, String value) throws GMSException;

    boolean updateUserValues(Map<String, String> map) throws GMSException;


}
