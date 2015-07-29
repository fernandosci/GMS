package uk.ac.gla.dcs.gms.api;

import java.util.Set;

import uk.ac.gla.dcs.gms.api.http.HTTPResponseListener;

public interface AuthenticationProvider{

    void loginWithCredentials(HTTPResponseListener httpResponseListener, int requestCode, CredentialAdapter credAdapter) throws GMSException;

    void setOnCredentialsRequiredListener(OnCredentialsRequiredListener listener) throws GMSException;

    void isLoggedIn(HTTPResponseListener httpResponseListener, int requestCode) throws GMSException;

    void getLoggedProviders(HTTPResponseListener httpResponseListener) throws GMSException;

    Set<String> getSupportedProviders();
}
