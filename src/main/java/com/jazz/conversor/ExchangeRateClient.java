package com.jazz.conversor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class ExchangeRateClient {

    private static final String DEFAULT_BASE = "USD";
    private final String apiKey;
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(8))
            .build();
    private final Gson gson = new Gson();

    public ExchangeRateClient() {
        this.apiKey = System.getenv("EXCHANGE_API_KEY");
        if (this.apiKey == null || this.apiKey.isBlank()) {
            throw new IllegalStateException("Defina EXCHANGE_API_KEY em Run → Edit Configurations…");
        }
    }

    public RatesResponse fetchLatestRates(EnumSet<CurrencyCodes> filter)
            throws IOException, InterruptedException {
        String url = String.format("https://v6.exchangerate-api.com/v6/%s/latest/%s",
                apiKey, DEFAULT_BASE);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> resp = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() < 200 || resp.statusCode() >= 300) {
            throw new IOException("HTTP " + resp.statusCode() + " - " + resp.body());
        }

        JsonObject root = JsonParser.parseString(resp.body()).getAsJsonObject();
        if (!root.has("conversion_rates")) {
            throw new IOException("Resposta inesperada: " + resp.body());
        }

        RatesResponse parsed = gson.fromJson(root, RatesResponse.class);
        if (!"success".equalsIgnoreCase(parsed.getResult())) {
            throw new IOException("API retornou erro: " + resp.body());
        }

        // filtra só as moedas do desafio
        Map<String, Double> filtered = new HashMap<>();
        for (CurrencyCodes c : filter) {
            Double v = parsed.getConversion_rates().get(c.name());
            if (v != null) filtered.put(c.name(), v);
        }
        parsed.getConversion_rates().clear();
        parsed.getConversion_rates().putAll(filtered);

        return parsed;
    }
}
