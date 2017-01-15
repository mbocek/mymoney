package org.mymoney.bankreportparser;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Michal Bocek
 * @since 09/01/2017
 */
@Builder
@Getter
@ToString
public class Header {

    private String accountNumber;
    private String accountName;
}
