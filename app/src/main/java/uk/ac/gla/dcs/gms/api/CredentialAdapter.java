package uk.ac.gla.dcs.gms.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import uk.ac.gla.dcs.gms.lms.R;


public class CredentialAdapter {

    public static final String PROVIDER_LOCAL = "local";

    private String provider;

    private Map<String,String> info;

    public static final CredentialAdapter getLocalCredentialAdapter(String username, String password){
        CredentialAdapter credentialAdapter = new CredentialAdapter(PROVIDER_LOCAL);

        credentialAdapter.info.put(GMS.getApplicationContext().getString(R.string.FIELD_USERNAME),username);
        credentialAdapter.info.put(GMS.getApplicationContext().getString(R.string.FIELD_PASSWORD),password);

        return credentialAdapter;
    }



    private CredentialAdapter(String provider) {
        this.provider = provider;
        info = new HashMap<>();
    }

    public CredentialAdapter setEntry(String field, String value)
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

    public Map<String, String> getMap() {
        //warning: might need to copy  instance to protect local map
        return info;
    }
}
