package com.banking.repository;

import com.banking.model.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal, Integer> {

    List<Withdrawal> findByCustomerId(Integer customerId);

    List<Withdrawal> findByAccountId(Integer accountId);
}
