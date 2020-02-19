/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.igoramaral.wallet.tonwallet.controller;

import br.com.igoramaral.wallet.tonwallet.models.User;
import br.com.igoramaral.wallet.tonwallet.models.Wallet;
import br.com.igoramaral.wallet.tonwallet.service.CreditCardService;
import br.com.igoramaral.wallet.tonwallet.service.WalletService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    
    @GetMapping("/wallets")
    //Return all users from db
    public List<Wallet> getAllWallets(){
        return walletService.getAllWallets();
    }
    
    @GetMapping("/wallets/{id}")
    //Return a single user given an id number
    public Wallet getWallet(@PathVariable(value="id") long id){
        return walletService.getWallet(id);
    }
    
    @PostMapping("/wallets")
    public Wallet saveWallet(@RequestBody User user){
        User userToBeSaved = new User(user.getName());
        Wallet walletToBeSaved = new Wallet(userToBeSaved);
        return walletService.saveWallet(walletToBeSaved);
    }
    
    @PutMapping("/wallets")
    public Wallet updateUserLimit(@RequestBody Wallet wallet){
        return walletService.updateUserLimit(wallet);
    }
    
    @DeleteMapping("/wallets")
    public void deleteWallet(@RequestBody Wallet wallet){
        //creditCardService.deleteAllCardsFromWallet(wallet.getId());
        walletService.deleteWallet(wallet);
    }
}
