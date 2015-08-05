package uk.ac.gla.dcs.gms.api.http;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class UrlParameterBuilder {
    protected Map<String,String> params;


    public UrlParameterBuilder() {
        params = new LinkedHashMap<>();
    }

    public UrlParameterBuilder setCustomParameter(String field, String value){
        params.put(field,String.valueOf(value));

        return this;
    }

    protected void setCalendarValues(String prefix, Calendar cal){
        params.put(prefix + "_year", String.valueOf(cal.get(Calendar.YEAR)));
        params.put(prefix + "_month", String.valueOf(cal.get(Calendar.MONTH)));
        params.put(prefix + "_day", String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        params.put(prefix + "_hour", String.valueOf(cal.get(Calendar.HOUR)));
        params.put(prefix + "_min", String.valueOf(cal.get(Calendar.MINUTE)));
    }

    @Override
    public String toString() {

        Set<String> keys = params.keySet();

        StringBuilder result = new StringBuilder();

        result.append("?");

        for (String key : keys)
        {
            result.append(key);
            result.append("=");
            result.append(params.get(key));
            result.append("&");
        }

        result.deleteCharAt(result.length() -1);

        return result.toString();
    }
}
