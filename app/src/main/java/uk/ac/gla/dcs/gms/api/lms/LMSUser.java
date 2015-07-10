package uk.ac.gla.dcs.gms.api.lms;

/**
 * Created by ito on 08/07/2015.
 */
public class LMSUser {
    private static LMSUser ourInstance = null;

    public static LMSUser getInstance() {
        if (ourInstance == null)
            ourInstance = new LMSUser();
        return ourInstance;
    }

    private String id;;
    private String username;
    private String email;
    private boolean uploadAsPrivate;
    private String token;
    private String tokenUpdate;
    private String question;

    private LMSUser() {
    }

    public void fillUser(String id, String username, String email, boolean uploadAsPrivate, String token, String tokenUpdate, String question) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.uploadAsPrivate = uploadAsPrivate;
        this.token = token;
        this.tokenUpdate = tokenUpdate;
        this.question = question;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isUploadAsPrivate() {
        return uploadAsPrivate;
    }

    public void setUploadAsPrivate(boolean uploadAsPrivate) {
        this.uploadAsPrivate = uploadAsPrivate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenUpdate() {
        return tokenUpdate;
    }

    public void setTokenUpdate(String tokenUpdate) {
        this.tokenUpdate = tokenUpdate;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    //    DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//    String string1 = "2001-07-04T12:08:56.235-0700";
//    Date result1 = df1.parse(string1);
//
//    DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
//    String string2 = "2001-07-04T12:08:56.235-07:00";
//    Date result2 = df2.parse(string2);
}
