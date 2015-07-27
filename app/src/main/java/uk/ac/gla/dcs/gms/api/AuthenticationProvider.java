package uk.ac.gla.dcs.gms.api;

import java.util.Set;

import uk.ac.gla.dcs.gms.api.http.HTTPResponseCaller;

public interface AuthenticationProvider extends HTTPResponseCaller{

    void loginWithCredentials(CredentialContainer container) throws GMSException;

    void setOnCredentialsRequiredListener(OnCredentialsRequiredListener listener) throws GMSException;

    boolean isLoggedIn() throws GMSException;

    Set<String> getLoggedProviders() throws GMSException;

    Set<String> getSupportedProviders();
}
