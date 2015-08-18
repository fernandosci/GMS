package uk.ac.gla.dcs.gms.api.lms;

import uk.ac.gla.dcs.gms.api.GMSUser;

public class LMSUser extends GMSUser{

    public static final String FIELD_ID = "id";
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_UPLOADASPRIVATE = "uploadAsPrivate";
    public static final String FIELD_TOKEN = "token";
    public static final String FIELD_TOKENUPDATE = "tokenUpdate";
    public static final String FIELD_QUESTION = "question";

    public LMSUser() {
        super(LMSService.SERVICENAME);
    }


    public void fillUser(String id, String username, String email, boolean uploadAsPrivate, String token, String tokenUpdate, String question) {

        user.put(FIELD_ID,id);
        user.put(FIELD_USERNAME,username);
        user.put(FIELD_EMAIL,email);
        user.put(FIELD_UPLOADASPRIVATE, String.valueOf(uploadAsPrivate));
        user.put(FIELD_TOKEN,token);
        user.put(FIELD_TOKENUPDATE,tokenUpdate);
        user.put(FIELD_QUESTION,question);
    }

    public String getId() {
        if (user.get(FIELD_ID) == null)
            return "";
        else
        return user.get(FIELD_ID);
    }

    public void setId(String id) {
        user.put(FIELD_ID,id);
    }

    public String getUsername() {
        if (user.get(FIELD_USERNAME) == null)
            return "";
        else
            return user.get(FIELD_USERNAME);
    }

    public void setUsername(String username) {
        user.put(FIELD_USERNAME,username);
    }

    public String getEmail() {
        if (user.get(FIELD_EMAIL) == null)
            return "";
        else
            return user.get(FIELD_EMAIL);
    }

    public void setEmail(String email) {
        user.put(FIELD_EMAIL,email);
    }

    public Boolean isUploadAsPrivate(){

        String uploadAsPrivate = user.get(FIELD_UPLOADASPRIVATE);
        if (uploadAsPrivate != null) {
            return Boolean.parseBoolean(uploadAsPrivate);
        }
       else
            return null;
    }

    public void setUploadAsPrivate(boolean uploadAsPrivate) {
        user.put(FIELD_UPLOADASPRIVATE, String.valueOf(uploadAsPrivate));
    }

    public String getToken() {
        if (user.get(FIELD_TOKEN) == null)
            return "";
        else
            return user.get(FIELD_TOKEN);
    }

    public void setToken(String token) {
        user.put(FIELD_TOKEN,token);
    }

    public String getTokenUpdate() {
        if (user.get(FIELD_TOKENUPDATE) == null)
            return "";
        else
            return user.get(FIELD_TOKENUPDATE);
    }

    public void setTokenUpdate(String tokenUpdate) {
        user.put(FIELD_TOKENUPDATE,tokenUpdate);
    }

    public String getQuestion() {
        if (user.get(FIELD_QUESTION) == null)
            return "";
        else
            return user.get(FIELD_QUESTION);
    }

    public void setQuestion(String question) {
        user.put(FIELD_QUESTION,question);
    }

    //    DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//    String string1 = "2001-07-04T12:08:56.235-0700";
//    Date result1 = df1.parse(string1);
//
//    DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
//    String string2 = "2001-07-04T12:08:56.235-07:00";
//    Date result2 = df2.parse(string2);
}
