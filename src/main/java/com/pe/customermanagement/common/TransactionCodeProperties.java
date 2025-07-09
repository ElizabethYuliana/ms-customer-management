package com.pe.customermanagement.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TransactionCodeProperties {
    @Value("${transaction.code.get}")
    private String getCode;

    @Value("${transaction.code.save}")
    private String saveCode;

    @Value("${transaction.code.update}")
    private String updateCode;

    @Value("${transaction.code.delete}")
    private String deleteCode;

    public String getTransactionCode(String operation) {
        return switch (operation) {
            case "GET" -> getCode;
            case "POST" -> saveCode;
            case "PUT" -> updateCode;
            case "DELETE" -> deleteCode;
            default -> "000";
        };
    }
}
