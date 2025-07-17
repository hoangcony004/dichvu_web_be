package web.backend.core.entitys.systems;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import web.backend.core.bases.BaseEntity;

public class SysTransaction extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "amount")  // Amount of the transaction
    private BigDecimal amount;

    @Column(name = "transaction_code", unique = true)  // Unique code for the transaction
    private String transaction_code;

    @Column(name = "account_number")
    private String account_number;

    @Column(name = "bank_name")
    private String bank_name;

    @Column(name = "account_name")
    private String account_name;

    @Column(name = "type")
    private String type; // banking / cart

    @Column(name = "status")
    private String status; // pending / completed / failed

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    public SysTransaction() {
    }

    public SysTransaction(Long userId, BigDecimal amount, String transaction_code, String account_number, String account_name, String type, String status, String description, String bank_name) {
        this.userId = userId;
        this.amount = amount;
        this.transaction_code = transaction_code;
        this.account_number = account_number;
        this.account_name = account_name;
        this.type = type;
        this.status = status;
        this.description = description;
        this.bank_name = bank_name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransaction_code() {
        return transaction_code;
    }

    public void setTransaction_code(String transaction_code) {
        this.transaction_code = transaction_code;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }
}
