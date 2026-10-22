package com.apexbank.util;

import java.text.DecimalFormat;

/**
 * Utility for Indian currency formatting and text rendering.
 */
public class CurrencyUtil {

    private static final DecimalFormat RUPEE_FORMAT = new DecimalFormat("##,##,##,##0.00");

    public static String format(double amount) {
        return "₹ " + RUPEE_FORMAT.format(amount);
    }

    public static String formatWithoutSymbol(double amount) {
        return RUPEE_FORMAT.format(amount);
    }

    private static final String[] UNITS = {
        "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
        "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"
    };

    private static final String[] TENS = {
        "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
    };

    /**
     * Converts a numeric rupee amount into formal words.
     */
    public static String convertToWords(double amount) {
        long rupees = (long) amount;
        int paise = (int) Math.round((amount - rupees) * 100);

        if (rupees == 0 && paise == 0) {
            return "Rupees Zero Only";
        }

        StringBuilder words = new StringBuilder("Rupees ");
        if (rupees > 0) {
            words.append(convertNumber(rupees));
        }

        if (paise > 0) {
            words.append(" and ").append(convertNumber(paise)).append(" Paise");
        }

        words.append(" Only");
        return words.toString();
    }

    private static String convertNumber(long n) {
        if (n < 0) return "Minus " + convertNumber(-n);
        if (n < 20) return UNITS[(int) n];
        if (n < 100) return TENS[(int) n / 10] + ((n % 10 != 0) ? " " : "") + UNITS[(int) n % 10];
        if (n < 1000) return UNITS[(int) n / 100] + " Hundred" + ((n % 100 != 0) ? " " : "") + convertNumber(n % 100);
        if (n < 100000) return convertNumber(n / 1000) + " Thousand" + ((n % 1000 != 0) ? " " : "") + convertNumber(n % 1000);
        if (n < 10000000) return convertNumber(n / 100000) + " Lakh" + ((n % 100000 != 0) ? " " : "") + convertNumber(n % 100000);
        return convertNumber(n / 10000000) + " Crore" + ((n % 10000000 != 0) ? " " : "") + convertNumber(n % 10000000);
    }
}
