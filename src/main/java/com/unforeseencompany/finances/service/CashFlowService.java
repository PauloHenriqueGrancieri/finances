package com.unforeseencompany.finances.service;

import com.unforeseencompany.finances.dto.transaction.CashFlowDTO;
import com.unforeseencompany.finances.enums.TransactionTypeEnum;
import com.unforeseencompany.finances.model.Account;
import com.unforeseencompany.finances.model.transaction.CashFlow;
import com.unforeseencompany.finances.repository.AccountRepository;
import com.unforeseencompany.finances.repository.CashFlowRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CashFlowService {

    @Getter
    private final CashFlowRepository cashFlowRepository;

    private final AccountService accountService;

    private final AccountRepository accountRepository;

    public List<CashFlow> getAllCashFlowTransactions() {
        return cashFlowRepository.findAll();
    }

    public Optional<CashFlow> findCashFlowById(Integer id) {
        return cashFlowRepository.findById(id);
    }

    public List<CashFlow> listCashFlowTransactionsByAccountName(String accountName) {
        if (accountRepository.findAccountByName(accountName).isEmpty()) {
            throw new IllegalArgumentException("Account not found with name: " + accountName);
        }

        return cashFlowRepository.findByAccountName(accountName);
    }

    public CashFlow saveCashFlowTransaction(CashFlowDTO cashFlowDTO) {
        Optional<Account> account = accountService.findAccountById(cashFlowDTO.getAccountId());

        CashFlow cashFlow = new CashFlow(cashFlowDTO);

        if (account.isPresent()) {
            cashFlow.setAccount(account.get());
        } else {
            throw new IllegalArgumentException("Account not found with id: " + cashFlowDTO.getAccountId());
        }

        if (cashFlow.getTransactionType().equals(TransactionTypeEnum.INCOME)) {
            accountService.increaseBalance(account.get(), cashFlowDTO.getAmount());
        } else {
            accountService.decreaseBalance(account.get(), cashFlowDTO.getAmount());
        }

        return cashFlowRepository.save(cashFlow);
    }

    public CashFlow updateCashFlowTransaction(Integer id, CashFlowDTO cashFlowDTO) {
        Optional<CashFlow> cashFlowOptional = cashFlowRepository.findById(id);

        if (cashFlowOptional.isPresent()) {
            CashFlow cashFlow = cashFlowOptional.get();

            if (cashFlow.getTransactionType().equals(TransactionTypeEnum.INCOME)) {
                accountService.decreaseBalance(cashFlow.getAccount(), cashFlow.getAmount());
            } else {
                accountService.increaseBalance(cashFlow.getAccount(), cashFlow.getAmount());
            }

            if (cashFlowDTO != null) {

                Optional<Account> account = accountService.findAccountById(cashFlowDTO.getAccountId());
                if (account.isPresent()) {
                    cashFlow.setAccount(account.get());
                } else {
                    throw new IllegalArgumentException("Account not found with id: " + cashFlowDTO.getAccountId());
                }

                cashFlow.setAmount(cashFlowDTO.getAmount());

                if (cashFlowDTO.getDescription() != null && !cashFlowDTO.getDescription().isEmpty()) {
                    cashFlow.setDescription(cashFlowDTO.getDescription());
                }

                cashFlow.setTransactionDate(cashFlowDTO.getTransactionDate());

                if (cashFlowDTO.getTransactionType() != null) {
                    cashFlow.setTransactionType(cashFlowDTO.getTransactionType());
                }
            } else {
                throw new IllegalArgumentException("CashFlowDTO cannot be null");
            }

            if (cashFlow.getTransactionType().equals(TransactionTypeEnum.INCOME)) {
                accountService.increaseBalance(cashFlow.getAccount(), cashFlowDTO.getAmount());
            } else {
                accountService.decreaseBalance(cashFlow.getAccount(), cashFlowDTO.getAmount());
            }

            return cashFlowRepository.save(cashFlow);
        } else {
            return null;
        }
    }

    public boolean deleteCashFlowById(Integer id) {
        Optional<CashFlow> cashFlowOptional = cashFlowRepository.findById(id);

        if (cashFlowOptional.isPresent()) {
            CashFlow cashFlow = cashFlowOptional.get();

            if (cashFlow.getTransactionType().equals(TransactionTypeEnum.INCOME)) {
                accountService.decreaseBalance(cashFlow.getAccount(), cashFlow.getAmount());
            } else {
                accountService.increaseBalance(cashFlow.getAccount(), cashFlow.getAmount());
            }

            cashFlowRepository.delete(cashFlow);
            return true;
        }
        return false;
    }

    public void deleteAllCashFlowTransactions() {
        List<CashFlow> cashFlows = cashFlowRepository.findAll();

        for (CashFlow cashFlow : cashFlows) {
            if (cashFlow.getTransactionType().equals(TransactionTypeEnum.INCOME)) {
                accountService.decreaseBalance(cashFlow.getAccount(), cashFlow.getAmount());
            } else {
                accountService.increaseBalance(cashFlow.getAccount(), cashFlow.getAmount());
            }
        }


    }
}
