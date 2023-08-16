package com.unforeseencompany.finances.dto.transaction;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class TransferDTO {

    @NotNull
    private BigDecimal amount;

    private String description;

    @NotNull
    private LocalDate transactionDate;

    @NotNull
    private Integer sourceAccountId;

    @NotNull
    private Integer targetAccountId;
}
