package com.unforeseencompany.finances.model.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.unforeseencompany.finances.enums.TransactionTypeEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@MappedSuperclass
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "description")
    private String description;

    @JsonFormat(pattern = "MM/dd/yyyy")
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "transaction_type", nullable = false)
    private TransactionTypeEnum transactionType;

    public Transaction(BigDecimal amount, String description, LocalDate transactionDate, TransactionTypeEnum transactionType) {
        this.amount = amount;
        this.description = description;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
    }
}
