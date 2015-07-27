package uk.ac.gla.dcs.gms.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class CredentialContainer {
    private String provider;

    private Map<String,String> info;

    public CredentialContainer(String provider) {
        this.provider = provider;
        info = new HashMap<>();
    }

    public CredentialContainer setEntry(String field, String value)
    {
        info.put(field,value);
        return this;
    }

    public String getProvider() {
        return provider;
    }

    public Set<String> getFields(){
        return info.keySet();
    }

    public String getValue(String field){
        return info.get(field);
    }

    public Map<String, String> getInfo() {
        return info;
    }
}
