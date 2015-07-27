package uk.ac.gla.dcs.gms.api;

import java.util.HashMap;
import java.util.Map;


public class GMSException extends Exception{

    private Exception exception;

    private int gmsAPIErrorCode;

    private Map<String,Object> extras;

    public GMSException(String detailMessage, Exception exception, int gmsAPIErrorCode) {
        super(detailMessage);
        this.exception = exception;
        this.gmsAPIErrorCode = gmsAPIErrorCode;
        this.extras = null;
    }

    public GMSException(String detailMessage, Exception exception, int gmsAPIErrorCode, Map<String, Object> extras) {
        super(detailMessage);
        this.exception = exception;
        this.gmsAPIErrorCode = gmsAPIErrorCode;
        this.extras = extras;
    }

    public Exception getException() {
        return exception;
    }

    public int getGmsAPIErrorCode() {
        return gmsAPIErrorCode;
    }

    public Map<String, Object> getExtras() {
        return new HashMap<>(extras);
    }

    public boolean hasExtras(){
        return extras != null;
    }
}
