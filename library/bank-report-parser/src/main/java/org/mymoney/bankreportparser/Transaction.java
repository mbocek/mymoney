package org.mymoney.bankreportparser;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Michal Bocek
 * @since 09/01/2017
 */
@Builder
@Getter
@ToString
public class Transaction {

    public enum Operation {
        CREDIT, DEBET, STORNO_CREDIT, STORNO_DEBET;
    }

    private String sourceAccountNumber;
    private String targetAccountNumber;
    private BigDecimal amount;
    private Operation operation;
    private String bankCode;
    private String variableSymbol;
    private String constantSymbol;
    private String specificSymbol;
    private LocalDate date;
    private String extraInfo;
}
