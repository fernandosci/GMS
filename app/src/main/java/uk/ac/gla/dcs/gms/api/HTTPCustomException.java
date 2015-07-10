package uk.ac.gla.dcs.gms.api;

/**
 * Created by ito on 07/07/2015.
 */
public class HTTPCustomException extends Exception{
    private int statusCode;
    private APIHttpResponse response;

    public HTTPCustomException(APIHttpResponse response, int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.response = response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public APIHttpResponse getResponse() {
        return response;
    }
}
