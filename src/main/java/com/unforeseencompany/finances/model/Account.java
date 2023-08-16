package com.unforeseencompany.finances.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unforeseencompany.finances.dto.AccountDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "account")
@Data
@NoArgsConstructor
public class Account {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "initial_balance")
    private BigDecimal initialBalance;

    @Column(name = "balance")
    private BigDecimal balance;

    @JsonIgnore
    @JsonFormat(pattern = "MM/dd/yyyy")
    @Column(name = "created_at")
    private LocalDate createdAt;

    public Account(AccountDTO accountDTO) {
        this.name = accountDTO.getName();
        this.initialBalance = accountDTO.getInitialBalance();
        this.balance = this.initialBalance;
        this.createdAt = LocalDate.now();
    }
}


