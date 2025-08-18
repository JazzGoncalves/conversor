package com.jazz.conversor;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Map;
import java.util.Scanner;

public class app {

    private static final EnumSet<CurrencyCodes> SUPPORTED = EnumSet.of(
            CurrencyCodes.ARS, CurrencyCodes.BOB, CurrencyCodes.BRL,
            CurrencyCodes.CLP, CurrencyCodes.COP, CurrencyCodes.USD
    );

    public static void main(String[] args) {
        System.out.println("Conversor de Moedas iniciado.");

        Scanner sc = new Scanner(System.in);
        ExchangeRateClient client = new ExchangeRateClient();
        ConversionHistory history = new ConversionHistory();

        while (true) {
            System.out.println("\nMenu:");
            System.out.println(" 1) USD -> BRL");
            System.out.println(" 2) BRL -> USD");
            System.out.println(" 3) BRL -> ARS");
            System.out.println(" 4) ARS -> BRL");
            System.out.println(" 5) BRL -> CLP");
            System.out.println(" 6) CLP -> BRL");
            System.out.println(" 7) Conversão livre (entre as 6 moedas)");
            System.out.println(" 8) Histórico (sessão)");
            System.out.println(" 9) Sair");
            System.out.print("Selecione uma opção: ");

            String opt = sc.nextLine().trim();
            if (opt.equals("9")) {
                System.out.println("Encerrando. Até mais!");
                break;
            }

            String from = null, to = null;
            switch (opt) {
                case "1": from = "USD"; to = "BRL"; break;
                case "2": from = "BRL"; to = "USD"; break;
                case "3": from = "BRL"; to = "ARS"; break;
                case "4": from = "ARS"; to = "BRL"; break;
                case "5": from = "BRL"; to = "CLP"; break;
                case "6": from = "CLP"; to = "BRL"; break;
                case "7":
                    System.out.print("De (ARS, BOB, BRL, CLP, COP, USD): ");
                    from = sc.nextLine().trim().toUpperCase();
                    System.out.print("Para (ARS, BOB, BRL, CLP, COP, USD): ");
                    to = sc.nextLine().trim().toUpperCase();
                    if (!isSupported(from) || !isSupported(to)) {
                        System.out.println("Moeda não suportada.");
                        continue;
                    }
                    break;
                case "8":
                    history.printAll();
                    continue;
                default:
                    System.out.println("Opção inválida.");
                    continue;
            }

            Double amount = askAmount(sc);
            if (amount == null) continue;

            try {
                RatesResponse rates = client.fetchLatestRates(SUPPORTED);
                System.out.printf("Base: %s | Atualizado em: %s%n",
                        rates.getBase_code(), rates.getTime_last_update_utc());
                Map<String, Double> map = rates.getConversion_rates();
                CurrencyConverter converter = new CurrencyConverter(rates.getBase_code(), map);

                double result = converter.convert(amount, from, to);
                System.out.printf("Resultado: %.4f %s -> %.4f %s%n", amount, from, result, to);
                history.add(from, to, amount, result);

            } catch (IOException | InterruptedException e) {
                System.err.println("Erro ao consultar API: " + e.getMessage());
            } catch (IllegalArgumentException iae) {
                System.err.println("Erro de conversão: " + iae.getMessage());
            }
        }
        sc.close();
    }

    private static boolean isSupported(String code) {
        try {
            CurrencyCodes.valueOf(code);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static Double askAmount(Scanner sc) {
        System.out.print("Valor a converter: ");
        String s = sc.nextLine().trim().replace(",", ".");
        try {
            double v = Double.parseDouble(s);
            if (v < 0) {
                System.out.println("Informe um valor não negativo.");
                return null;
            }
            return v;
        } catch (NumberFormatException nfe) {
            System.out.println("Valor inválido.");
            return null;
        }
    }
}
