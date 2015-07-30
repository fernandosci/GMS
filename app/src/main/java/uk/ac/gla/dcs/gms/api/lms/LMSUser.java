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
        if (user.get("id") == null)
            return "";
        else
        return user.get("id");
    }

    public void setId(String id) {
        user.put("id",id);
    }

    public String getUsername() {
        if (user.get("username") == null)
            return "";
        else
            return user.get("username");
    }

    public void setUsername(String username) {
        user.put("username",username);
    }

    public String getEmail() {
        if (user.get("email") == null)
            return "";
        else
            return user.get("email");
    }

    public void setEmail(String email) {
        user.put("email",email);
    }

    public Boolean isUploadAsPrivate(){

        String uploadAsPrivate = user.get("uploadAsPrivate");
        if (uploadAsPrivate != null) {
            return Boolean.parseBoolean(uploadAsPrivate);
        }
       else
            return null;
    }

    public void setUploadAsPrivate(boolean uploadAsPrivate) {
        user.put("uploadAsPrivate", String.valueOf(uploadAsPrivate));
    }

    public String getToken() {
        if (user.get("token") == null)
            return "";
        else
            return user.get("token");
    }

    public void setToken(String token) {
        user.put("token",token);
    }

    public String getTokenUpdate() {
        if (user.get("tokenUpdate") == null)
            return "";
        else
            return user.get("tokenUpdate");
    }

    public void setTokenUpdate(String tokenUpdate) {
        user.put("tokenUpdate",tokenUpdate);
    }

    public String getQuestion() {
        if (user.get("question") == null)
            return "";
        else
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
