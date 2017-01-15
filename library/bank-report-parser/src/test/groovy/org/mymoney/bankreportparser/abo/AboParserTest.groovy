package org.mymoney.bankreportparser.abo

import org.mymoney.bankreportparser.Transaction
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate

/**
 * @author Michal Bocek
 * @since 14/01/2017
 */
class AboParserTest extends Specification {

    @Shared aboParser = new AboParser(getClass().getClassLoader().getResourceAsStream("sample.abo"))

    def "test Header"() {
        setup:
            def inputStream = getClass().getClassLoader().getResourceAsStream("sample.abo")
            def aboParser = new AboParser(inputStream)
        expect:
            aboParser.header.accountName == "Novák, Jan"
            aboParser.header.accountNumber == "000000240022222"
    }

    def "test Transaction"() {
        when:
            def transaction = aboParser.nextTransaction
        then:
            transaction.sourceAccountNumber == sourceAccountNumber
            transaction.targetAccountNumber == targetAccountNumber
            transaction.amount == amount
            transaction.operation == operation
            transaction.bankCode == bankCode
            transaction.constantSymbol == constantSymbol
            transaction.variableSymbol == variableSymbol
            transaction.specificSymbol == specificSymbol
            transaction.date == date
        where:
            sourceAccountNumber|targetAccountNumber|amount|operation|bankCode|constantSymbol|variableSymbol|specificSymbol|date|extraInfo
            "000000240022222"|"000000290023333"|new BigDecimal("10")|Transaction.Operation.CREDIT|"2010"|"0020100558"|"0000000000"|"0000000000"|LocalDate.of(2012,6,26)|""
            "000000240022222"|"000000290023333"|new BigDecimal("10")|Transaction.Operation.DEBET |"2010"|"0020100558"|"0000000000"|"0000000000"|LocalDate.of(2012,6,26)|""
            "000000240022222"|"000000000000000"|new BigDecimal("0") |Transaction.Operation.CREDIT|"0000"|"0000000000"|"0000000000"|"0000000000"|LocalDate.of(2012,6,30)|""
    }
}
