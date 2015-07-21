package uk.ac.gla.dcs.gms.api;

/**
 * Created by ito on 12/07/2015.
 */
public interface User {

    boolean hasField(String field);

    Object getField(String field);

    void setField(Object value);

    boolean register() throws RuntimeException;

    String[] getRequiredRegisterFields();

    boolean login(String password);

    boolean isLoggedIn();

    void logoff();
}
