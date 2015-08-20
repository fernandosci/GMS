package uk.ac.gla.dcs.gms.api.lms;

import android.content.Context;

import java.util.Calendar;

import uk.ac.gla.dcs.gms.api.http.UrlParameterBuilder;
import uk.ac.gla.dcs.gms.lms.R;

/**
 * Created by ito.
 */
public class LMSTrailsRequestParamBuilder extends UrlParameterBuilder {

    private Context context;

    public LMSTrailsRequestParamBuilder(Context context) {
        this.context = context;
    }


    public LMSTrailsRequestParamBuilder setIsHeatmap(boolean heatmap){
        params.put(context.getString(R.string.LMS_URLPARAM_HEATMAP),String.valueOf(heatmap));
        return this;
    }

    public LMSTrailsRequestParamBuilder setIsPersonal(boolean personal){
        params.put(context.getString(R.string.LMS_URLPARAM_PERSONAL),String.valueOf(personal));
        return this;
    }

    public LMSTrailsRequestParamBuilder setInterval(Calendar from, Calendar to){

        setCalendarValues(context.getString(R.string.LMS_URLPARAM_FROM),from);

        setCalendarValues(context.getString(R.string.LMS_URLPARAM_TO), to);

        return this;
    }
}
