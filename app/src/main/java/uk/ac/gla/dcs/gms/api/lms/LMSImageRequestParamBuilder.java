package uk.ac.gla.dcs.gms.api.lms;

import android.content.Context;

import java.util.Calendar;

import uk.ac.gla.dcs.gms.api.http.UrlParameterBuilder;
import uk.ac.gla.dcs.gms.lms.R;


public class LMSImageRequestParamBuilder extends UrlParameterBuilder{

    private Context context;

    public LMSImageRequestParamBuilder(Context context) {
        this.context = context;
    }

    public LMSImageRequestParamBuilder setInterval(Calendar from, Calendar to){

        setCalendarValues(context.getString(R.string.LMS_URLPARAM_FROM), from);
        setCalendarValues(context.getString(R.string.LMS_URLPARAM_TO),to);

        return this;
    }

    public LMSImageRequestParamBuilder setUpload(){

        params.put(context.getString(R.string.LMS_URLPARAM_UPLOAD),String.valueOf(true));

        return this;
    }


    public LMSImageRequestParamBuilder setLimit(int limit){

        params.put(context.getString(R.string.LMS_URLPARAM_LIMIT),String.valueOf(limit));

        return this;
    }

    public LMSImageRequestParamBuilder setSkip(int skip){

        params.put(context.getString(R.string.LMS_URLPARAM_SKIP),String.valueOf(skip));

        return this;
    }
    public LMSImageRequestParamBuilder setPersonal(boolean personal){
        params.put(context.getString(R.string.LMS_URLPARAM_PERSONAL),String.valueOf(personal));
        return this;
    }
    public LMSImageRequestParamBuilder setPosition(int radius,float lat, float log){
        params.put(context.getString(R.string.LMS_URLPARAM_RADIUS),String.valueOf(radius));
        params.put(context.getString(R.string.LMS_URLPARAM_LAT),String.valueOf(lat));
        params.put(context.getString(R.string.LMS_URLPARAM_LOG),String.valueOf(log));
        return this;
    }
    public LMSImageRequestParamBuilder setAllImages(){
        params.put(context.getString(R.string.LMS_URLPARAM_ALLIMAGES),String.valueOf(true));
        return this;
    }
    public LMSImageRequestParamBuilder setKeyMoments(){
        params.put(context.getString(R.string.LMS_URLPARAM_KEYMOMENTS),String.valueOf(true));
        return this;
    }


}
