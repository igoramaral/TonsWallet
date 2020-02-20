/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.igoramaral.wallet.tonwallet.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 *
 * @author IgorAmaral
 */

@Entity
@Table(name="tb_user")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String name;
    
    //User has One to One relation to Wallet: A wallet belongs to a single user, an user has only one wallet
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "wallet_id", referencedColumnName= "id")
    private Wallet wallet;
    
    public User(){
        
    }
        
    public User(String name, Wallet wallet) {
        this.name = name;
        this.wallet = wallet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallets) {
        this.wallet = wallet;
    }
    

}
