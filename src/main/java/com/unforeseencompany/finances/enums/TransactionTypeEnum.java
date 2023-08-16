package com.unforeseencompany.finances.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TransactionTypeEnum {
    INCOME(1, "Income"),
    EXPENSE(2, "Expense"),
    TRANSFER(3, "Transfer");

    private final Integer code;
    
    private final String description;
}
