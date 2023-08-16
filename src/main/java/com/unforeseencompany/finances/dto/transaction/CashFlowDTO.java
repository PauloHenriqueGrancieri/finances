package com.unforeseencompany.finances.dto.transaction;

import com.unforeseencompany.finances.enums.TransactionTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class CashFlowDTO {

    @NotNull
    private BigDecimal amount;

    private String description;

    @NotNull
    private LocalDate transactionDate;

    private TransactionTypeEnum transactionType;

    @NotNull
    private Integer accountId;
}
