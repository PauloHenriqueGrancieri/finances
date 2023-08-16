package com.unforeseencompany.finances.model.transaction;

import com.unforeseencompany.finances.dto.transaction.CashFlowDTO;
import com.unforeseencompany.finances.model.Account;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cash_flow_transaction")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class CashFlow extends Transaction{

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public CashFlow(CashFlowDTO cashFlowDTO) {
        super(cashFlowDTO.getAmount(), cashFlowDTO.getDescription(), cashFlowDTO.getTransactionDate(), cashFlowDTO.getTransactionType());
    }
}
