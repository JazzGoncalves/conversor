package com.jazz.conversor;

import java.util.Map;

public class CurrencyConverter {
    private final String base;
    private final Map<String, Double> rates;

    public CurrencyConverter(String base, Map<String, Double> rates) {
        this.base = base;
        this.rates = rates;
        this.rates.putIfAbsent(base, 1.0);
    }

    public double convert(double amount, String from, String to) {
        if (from.equalsIgnoreCase(to)) return amount;
        double rateFrom = getRate(from);
        double rateTo   = getRate(to);
        double inBase = amount / rateFrom;   // from -> base
        return inBase * rateTo;              // base -> to
    }

    private double getRate(String code) {
        Double r = rates.get(code.toUpperCase());
        if (r == null) {
            if (code.equalsIgnoreCase(base)) return 1.0;
            throw new IllegalArgumentException("Taxa não disponível para: " + code);
        }
        return r;
    }
}
