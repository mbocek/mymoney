package org.mymoney.bankreportparser.abo;

import org.mymoney.bankreportparser.Header;

import static org.mymoney.bankreportparser.abo.ExtractorUtils.extractAccountName;
import static org.mymoney.bankreportparser.abo.ExtractorUtils.extractAccountNumber;
import static org.mymoney.bankreportparser.abo.ExtractorUtils.extractType;

/**
 * @author Michal Bocek
 * @since 09/01/2017
 */
public final class HeaderParser {

    public static final String HEADER_TYPE_CODE = "074";

    private HeaderParser() {
        throw new UnsupportedOperationException();
    }

    public static Header parse(String headerLine) {
        validateLength(headerLine);
        validateType(extractType(headerLine));
        return Header.builder()
            .accountName(extractAccountName(headerLine))
            .accountNumber(extractAccountNumber(headerLine))
            .build();
    }

    private static String validateType(String type) {
        if (!HEADER_TYPE_CODE.equals(type)) {
            throw new IllegalArgumentException("Not expected type: " + type);
        }
        return type;
    }

    private static void validateLength(String headerLine) {
        if (headerLine.length() != 128) {
            throw new IllegalArgumentException("Header line must be length 130 characters but it is: " + headerLine.length());
        }
    }
}

