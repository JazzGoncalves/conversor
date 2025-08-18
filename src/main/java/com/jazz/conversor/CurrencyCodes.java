package com.jazz.conversor;

public enum CurrencyCodes {
    ARS("Peso argentino"),
    BOB("Boliviano boliviano"),
    BRL("Real brasileiro"),
    CLP("Peso chileno"),
    COP("Peso colombiano"),
    USD("Dólar americano");

    private final String label;
    CurrencyCodes(String label) { this.label = label; }
    public String label() { return label; }
}
