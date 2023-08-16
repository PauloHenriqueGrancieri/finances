package com.unforeseencompany.finances.controller;

import com.unforeseencompany.finances.model.transaction.Transaction;
import com.unforeseencompany.finances.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller to manage operations related to transactions.
 */
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    Logger log = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionService transactionService;

    /**
     * Endpoint to retrieve all transactions.
     *
     * @return The HTTP response containing the list of all transactions, or an error status if an error occurs.
     */
    @GetMapping()
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        try {
            return ResponseEntity.ok(transactionService.getAllTransactions());
        } catch (Exception e) {
            log.error("Error getting transactions: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint to delete all transactions.
     *
     * @return The HTTP response indicating the result of the operation, or an error status if an error occurs.
     */
    @DeleteMapping()
    public ResponseEntity<Void> deleteAllTransactions() {
        try {
            transactionService.deleteAllTransactions();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting transactions: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
