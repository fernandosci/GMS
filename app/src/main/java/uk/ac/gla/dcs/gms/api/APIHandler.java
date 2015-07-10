package uk.ac.gla.dcs.gms.api;

import uk.ac.gla.dcs.gms.api.lms.LMSAuthenticationRequest;

/**
 * Created by ito on 29/06/2015.
 */
public class APIHandler {


    public static void login(LMSAuthenticationRequest loginRequest, String username, String password){
        loginRequest.execute(username,password);
    }

    public static void register(LMSAuthenticationRequest registerRequest, String username, String password, String email, String answer, String question){
        registerRequest.execute(username,password,email,answer,question);
    }

    //get

    //post



}
