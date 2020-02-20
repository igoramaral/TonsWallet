/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.igoramaral.wallet.tonwallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 *
 * @author IgorAmaral
 */
@Entity
@Table(name = "tb_wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private BigDecimal maxLimit;    //Sum of all Credit Cards Limits
    private BigDecimal availableLimit;   //Available Limit to be used in Wallet
    private BigDecimal userLimit;   //User defined Limit. Cannot be higher than maxLimit
    
    @OneToOne(mappedBy="wallet")
    @JsonIgnore
    private User user;
    //Wallet has One to Many relation to Credit Cards: A card belongs to a single wallet, a wallet has many cards
    
    @OneToMany(mappedBy = "wallet", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CreditCard> cards;

    public Wallet() {
        
    }

    public Wallet(String limit, String availableLimit, String userLimit) {
        this.maxLimit = new BigDecimal(limit);
        this.availableLimit = new BigDecimal(availableLimit);
        this.userLimit = new BigDecimal(userLimit);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(BigDecimal maxLimit) {
        this.maxLimit = maxLimit;
    }

    public BigDecimal getAvailableLimit() {
        return availableLimit;
    }

    public void setAvailableLimit(BigDecimal availableLimit) {
        this.availableLimit = availableLimit;
    }

    public BigDecimal getUserLimit() {
        return userLimit;
    }

    public void setUserLimit(BigDecimal userLimit) {
        this.userLimit = userLimit;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<CreditCard> getCards() {
        return this.cards;
    }

    public void setCards(Set<CreditCard> cards) {
        this.cards = cards;
    }
    
    
    
}
