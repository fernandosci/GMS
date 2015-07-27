package uk.ac.gla.dcs.gms.api;

import java.util.HashMap;


public class GMSExceptionBuilder {

    HashMap<String, Object> extras = null;

    private Exception exception;

    private int gmsAPIErrorCode;

    private String message;

    public GMSExceptionBuilder(Exception exception, int gmsAPIErrorCode, String message) {
        this.exception = exception;
        this.gmsAPIErrorCode = gmsAPIErrorCode;
        this.message = message;
    }

    public GMSExceptionBuilder addExtra(String field, Object obj){
        if (extras == null)
            extras = new HashMap<>();

        extras.put(field,obj);

        return this;
    }

    public GMSException build(){
        if (extras.size() == 0)
            return new GMSException(message,exception,gmsAPIErrorCode);
        else
            return new GMSException(message,exception,gmsAPIErrorCode,extras);
    }
}
