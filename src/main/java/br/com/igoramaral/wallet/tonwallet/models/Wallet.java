/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.igoramaral.wallet.tonwallet.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
    private BigDecimal currLimit;   //Available Limit to be used in Wallet
    private BigDecimal userLimit;   //User defined Limit. Cannot be higher than maxLimit
    
    //Wallet has One to One relation to User: A wallet belongs to a single user, an user has only one wallet
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName= "id")
    private User user;
    
    //Wallet has One to Many relation to Credit Cards: A card belongs to a single wallet, a wallet has many cards
    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreditCard> cards;

    public Wallet() {
    }

    public Wallet(User user) {
        this.maxLimit = new BigDecimal("0.00");
        this.currLimit = new BigDecimal("0.00");
        this.userLimit = new BigDecimal("0.00");
        this.user = user;
        this.cards = new ArrayList<>();
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

    public BigDecimal getCurrLimit() {
        return currLimit;
    }

    public void setCurrLimit(BigDecimal currLimit) {
        this.currLimit = currLimit;
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

    public void addCard(CreditCard card) {
        cards.add(card);
    }

    public void removeCard(CreditCard card) {
        cards.remove(card);
    }    
}
