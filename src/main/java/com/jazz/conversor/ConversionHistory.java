package com.jazz.conversor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConversionHistory {
    public static class Entry {
        public final LocalDateTime at;
        public final String from;
        public final String to;
        public final double amount;
        public final double result;

        public Entry(LocalDateTime at, String from, String to, double amount, double result) {
            this.at = at; this.from = from; this.to = to;
            this.amount = amount; this.result = result;
        }

        @Override public String toString() {
            return String.format("[%s] %.4f %s -> %.4f %s", at, amount, from, result, to);
        }
    }

    private final List<Entry> entries = new ArrayList<>();
    public void add(String from, String to, double amount, double result) {
        entries.add(new Entry(LocalDateTime.now(), from, to, amount, result));
    }
    public void printAll() {
        if (entries.isEmpty()) System.out.println("Sem histórico de conversões nesta sessão.");
        else {
            System.out.println("=== Histórico de Conversões ===");
            entries.forEach(e -> System.out.println(" - " + e));
        }
    }
}
