package com.unforeseencompany.finances.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class AccountDTO {

    @NotBlank
    @NotNull
    private String name;

    @NotNull
    private BigDecimal initialBalance;
}
