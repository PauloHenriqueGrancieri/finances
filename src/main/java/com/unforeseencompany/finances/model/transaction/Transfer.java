package com.unforeseencompany.finances.model.transaction;

import com.unforeseencompany.finances.dto.transaction.TransferDTO;
import com.unforeseencompany.finances.enums.TransactionTypeEnum;
import com.unforeseencompany.finances.model.Account;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transfer_transaction")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Transfer extends Transaction {

    @ManyToOne
    @JoinColumn(name = "source_account_id", nullable = false)
    private Account sourceAccount;

    @ManyToOne
    @JoinColumn(name = "target_account_id", nullable = false)
    private Account targetAccount;

    public Transfer(TransferDTO transferDTO) {
        super(transferDTO.getAmount(), transferDTO.getDescription(), transferDTO.getTransactionDate(), TransactionTypeEnum.TRANSFER);
    }
}
