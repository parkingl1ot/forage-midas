package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_records")
public class TransactionRecord {

    //automatically generated id for this table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Float amount;

    @Column(nullable = false)
    private Float incentiveAmount = 0.0f;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    //many transactions can have one sender/recipient, only loaded when needed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserRecord sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserRecord recipient;

    //default constructor for JPA (presenting existing transactions)
    protected TransactionRecord() {
    }

    //constructor for a new transaction (entering the transaction into the database)
    public TransactionRecord(UserRecord sender, UserRecord recipient, Float amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public TransactionRecord(UserRecord sender, UserRecord recipient, Float amount, Float incentiveAmount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.incentiveAmount = incentiveAmount;
        this.timestamp = LocalDateTime.now();
    }

    //getter&setter makes transaction data accessible in public
    //refer TransactionListener to see how this data is used
    public Long getId() {
        return id;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Float getIncentiveAmount() {
        return incentiveAmount;
    }

    public void setIncentiveAmount(Float incentiveAmount) {
        this.incentiveAmount = incentiveAmount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public UserRecord getSender() {
        return sender;
    }

    public void setSender(UserRecord sender) {
        this.sender = sender;
    }

    public UserRecord getRecipient() {
        return recipient;
    }

    public void setRecipient(UserRecord recipient) {
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "TransactionRecord{" +
                "id=" + id +
                ", amount=" + amount +
                ", incentiveAmount=" + incentiveAmount +
                ", timestamp=" + timestamp +
                ", senderId=" + (sender != null ? sender.getId() : null) +
                ", recipientId=" + (recipient != null ? recipient.getId() : null) +
                '}';
    }
}
