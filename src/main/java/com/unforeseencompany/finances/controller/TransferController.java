package com.unforeseencompany.finances.controller;

import com.unforeseencompany.finances.dto.transaction.TransferDTO;
import com.unforeseencompany.finances.model.transaction.Transfer;
import com.unforeseencompany.finances.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transfer")
@RequiredArgsConstructor
public class TransferController {

    Logger log = LoggerFactory.getLogger(TransactionController.class);

    private final TransferService transferService;

    /**
     * Endpoint to get all transfer transactions.
     *
     * @return The HTTP response containing the list of transfer transactions.
     */
    @GetMapping()
    public ResponseEntity<List<Transfer>> getAllTransferTransactions() {
        try {
            return ResponseEntity.ok(transferService.getAllTransferTransactions());
        } catch (Exception e) {
            log.error("Error retrieving transfer transactions: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint to retrieve a transfer transaction by its ID.
     *
     * @param id The ID of the transfer transaction to retrieve.
     * @return The HTTP response containing the retrieved transfer transaction, or an error status if the transaction is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Transfer> findTransferById(@PathVariable Integer id) {
        try {
            Optional<Transfer> transferOptional = transferService.findTransferById(id);

            if (transferOptional.isPresent()) {
                return ResponseEntity.ok(transferOptional.get());
            } else {
                log.error("Transaction not found with id: " + id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error obtaining transfer transaction: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint to list all transfer transactions based on the account name.
     *
     * @param accountName The name of the account to filter transactions.
     * @return The HTTP response containing the list of transfer transactions for the specified account name.
     */
    @GetMapping("/accountname/{accountName}")
    public ResponseEntity<List<Transfer>> listTransferTransactionsByAccountName(@PathVariable String accountName) {
        try {
            return ResponseEntity.ok(transferService.listTransferTransactionsByAccountName(accountName));
        } catch (IllegalArgumentException e) {
            log.error("Account not found with name: " + accountName);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error listing transfer transactions: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint to save transfer transactions between accounts.
     *
     * @param transferDTO DTO containing the details of the transfer transaction, including source and target accounts.
     * @return The HTTP response containing the created transaction, or an error status if the parameters are incorrect.
     */
    @PostMapping()
    public ResponseEntity<Transfer> saveTransaction(@Valid @RequestBody TransferDTO transferDTO) {
        try {
            return ResponseEntity.ok(transferService.saveTransferTransaction(transferDTO));
        } catch (IllegalArgumentException e) {
            log.error("Error generating transfer transaction: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error generating transfer transaction: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint to update a transfer transaction by its ID.
     *
     * @param id          The ID of the transfer transaction to be updated.
     * @param transferDTO DTO containing the details of the transfer transaction, including source and target accounts.
     * @return The HTTP response containing the updated transfer transaction, or an error status if the parameters are incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Transfer> updateTransferTransaction(@PathVariable Integer id, @Valid @RequestBody TransferDTO transferDTO) {
        try {
            Transfer updatedTransfer = transferService.updateTransferTransaction(id, transferDTO);
            if (updatedTransfer != null) {
                return ResponseEntity.ok(updatedTransfer);
            } else {
                log.error("Transaction not found with id: " + id);
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            log.error("Error updating transfer transaction: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error updating transfer transaction: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint to delete a transfer transaction by its ID.
     *
     * @param id The ID of the transfer transaction to be deleted.
     * @return The HTTP response indicating the operation result. Returns status 204 No Content if the transaction is found and deleted,
     * or status 404 Not Found if the transaction is not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransferById(@PathVariable Integer id) {
        try {
            if (transferService.deleteTransferById(id)) {
                return ResponseEntity.noContent().build();
            } else {
                log.error("Transaction not found with id: " + id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error deleting transfer transaction: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint to delete all transfer transactions.
     *
     * @return The HTTP response indicating the result of the operation.
     */
    @DeleteMapping()
    public ResponseEntity<Void> deleteAllTransferTransactions() {
        try {
            transferService.deleteAllTransferTransactions();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting transfer transactions: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

}
