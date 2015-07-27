package uk.ac.gla.dcs.gms.api.lms;


import uk.ac.gla.dcs.gms.api.Session;


public interface LMSSession extends Session{

    void getStats(int requestCode,String urlParameters);

    void getVideos(int requestCode,String urlParameters);

    void getImages(int requestCode,String urlParameters);

    void getTrails(int requestCode,String urlParameters);

}
