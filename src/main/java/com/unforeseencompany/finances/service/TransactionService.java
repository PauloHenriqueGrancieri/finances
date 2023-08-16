package com.unforeseencompany.finances.service;

import com.unforeseencompany.finances.model.transaction.CashFlow;
import com.unforeseencompany.finances.model.transaction.Transaction;
import com.unforeseencompany.finances.model.transaction.Transfer;
import com.unforeseencompany.finances.repository.CashFlowRepository;
import com.unforeseencompany.finances.repository.TransferRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TransactionService {

    @Getter
    private final CashFlowRepository cashFlowRepository;

    @Getter
    private final TransferRepository transferRepository;

    private final CashFlowService cashFlowService;

    private final TransferService transferService;

    public List<Transaction> getAllTransactions() {
        List<CashFlow> cashFlows = cashFlowService.getAllCashFlowTransactions();
        List<Transfer> transfers = transferService.getAllTransferTransactions();

        return Stream.concat(cashFlows.stream(), transfers.stream())
                .collect(Collectors.toList());
    }

    public void deleteAllTransactions() {
        cashFlowService.deleteAllCashFlowTransactions();
        transferService.deleteAllTransferTransactions();
    }
}