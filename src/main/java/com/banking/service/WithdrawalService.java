package com.banking.service;

import com.banking.exception.InsufficientBalanceException;
import com.banking.exception.ResourceNotFoundException;
import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.Withdrawal;
import com.banking.repository.AccountRepository;
import com.banking.repository.CustomerRepository;
import com.banking.repository.WithdrawalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WithdrawalService {

    private final WithdrawalRepository withdrawalRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    public WithdrawalService(WithdrawalRepository withdrawalRepository,
            CustomerRepository customerRepository,
            AccountRepository accountRepository) {
        this.withdrawalRepository = withdrawalRepository;
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    // Tüm çekimleri getir
    public List<Withdrawal> getAllWithdrawals() {
        return withdrawalRepository.findAll();
    }

    // ID'ye göre çekim getir
    public Withdrawal getWithdrawalById(Integer id) {
        return withdrawalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Withdrawal not found with id: " + id));
    }

    // Yeni para çekme işlemi — BAKİYE KONTROLÜ VAR
    @Transactional
    public Withdrawal createWithdrawal(Integer customerId, Integer accountId, Withdrawal withdrawalRequest) {

        // Müşteri ve hesap var mı kontrol et
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        BigDecimal amount = withdrawalRequest.getAmount();

        // Bakiye kontrolü: hesap bakiyesi çekim tutarından düşük mü?
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Account balance: " + account.getBalance() + ", Withdrawal amount: "
                            + amount);
        }

        // Bakiyeyi düşür
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        // Withdrawal kaydını oluştur
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setCustomer(customer);
        withdrawal.setAccount(account);
        withdrawal.setDate(withdrawalRequest.getDate());
        withdrawal.setAmount(amount);

        return withdrawalRepository.save(withdrawal);
    }

    // Çekim güncelle
    @Transactional
    public Withdrawal updateWithdrawal(Integer id, Withdrawal withdrawalDetails) {
        Withdrawal withdrawal = getWithdrawalById(id);

        // Eski tutarı hesaba geri ekle
        Account account = withdrawal.getAccount();
        account.setBalance(account.getBalance().add(withdrawal.getAmount()));

        BigDecimal newAmount = withdrawalDetails.getAmount();

        // Yeni tutar için bakiye kontrolü
        if (account.getBalance().compareTo(newAmount) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance for update. Available: " + account.getBalance() + ", Requested: "
                            + newAmount);
        }

        // Yeni tutarı düşür
        account.setBalance(account.getBalance().subtract(newAmount));
        accountRepository.save(account);

        // Withdrawal bilgilerini güncelle
        withdrawal.setDate(withdrawalDetails.getDate());
        withdrawal.setAmount(newAmount);

        return withdrawalRepository.save(withdrawal);
    }

    // Çekim sil (tutarı hesaba geri ekle)
    @Transactional
    public void deleteWithdrawal(Integer id) {
        Withdrawal withdrawal = getWithdrawalById(id);

        // Silinen çekim tutarını hesaba geri yükle
        Account account = withdrawal.getAccount();
        account.setBalance(account.getBalance().add(withdrawal.getAmount()));
        accountRepository.save(account);

        withdrawalRepository.delete(withdrawal);
    }
}
