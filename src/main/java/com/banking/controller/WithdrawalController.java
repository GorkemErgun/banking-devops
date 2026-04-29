package com.banking.controller;

import com.banking.model.Withdrawal;
import com.banking.service.WithdrawalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/withdrawals")
public class WithdrawalController {

    private final WithdrawalService withdrawalService;

    public WithdrawalController(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }

    // GET /api/withdrawals — Tüm çekimleri listele
    @GetMapping
    public List<Withdrawal> getAllWithdrawals() {
        return withdrawalService.getAllWithdrawals();
    }

    // GET /api/withdrawals/{id} — Tek çekim getir
    @GetMapping("/{id}")
    public Withdrawal getWithdrawalById(@PathVariable Integer id) {
        return withdrawalService.getWithdrawalById(id);
    }

    // POST /api/withdrawals?customerId=1&accountId=1 — Yeni para çekme (bakiye
    // kontrolü yapılır)
    @PostMapping
    public ResponseEntity<Withdrawal> createWithdrawal(
            @RequestParam Integer customerId,
            @RequestParam Integer accountId,
            @Valid @RequestBody Withdrawal withdrawal) {
        Withdrawal created = withdrawalService.createWithdrawal(customerId, accountId, withdrawal);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // PUT /api/withdrawals/{id} — Çekim güncelle
    @PutMapping("/{id}")
    public Withdrawal updateWithdrawal(@PathVariable Integer id, @Valid @RequestBody Withdrawal withdrawal) {
        return withdrawalService.updateWithdrawal(id, withdrawal);
    }

    // DELETE /api/withdrawals/{id} — Çekim sil (tutar hesaba geri döner)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWithdrawal(@PathVariable Integer id) {
        withdrawalService.deleteWithdrawal(id);
        return ResponseEntity.noContent().build();
    }
}
