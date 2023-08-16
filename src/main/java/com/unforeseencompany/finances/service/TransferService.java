package com.unforeseencompany.finances.service;

import com.unforeseencompany.finances.dto.transaction.TransferDTO;
import com.unforeseencompany.finances.model.Account;
import com.unforeseencompany.finances.model.transaction.Transfer;
import com.unforeseencompany.finances.repository.AccountRepository;
import com.unforeseencompany.finances.repository.TransferRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TransferService {

    @Getter
    private final TransferRepository transferRepository;

    private final AccountService accountService;

    private final AccountRepository accountRepository;

    public List<Transfer> getAllTransferTransactions() {
        return transferRepository.findAll();
    }

    public Optional<Transfer> findTransferById(Integer id) {
        return transferRepository.findById(id);
    }

    public List<Transfer> listTransferTransactionsByAccountName(String accountName) {
        if (accountRepository.findAccountByName(accountName).isEmpty()) {
            throw new IllegalArgumentException("Account not found with name: " + accountName);
        }

        List<Transfer> sourceAccountList = transferRepository.findBySourceAccountName(accountName);
        List<Transfer> targetTransferList = transferRepository.findByTargetAccountName(accountName);

        return Stream.concat(sourceAccountList.stream(), targetTransferList.stream())
                .collect(Collectors.toList());
    }

    public Transfer saveTransferTransaction(TransferDTO transferDTO) {
        Optional<Account> sourceAccount = accountService.findAccountById(transferDTO.getSourceAccountId());
        Optional<Account> targetAccount = accountService.findAccountById(transferDTO.getTargetAccountId());

        Transfer transfer = new Transfer(transferDTO);

        if (sourceAccount.isPresent() && targetAccount.isPresent()) {
            transfer.setSourceAccount(sourceAccount.get());
            transfer.setTargetAccount(targetAccount.get());

            accountService.decreaseBalance(sourceAccount.get(), transferDTO.getAmount());
            accountService.increaseBalance(targetAccount.get(), transferDTO.getAmount());
        } else if (sourceAccount.isEmpty()) {
            throw new IllegalArgumentException("Source account not found with id: " + transferDTO.getSourceAccountId());
        } else {
            throw new IllegalArgumentException("Target account not found with id: " + transferDTO.getTargetAccountId());
        }

        return transferRepository.save(transfer);
    }

    public Transfer updateTransferTransaction(Integer id, TransferDTO transferDTO) {
        Optional<Transfer> transferOptional = transferRepository.findById(id);

        if (transferOptional.isPresent()) {
            Transfer transfer = transferOptional.get();

            accountService.increaseBalance(transfer.getSourceAccount(), transfer.getAmount());
            accountService.decreaseBalance(transfer.getTargetAccount(), transfer.getAmount());

            if (transferDTO != null) {

                Optional<Account> sourceAccount = accountService.findAccountById(transferDTO.getSourceAccountId());
                if (sourceAccount.isPresent()) {
                    transfer.setSourceAccount(sourceAccount.get());
                } else {
                    throw new IllegalArgumentException("Source account not found with id: " + transferDTO.getSourceAccountId());
                }

                Optional<Account> targetAccount = accountService.findAccountById(transferDTO.getTargetAccountId());
                if (targetAccount.isPresent()) {
                    transfer.setTargetAccount(targetAccount.get());
                } else {
                    throw new IllegalArgumentException("Target account not found with id: " + transferDTO.getTargetAccountId());
                }

                transfer.setAmount(transferDTO.getAmount());

                if (transferDTO.getDescription() != null && !transferDTO.getDescription().isEmpty()) {
                    transfer.setDescription(transferDTO.getDescription());
                }

                transfer.setTransactionDate(transferDTO.getTransactionDate());

                //correct the balance
                accountService.decreaseBalance(transfer.getSourceAccount(), transfer.getAmount());
                accountService.increaseBalance(transfer.getTargetAccount(), transfer.getAmount());

                return transferRepository.save(transfer);
            } else {
                throw new IllegalArgumentException("TransferDTO cannot be null");
            }
        } else {
            return null; // Transaction not found
        }
    }

    public boolean deleteTransferById(Integer id) {
        Optional<Transfer> transferOptional = transferRepository.findById(id);
        if (transferOptional.isPresent()) {
            Transfer transfer = transferOptional.get();

            accountService.increaseBalance(transfer.getSourceAccount(), transfer.getAmount());
            accountService.decreaseBalance(transfer.getTargetAccount(), transfer.getAmount());

            transferRepository.delete(transfer);
            return true;
        }
        return false;
    }

    public void deleteAllTransferTransactions() {
        List<Transfer> transfers = transferRepository.findAll();

        for (Transfer transfer : transfers) {
            accountService.increaseBalance(transfer.getSourceAccount(), transfer.getAmount());
            accountService.decreaseBalance(transfer.getTargetAccount(), transfer.getAmount());
        }

        transferRepository.deleteAll();
    }
}
