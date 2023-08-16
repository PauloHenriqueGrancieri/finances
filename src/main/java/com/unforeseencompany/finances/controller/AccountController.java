package com.unforeseencompany.finances.controller;

import com.unforeseencompany.finances.dto.AccountDTO;
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
     * Retrieves all registered accounts.
     *
     * @return The HTTP response containing the list of registered accounts, or an error status if an internal error occurs.
     */
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        try {
            return ResponseEntity.ok(accountService.getAllAccounts());
        } catch (Exception e) {
            log.error("Error obtaining the list of accounts: " + e.getMessage());
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
     * Retrieves an account based on the provided name.
     *
     * @param name The name of the account to retrieve.
     * @return The HTTP response containing the retrieved account, or an error status if the account is not found or an internal error occurs.
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Account> findAccountByName(@PathVariable String name) {
        try {
            Optional<Account> accountOptional = accountService.findAccountByName(name);
            return accountOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error obtaining account: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Creates a new account based on the provided data.
     *
     * @param accountDto The account data to be created (name and initial balance).
     * @return The HTTP response containing the created account, or an error status if the parameters are incorrect.
     */
    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountDTO accountDto) {
        try {
            return ResponseEntity.ok(accountService.saveAccount(new Account(accountDto)));
        } catch (IllegalArgumentException e) {
            log.error("Error creating account: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error creating account: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Updates an account based on the provided ID and account data.
     *
     * @param id         The ID of the account to be updated.
     * @param accountDTO The account data containing the updated information (name and initial balance).
     * @return The HTTP response containing the updated account, or an error status if the parameters are incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Integer id, @Valid @RequestBody AccountDTO accountDTO) {
        try {
            Account updatedAccount = accountService.updateAccount(id, accountDTO);
            if (updatedAccount != null) {
                return ResponseEntity.ok(updatedAccount);
            } else {
                log.error("Account not found with id: " + id);
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            log.error("Error updating account: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error updating account: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Deletes an account based on the provided ID.
     *
     * @param id The ID of the account to be deleted.
     * @return The HTTP response indicating the operation result. Returns status 204 No Content if the account is found and deleted,
     * or status 404 Not Found if the account is not found.
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

    /**
     * Endpoint to delete all registered accounts.
     *
     * @return The HTTP response indicating the result of the operation, or an error status if an error occurs.
     */
    @DeleteMapping()
    public ResponseEntity<Void> deleteAllAccounts() {
        try {
            accountService.deleteAllAccounts();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting accounts: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
