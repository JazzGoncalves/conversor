package com.jazz.conversor;

import java.util.Map;

public class RatesResponse {
    private String result;
    private String base_code;              // ex.: "USD"
    private String time_last_update_utc;   // data/hora em texto
    private Map<String, Double> conversion_rates;

    public String getResult() { return result; }
    public String getBase_code() { return base_code; }
    public String getTime_last_update_utc() { return time_last_update_utc; }
    public Map<String, Double> getConversion_rates() { return conversion_rates; }
}
