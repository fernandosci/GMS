package uk.ac.gla.dcs.gms.api.lms;

import java.util.Calendar;

import uk.ac.gla.dcs.gms.api.UrlParameterBuilder;


public class LMSImageRequestParamBuilder extends UrlParameterBuilder{

    public LMSImageRequestParamBuilder setInterval(Calendar from, Calendar to){

        setCalendarValues("from",from);

        setCalendarValues("to",to);

        return this;
    }

    public LMSImageRequestParamBuilder setUpload(){

        params.put("upload",String.valueOf(true));

        return this;
    }


    public LMSImageRequestParamBuilder setLimit(int limit){

        params.put("limit",String.valueOf(limit));

        return this;
    }

    public LMSImageRequestParamBuilder setSkip(int skip){

        params.put("skip",String.valueOf(skip));

        return this;
    }
    public LMSImageRequestParamBuilder setPersonal(boolean personal){
        params.put("personal",String.valueOf(personal));
        return this;
    }
    public LMSImageRequestParamBuilder setPosition(int radius,float lat, float log){
        params.put("radius",String.valueOf(radius));
        params.put("lat",String.valueOf(lat));
        params.put("log",String.valueOf(log));
        return this;
    }
    public LMSImageRequestParamBuilder setAllImages(){
        params.put("allImages",String.valueOf(true));
        return this;
    }
    public LMSImageRequestParamBuilder setKeyMoments(){
        params.put("keyMoments",String.valueOf(true));
        return this;
    }


}
