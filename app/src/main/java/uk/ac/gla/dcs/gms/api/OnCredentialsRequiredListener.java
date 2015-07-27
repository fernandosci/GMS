package uk.ac.gla.dcs.gms.api;

import java.util.Set;

public interface OnCredentialsRequiredListener {

    /**
     * Service calls this method whenever it fails to complete a request by credentials errors.
     * @param service Service which requires credentials
     * @param supportedProviders providers supported by the service
     */
    void onCredentialsRequired(ServiceProvider service, Set<String> supportedProviders);
}
