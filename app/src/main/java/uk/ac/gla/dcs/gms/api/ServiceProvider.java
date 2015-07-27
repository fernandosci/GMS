package uk.ac.gla.dcs.gms.api;

public interface ServiceProvider{

    String getServiceName();

    boolean isServiceFullAvailable() throws GMSException;

    boolean isServicePartiallyAvailable() throws GMSException;
}
