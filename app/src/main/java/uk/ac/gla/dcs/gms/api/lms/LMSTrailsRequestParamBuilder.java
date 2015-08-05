package uk.ac.gla.dcs.gms.api.lms;

import java.util.Calendar;

import uk.ac.gla.dcs.gms.api.http.UrlParameterBuilder;

/**
 * Created by ito.
 */
public class LMSTrailsRequestParamBuilder extends UrlParameterBuilder {

    public LMSTrailsRequestParamBuilder setIsHeatmap(boolean heatmap){
        params.put("heatmap",String.valueOf(heatmap));
        return this;
    }

    public LMSTrailsRequestParamBuilder setIsPersonal(boolean personal){
        params.put("personal",String.valueOf(personal));
        return this;
    }

    public LMSTrailsRequestParamBuilder setInterval(Calendar from, Calendar to){

        setCalendarValues("from",from);

        setCalendarValues("to", to);

        return this;
    }
}
