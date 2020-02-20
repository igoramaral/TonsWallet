/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.igoramaral.wallet.tonwallet.controller;

import br.com.igoramaral.wallet.tonwallet.models.ValueDTO;
import br.com.igoramaral.wallet.tonwallet.models.Wallet;
import br.com.igoramaral.wallet.tonwallet.service.CreditCardService;
import br.com.igoramaral.wallet.tonwallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author IgorAmaral
 */
@RestController
public class WalletController {
    
    private final CreditCardService creditCardService;
    private final WalletService walletService;
    
    @Autowired
    public WalletController(CreditCardService creditCardService, WalletService walletService) {
        this.creditCardService = creditCardService;
        this.walletService = walletService;
    }
            
    @GetMapping("/users/{user_id}/wallet")
    //Return a wallet given an user id
    public Wallet getWallet(@PathVariable(value="user_id") long id){
        return walletService.getWallet(id);
    }
    
    @PutMapping("/users/{user_id}/wallet")
    public Wallet updateUserLimit(@RequestBody Wallet wallet){
        return walletService.updateUserLimit(wallet);
    }
    
    @PostMapping("/users/{user_id}/wallet/payment")
    public Wallet makePayment(@PathVariable(value="user_id") long user_id, @RequestBody ValueDTO value){
        return walletService.makePayment(user_id, value.getValue());
    }
}
