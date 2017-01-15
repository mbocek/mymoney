package org.mymoney.bankreportparser.abo;

import org.mymoney.bankreportparser.Header;
import org.mymoney.bankreportparser.ReportParser;
import org.mymoney.bankreportparser.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * @author Michal Bocek
 * @since 09/01/2017
 */
public class AboParser implements ReportParser {

    private static final Logger logger = LoggerFactory.getLogger(AboParser.class);

    private final BufferedReader reader;
    private Header header;
    private String headerLine;
    private String furtherLine;

    public AboParser(InputStream data) {
        reader = new BufferedReader(new InputStreamReader(data));
        headerLine = readLine();
        logger.debug("Header line: {}", headerLine);
        furtherLine = readLine();
    }

    public Header getHeader() {
        header = isNull(header) ? HeaderParser.parse(headerLine) : header;
        furtherLine = readLine();
        return header;
    }

    public Boolean hasTransaction() {
        return nonNull(furtherLine);
    }

    public Transaction getNextTransaction() {
        String transactionLine = furtherLine;
        logger.debug("Transaction line: {}", transactionLine);
        Transaction result = nonNull(transactionLine) ? TransactionParser.parse(transactionLine) : null;
        furtherLine = nonNull(transactionLine) ? readLine() : null;
        return result;
    }

    private String readLine() {
        String result;
        try {
            result = reader.ready() ? reader.readLine() : null;
            if (isNull(result)) {
                reader.close();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return result;
    }
}
