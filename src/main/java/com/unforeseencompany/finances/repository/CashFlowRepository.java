package com.unforeseencompany.finances.repository;

import com.unforeseencompany.finances.model.transaction.CashFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashFlowRepository extends JpaRepository<CashFlow, Integer> {
    List<CashFlow> findByAccountName(String accountName);
}
