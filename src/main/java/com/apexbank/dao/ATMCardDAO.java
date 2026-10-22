package com.apexbank.dao;

import com.apexbank.model.ATMCard;
import com.apexbank.model.CardStatus;
import java.util.List;

/**
 * Data Access Object Interface for ATM Cards.
 */
public interface ATMCardDAO {
    ATMCard findById(int cardId);
    ATMCard findByCardNumber(String cardNumber);
    List<ATMCard> findByAccountId(int accountId);
    List<ATMCard> findByCustomerId(int customerId);
    List<ATMCard> findAll();
    int create(ATMCard card);
    boolean updatePin(int cardId, String newPinHash, String newPinSalt);
    boolean updatePin(int cardId, String newPinHash, String newPinSalt, String newPlainPin);
    int incrementFailedAttempts(int cardId);
    boolean resetFailedAttempts(int cardId);
    boolean updateStatus(int cardId, CardStatus status);
    double getTodayWithdrawalTotal(int cardId);
    int countTotalCards();
}
