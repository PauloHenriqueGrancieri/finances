package com.unforeseencompany.finances.repository;

import com.unforeseencompany.finances.model.transaction.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Integer> {
    List<Transfer> findBySourceAccountName(String accountName);
    List<Transfer> findByTargetAccountName(String accountName);
}
