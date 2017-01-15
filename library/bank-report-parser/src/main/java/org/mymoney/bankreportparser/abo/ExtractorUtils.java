package org.mymoney.bankreportparser.abo;

import org.mymoney.bankreportparser.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Michal Bocek
 * @since 09/01/2017
 */
public final class ExtractorUtils {

    private ExtractorUtils() {
        throw new UnsupportedOperationException();
    }

    public static String extractType(final String line) {
        return line.substring(0, 3);
    }

    public static String extractAccountNumber(final String line) {
        return line.substring(3, 18);
    }

    public static String extractAccountName(final String line) {
        return line.substring(19, 38).trim();
    }

    public static String extractSourceAccountNumber(final String line) {
        return line.substring(3, 18);
    }
    public static String extractTargetAccountNumber(final String line) {
        return line.substring(19, 34);
    }

    public static BigDecimal extractAmount(final String line) {
        String amount = line.substring(48, 59);
        return new BigDecimal(amount);
    }

    public static Transaction.Operation extractOperation(final String line) {
        String operation = line.substring(60, 61);
        switch (operation) {
            case "1":
                return Transaction.Operation.DEBET;
            case "2":
                return Transaction.Operation.CREDIT;
            case "4":
                return Transaction.Operation.STORNO_DEBET;
            case "5":
                return Transaction.Operation.STORNO_CREDIT;
            default:
                throw new IllegalArgumentException("Not define operation:" + operation);
        }
    }

    public static String extractBankCode(final String line) {
        return line.substring(73, 77);
    }

    public static String extractConstantSymbol(final String line) {
        return line.substring(71, 81);
    }

    public static String extractVariableSymbol(final String line) {
        return line.substring(61, 71);
    }

    public static String extractSpecificSymbol(final String line) {
        return line.substring(81, 91);
    }

    public static LocalDate extractValutationDate(final String line) {
        String date =  line.substring(91, 97);
        int day = Integer.valueOf(date.substring(0, 2));
        int month = Integer.valueOf(date.substring(2, 4));
        int year = Integer.valueOf(date.substring(4, 6));
        year += year < 50 ? 2000 : 1900;
        return LocalDate.of(year, month, day);
    }

    public static String extractExtraInfo(final String line) {
        return line.substring(97, 117);
    }
}
