package com.unforeseencompany.finances.service;

import com.unforeseencompany.finances.dto.AccountDTO;
import com.unforeseencompany.finances.enums.TransactionTypeEnum;
import com.unforeseencompany.finances.model.Account;
import com.unforeseencompany.finances.model.transaction.Transfer;
import com.unforeseencompany.finances.repository.AccountRepository;
import com.unforeseencompany.finances.repository.CashFlowRepository;
import com.unforeseencompany.finances.repository.TransferRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Getter
    private final AccountRepository accountRepository;

    private final TransferRepository transferRepository;

    private final CashFlowRepository cashFlowRepository;

    public void decreaseBalance(Account account, BigDecimal amount) {
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
    }

    public void increaseBalance(Account account, BigDecimal amount) {
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> findAccountById(Integer id) {
        return accountRepository.findById(id);
    }

    public Optional<Account> findAccountByName(String name) {
        return accountRepository.findAccountByName(name);
    }

    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account updateAccount(Integer id, AccountDTO accountDto) {
        Optional<Account> accountOptional = accountRepository.findById(id);

        if (accountOptional.isPresent()) {
            Account existingAccount = accountOptional.get();

            if (!accountDto.getName().equals(existingAccount.getName())) {
                existingAccount.setName(accountDto.getName());
            }

            if (!accountDto.getInitialBalance().equals(existingAccount.getInitialBalance())) {

                if (existingAccount.getInitialBalance().compareTo(accountDto.getInitialBalance()) > 0) {
                    existingAccount.setBalance(existingAccount.getBalance().subtract(existingAccount.getBalance().subtract(accountDto.getInitialBalance())));
                } else {
                    existingAccount.setBalance(existingAccount.getBalance().add(accountDto.getInitialBalance().subtract(existingAccount.getBalance())));
                }

                existingAccount.setInitialBalance(accountDto.getInitialBalance());
            }

            return accountRepository.save(existingAccount);
        } else {
            return null; // Account not found
        }
    }

    public Boolean deleteAccountById(Integer id) {
        Optional<Account> accountOptional = accountRepository.findById(id);

        if (accountOptional.isPresent()) {

            cashFlowRepository.findByAccountName(accountOptional.get().getName()).forEach(
                    cashFlow -> {
                        if (cashFlow.getTransactionType().equals(TransactionTypeEnum.INCOME)) {
                            decreaseBalance(cashFlow.getAccount(), cashFlow.getAmount());
                        } else {
                            increaseBalance(cashFlow.getAccount(), cashFlow.getAmount());
                        }

                        cashFlowRepository.delete(cashFlow);
                    }
            );

            List<Transfer> sourceAccountList = transferRepository.findBySourceAccountName(accountOptional.get().getName());
            List<Transfer> targetTransferList = transferRepository.findByTargetAccountName(accountOptional.get().getName());

            List<Transfer> transferList = Stream.concat(sourceAccountList.stream(), targetTransferList.stream()).toList();

            transferList.forEach(
                    transfer -> {
                        increaseBalance(transfer.getSourceAccount(), transfer.getAmount());
                        decreaseBalance(transfer.getTargetAccount(), transfer.getAmount());

                        transferRepository.delete(transfer);
                    }
            );

            accountRepository.deleteById(id);
            return true; // Account found and deleted
        } else {
            return false; // Account not found
        }
    }

    public void deleteAllAccounts() {
        cashFlowRepository.deleteAll();

        transferRepository.deleteAll();

        accountRepository.deleteAll();
    }
}