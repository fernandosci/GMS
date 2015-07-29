package uk.ac.gla.dcs.gms.api.lms;


import uk.ac.gla.dcs.gms.api.Session;
import uk.ac.gla.dcs.gms.api.http.HTTPResponseListener;


public interface LMSSession extends Session{

    void getStats(HTTPResponseListener listener, int requestCode, String urlParameters);

    void getVideos(HTTPResponseListener listener, int requestCode, String urlParameters);

    void getImages(HTTPResponseListener listener, int requestCode, String urlParameters);

    void getTrails(HTTPResponseListener listener, int requestCode, String urlParameters);

}
