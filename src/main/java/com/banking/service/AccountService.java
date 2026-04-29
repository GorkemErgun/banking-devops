package com.banking.service;

import com.banking.exception.ResourceNotFoundException;
import com.banking.model.Account;
import com.banking.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Tüm hesapları getir
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // ID'ye göre hesap getir
    public Account getAccountById(Integer id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
    }

    // Yeni hesap oluştur
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    // Hesap güncelle
    public Account updateAccount(Integer id, Account accountDetails) {
        Account account = getAccountById(id);
        account.setBranch(accountDetails.getBranch());
        account.setBalance(accountDetails.getBalance());
        return accountRepository.save(account);
    }

    // Hesap sil
    public void deleteAccount(Integer id) {
        Account account = getAccountById(id);
        accountRepository.delete(account);
    }
}
