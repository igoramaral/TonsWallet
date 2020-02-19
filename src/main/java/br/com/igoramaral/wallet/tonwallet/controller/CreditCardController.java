/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.igoramaral.wallet.tonwallet.controller;

import br.com.igoramaral.wallet.tonwallet.models.CreditCard;
import br.com.igoramaral.wallet.tonwallet.models.Wallet;
import br.com.igoramaral.wallet.tonwallet.service.CreditCardService;
import br.com.igoramaral.wallet.tonwallet.service.WalletService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author IgorAmaral
 */
@RestController
public class CreditCardController {
    
    private final CreditCardService creditCardService;
    private final WalletService walletService;
    
    @Autowired
    public CreditCardController(CreditCardService creditCardService, WalletService walletService) {
        this.creditCardService = creditCardService;
        this.walletService = walletService;
    }
    
    @GetMapping("/wallets/{wallet_id}/creditcards")
    //Return all credit cards from a given wallet from db
    public List<CreditCard> getAllCreditCardsFromWallet(@PathVariable(value="wallet_id") long wallet_id){
        return creditCardService.getAllCreditCardsFromWallet(wallet_id);
    }
    
    @GetMapping("/wallets/{wallet_id}/creditcards/{id}")
    //Return a single user given an id number
    public CreditCard getCreditCard(@PathVariable(value="id") long id){
        return creditCardService.getCreditCard(id);
    }
    
    @PostMapping("/wallets/{wallet_id}/creditcards")
    public CreditCard saveCreditCard(@PathVariable(value="wallet_id") long wallet_id, @RequestBody CreditCard creditCard){
        Wallet wallet = walletService.getWallet(wallet_id); //get wallet from id
        BigDecimal newLimit = creditCard.getMaxLimit(); //get credit card limit value
        wallet.setMaxLimit(wallet.getMaxLimit().add(newLimit)); //adds credit card limit to wallet limit
        wallet.addCard(creditCard);
        walletService.updateMaxLimit(wallet);   //updates wallet information
        creditCard.setWallet(wallet);
        return creditCardService.saveCreditCard(creditCard);    //saves credit card
        
    }
    
}
