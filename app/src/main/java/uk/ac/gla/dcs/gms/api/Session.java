package uk.ac.gla.dcs.gms.api;

import uk.ac.gla.dcs.gms.api.http.HTTPResponseCaller;


public interface Session extends HTTPResponseCaller {
    boolean isExpired();
}
