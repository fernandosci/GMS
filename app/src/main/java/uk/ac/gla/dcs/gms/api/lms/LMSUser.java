package uk.ac.gla.dcs.gms.api.lms;

import uk.ac.gla.dcs.gms.api.GMSUser;

public class LMSUser extends GMSUser{

    public LMSUser() {
        super(LMSService.SERVICENAME);
    }


    public void fillUser(String id, String username, String email, boolean uploadAsPrivate, String token, String tokenUpdate, String question) {

        user.put("id",id);
        user.put("username",username);
        user.put("email",email);
        user.put("uploadAsPrivate", String.valueOf(uploadAsPrivate));
        user.put("token",token);
        user.put("tokenUpdate",tokenUpdate);
        user.put("question",question);
    }

    public String getId() {
        return user.get("id");
    }

    public void setId(String id) {
        user.put("id",id);
    }

    public String getUsername() {
        return user.get("username");
    }

    public void setUsername(String username) {
        user.put("username",username);
    }

    public String getEmail() {
        return user.get("email");
    }

    public void setEmail(String email) {
        user.put("email",email);
    }

    public boolean isUploadAsPrivate(){
        return Boolean.parseBoolean(user.get("uploadAsPrivate"));
    }

    public void setUploadAsPrivate(boolean uploadAsPrivate) {
        user.put("uploadAsPrivate", String.valueOf(uploadAsPrivate));
    }

    public String getToken() {
        return user.get("token");
    }

    public void setToken(String token) {
        user.put("token",token);
    }

    public String getTokenUpdate() {
        return user.get("tokenUpdate");
    }

    public void setTokenUpdate(String tokenUpdate) {
        user.put("tokenUpdate",tokenUpdate);
    }

    public String getQuestion() {
        return user.get("question");
    }

    public void setQuestion(String question) {
        user.put("question",question);
    }

    //    DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//    String string1 = "2001-07-04T12:08:56.235-0700";
//    Date result1 = df1.parse(string1);
//
//    DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
//    String string2 = "2001-07-04T12:08:56.235-07:00";
//    Date result2 = df2.parse(string2);
}
