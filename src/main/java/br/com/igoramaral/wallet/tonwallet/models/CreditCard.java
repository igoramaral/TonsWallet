/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.igoramaral.wallet.tonwallet.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author IgorAmaral
 */
@Entity
@Table(name="tb_cards")
public class CreditCard {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String cardName;  //creates a name for a given credit card so the user may identify it easily
    private String cardNumber;    //stores the number of the credit card
    private String holderName;
    private String cvv;
    private BigDecimal maxLimit;
    private BigDecimal availableLimit;
    private int paymentDay;
    private LocalDate expirationDate;
    
    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    public CreditCard() {
    }

    public CreditCard(String cardName, String cardNumber, String holderName, String cvv, String limit, String availableLimit, int paymentDay, String expirationDate) {
        YearMonth date = YearMonth.parse(expirationDate);
        LocalDate expiration = date.atEndOfMonth();
        
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.holderName = holderName;
        this.cvv = cvv;
        this.maxLimit = new BigDecimal(limit);
        this.availableLimit = new BigDecimal(availableLimit);
        this.paymentDay = paymentDay;
        this.expirationDate = expiration;
        this.wallet = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public BigDecimal getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(BigDecimal limit) {
        this.maxLimit = limit;
    }

    public BigDecimal getAvailableLimit() {
        return availableLimit;
    }

    public void setAvailableLimit(BigDecimal availableLimit) {
        this.availableLimit = availableLimit;
    }

    public int getPaymentDay() {
        return paymentDay;
    }

    public void setPaymentDay(int paymentDay) {
        this.paymentDay = paymentDay;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
    
    
    
}