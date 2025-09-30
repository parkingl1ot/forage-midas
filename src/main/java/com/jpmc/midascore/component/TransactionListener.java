package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionListener {
    private static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    @KafkaListener(topics = "${general.kafka-topic}")
    @Transactional
    public void handleTransaction(Transaction transaction) {
        logger.info("Received transaction: {}", transaction);

        try {
            // Validate transaction
            if (isValidTransaction(transaction)) {
                // Process the transaction
                processTransaction(transaction);
                logger.info("Transaction processed successfully: {}", transaction);
            } else {
                logger.warn("Transaction validation failed, discarding: {}", transaction);
            }
        } catch (Exception e) {
            logger.error("Error processing transaction: {}", transaction, e);
        }
    }

    private boolean isValidTransaction(Transaction transaction) {
        // Check if senderId is valid
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        if (sender == null) {
            logger.warn("Invalid senderId: {}", transaction.getSenderId());
            return false;
        }

        // Check if recipientId is valid
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());
        if (recipient == null) {
            logger.warn("Invalid recipientId: {}", transaction.getRecipientId());
            return false;
        }

        // Check if sender has sufficient balance
        if (sender.getBalance() < transaction.getAmount()) {
            logger.warn("Insufficient balance for sender {}: required {}, available {}", 
                       sender.getId(), transaction.getAmount(), sender.getBalance());
            return false;
        }

        return true;
    }

    private void processTransaction(Transaction transaction) {
        // Get sender and recipient
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());

        // Create transaction record
        TransactionRecord transactionRecord = new TransactionRecord(sender, recipient, transaction.getAmount());

        // Update balances
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount());

        // Save entities
        userRepository.save(sender);
        userRepository.save(recipient);
        transactionRecordRepository.save(transactionRecord);

        logger.info("Transaction recorded: sender {} balance updated to {}, recipient {} balance updated to {}", 
                   sender.getId(), sender.getBalance(), recipient.getId(), recipient.getBalance());
    }
}
