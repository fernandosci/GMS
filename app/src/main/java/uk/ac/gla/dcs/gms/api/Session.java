package uk.ac.gla.dcs.gms.api;

import java.io.Serializable;

public interface Session extends Serializable{
    boolean isExpired();
}
