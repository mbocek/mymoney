package org.mymoney.bankreportparser.abo;

import org.mymoney.bankreportparser.Transaction;

import static org.mymoney.bankreportparser.abo.ExtractorUtils.*;

/**
 * @author Michal Bocek
 * @since 10/01/2017
 */
public final class TransactionParser {

    private static final String TRANSACTION_TYPE_CODE = "075";

    private TransactionParser() {
        throw new UnsupportedOperationException();
    }

    public static Transaction parse(String transactionLine) {
        validateLength(transactionLine);
        validateType(extractType(transactionLine));
        return Transaction.builder()
            .sourceAccountNumber(extractSourceAccountNumber(transactionLine))
            .targetAccountNumber(extractTargetAccountNumber(transactionLine))
            .amount(extractAmount(transactionLine))
            .operation(extractOperation(transactionLine))
            .bankCode(extractBankCode(transactionLine))
            .constantSymbol(extractConstantSymbol(transactionLine))
            .variableSymbol(extractVariableSymbol(transactionLine))
            .specificSymbol(extractSpecificSymbol(transactionLine))
            .date(extractValutationDate(transactionLine))
            .extraInfo(extractExtraInfo(transactionLine))
            .build();
    }

    private static void validateLength(String headerLine) {
        if (headerLine.length() != 128) {
            throw new IllegalArgumentException("Header line must be length 130 characters but it is: " + headerLine.length());
        }
    }

    private static String validateType(String type) {
        if (!TRANSACTION_TYPE_CODE.equals(type)) {
            throw new IllegalArgumentException("Not expected type: " + type);
        }
        return type;
    }
}
