package com.unforeseencompany.finances.controller;

import com.unforeseencompany.finances.dto.transaction.CashFlowDTO;
import com.unforeseencompany.finances.model.transaction.CashFlow;
import com.unforeseencompany.finances.service.CashFlowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cashflow")
@RequiredArgsConstructor
public class CashFlowController {

    Logger log = LoggerFactory.getLogger(TransactionController.class);

    private final CashFlowService cashFlowService;

    /**
     * Endpoint to get all cash flow transactions.
     *
     * @return The HTTP response containing the list of cash flow transactions.
     */
    @GetMapping()
    public ResponseEntity<List<CashFlow>> getAllCashFlowTransactions() {
        try {
            return ResponseEntity.ok(cashFlowService.getAllCashFlowTransactions());
        } catch (Exception e) {
            log.error("Error retrieving cash flow transactions: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint to retrieve a cash flow transaction by its ID.
     *
     * @param id The ID of the cash flow transaction to retrieve.
     * @return The HTTP response containing the retrieved cash flow transaction, or an error status if the transaction is not found or an internal error occurs.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CashFlow> findCashFlowById(@PathVariable Integer id) {
        try {
            Optional<CashFlow> cashFlowOptional = cashFlowService.findCashFlowById(id);

            if (cashFlowOptional.isPresent()) {
                return ResponseEntity.ok(cashFlowOptional.get());
            } else {
                log.error("Transaction not found with id: " + id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error obtaining cash flow transaction: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint to list all cash flow transactions based on the account name.
     *
     * @param accountName The name of the account to filter transactions.
     * @return The HTTP response containing the list of cash flow transactions for the specified account name.
     */
    @GetMapping("/accountName/{accountName}")
    public ResponseEntity<List<CashFlow>> listCashFlowTransactionsByAccountName(@PathVariable String accountName) {
        try {
            return ResponseEntity.ok(cashFlowService.listCashFlowTransactionsByAccountName(accountName));
        } catch (IllegalArgumentException e) {
            log.error("Account not found with name: " + accountName);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error listing cash flow transactions: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint to save cash flow transactions (income or expense).
     *
     * @param cashFlowDTO DTO containing the details of the cash flow transaction.
     * @return The HTTP response containing the created transaction, or an error status if the parameters are incorrect.
     */
    @PostMapping()
    public ResponseEntity<CashFlow> saveCashFlowTransaction(@Valid @RequestBody CashFlowDTO cashFlowDTO) {
        try {
            return ResponseEntity.ok(cashFlowService.saveCashFlowTransaction(cashFlowDTO));
        } catch (IllegalArgumentException e) {
            log.error("Error generating cash flow transaction: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error generating cash flow transaction: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint to update a cash flow transaction.
     *
     * @param id          The ID of the cash flow transaction to update.
     * @param cashFlowDTO DTO containing the updated details of the cash flow transaction.
     * @return The HTTP response containing the updated cash flow transaction, or an error status if the parameters are incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CashFlow> updateCashFlowTransaction(
            @PathVariable Integer id,
            @Valid @RequestBody CashFlowDTO cashFlowDTO) {
        try {
            CashFlow updatedCashFlow = cashFlowService.updateCashFlowTransaction(id, cashFlowDTO);
            if (updatedCashFlow != null) {
                return ResponseEntity.ok(updatedCashFlow);
            } else {
                log.error("Transaction not found with id: " + id);
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            log.error("Error updating cash flow transaction: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error updating cash flow transaction: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint to delete a cash flow transaction by its ID.
     *
     * @param id The ID of the cash flow transaction to be deleted.
     * @return The HTTP response indicating the operation result. Returns status 204 No Content if the transaction is found and deleted,
     * or status 404 Not Found if the transaction is not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCashFlowById(@PathVariable Integer id) {
        try {
            if (cashFlowService.deleteCashFlowById(id)) {
                return ResponseEntity.noContent().build();
            } else {
                log.error("Cash Flow transaction not found with id: " + id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error deleting Cash Flow transaction: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint to delete all cash flow transactions and adjust account balances.
     *
     * @return The HTTP response indicating the result of the operation.
     */
    @DeleteMapping()
    public ResponseEntity<Void> deleteAllCashFlowTransactionsAndAdjustBalances() {
        try {
            cashFlowService.deleteAllCashFlowTransactions();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting and adjusting cash flow transactions: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
