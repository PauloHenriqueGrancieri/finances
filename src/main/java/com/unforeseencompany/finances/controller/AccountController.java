package com.unforeseencompany.finances.controller;

import com.unforeseencompany.finances.dto.CreateAccountDTO;
import com.unforeseencompany.finances.model.Account;
import com.unforeseencompany.finances.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller to manage operations related to accounts.
 */
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    Logger log = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    /**
     * Creates a new account based on the provided data.
     *
     * @param createAccountDto The account data to be created (name and initial balance).
     * @return The HTTP response containing the created account, or an error status if the parameters are incorrect.
     */
    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountDTO createAccountDto) {
        try {
            Account account = new Account(createAccountDto);

            return ResponseEntity.ok(accountService.saveAccount(account));
        } catch (IllegalArgumentException e) {
            log.error("Error creating account: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error creating account: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Retrieves an account based on the provided ID.
     *
     * @param id The ID of the account to retrieve.
     * @return The HTTP response containing the retrieved account, or an error status if the account is not found or an internal error occurs.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Account> findAccountById(@PathVariable Integer id) {
        try {
            Optional<Account> accountOptional = accountService.findAccountById(id);
            return accountOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error obtaining account: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Retrieves all registered accounts.
     *
     * @return The HTTP response containing the list of registered accounts, or an error status if an internal error occurs.
     */
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        try {
            List<Account> accounts = accountService.getAllAccounts();
            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            log.error("Error obtaining the list of accounts: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Deletes an account based on the provided ID.
     *
     * @param id The ID of the account to be deleted.
     * @return The HTTP response indicating the operation result. Returns status 204 No Content if the account is found and deleted,
     *         or status 404 Not Found if the account is not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountById(@PathVariable Integer id) {
        try {
            if (accountService.deleteAccountById(id)) {
                return ResponseEntity.noContent().build();
            } else {
                log.error("Account not found with id: " + id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error deleting account: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
