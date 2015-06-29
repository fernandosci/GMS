package uk.ac.gla.dcs.gms.api;

import java.util.List;
import java.util.Map;

/**
 * Created by ito on 29/06/2015.
 */
public class APIResponse {

    private String response;
    private Map<String, List<String>> headerFields;
    private Exception exception;
    private boolean failed;

    public APIResponse(String response, Map<String, List<String>> headerFields, boolean failed, Exception exception) {
        this.response = response;
        this.headerFields = headerFields;
    }

    public String getResponse() {
        return response;
    }

    public Map<String, List<String>> getHeaderFields() {
        return headerFields;
    }

    public boolean isFailed() {
        return failed;
    }

    public Exception getException() {
        return exception;
    }
}
