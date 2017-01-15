package org.mymoney.bankreportparser;

/**
 * @author Michal Bocek
 * @since 09/01/2017
 */
public interface ReportParser {

    Header getHeader();

    Boolean hasTransaction();

    Transaction getNextTransaction();
}
