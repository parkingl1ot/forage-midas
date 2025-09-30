package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_records")
public class UserRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Float balance;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransactionRecord> sentTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransactionRecord> receivedTransactions = new ArrayList<>();

    protected UserRecord() {
    }

    public UserRecord(String name, Float balance) {
        this.name = name;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return String.format("User[id=%d, name='%s', balance='%f']", id, name, balance);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public List<TransactionRecord> getSentTransactions() {
        return sentTransactions;
    }

    public void setSentTransactions(List<TransactionRecord> sentTransactions) {
        this.sentTransactions = sentTransactions;
    }

    public List<TransactionRecord> getReceivedTransactions() {
        return receivedTransactions;
    }

    public void setReceivedTransactions(List<TransactionRecord> receivedTransactions) {
        this.receivedTransactions = receivedTransactions;
    }
}
