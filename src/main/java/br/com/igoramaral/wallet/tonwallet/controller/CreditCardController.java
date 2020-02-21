/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.igoramaral.wallet.tonwallet.controller;

import br.com.igoramaral.wallet.tonwallet.models.CreditCard;
import br.com.igoramaral.wallet.tonwallet.models.ValueDTO;
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
public class CreditCardController {
    
    private final CreditCardService creditCardService;
    private final WalletService walletService;
    
    @Autowired
    public CreditCardController(CreditCardService creditCardService, WalletService walletService) {
        this.creditCardService = creditCardService;
        this.walletService = walletService;
    }
    
    @GetMapping("/users/{user_id}/wallet/creditcards")
    //Return all credit cards from a given wallet from db
    public List<CreditCard> getAllCreditCardsFromWallet(@PathVariable(value="user_id") long user_id){
        long wallet_id = walletService.getWallet(user_id).getId();  //get walet_id from user_id
        return creditCardService.getAllCreditCardsFromWallet(wallet_id);
    }
    
    @PostMapping("/users/{user_id}/wallet/creditcards")
    public CreditCard insertCreditCard(@PathVariable(value="user_id") long user_id, @RequestBody CreditCard creditCard){
        long wallet_id = walletService.getWallet(user_id).getId(); //get wallet from user_id
        return creditCardService.saveCreditCardUpdatingLimit(wallet_id, creditCard);        
    }
    
    @PutMapping("/users/{user_id}/wallet/creditcards")
    public CreditCard updateCreditCardInfo(@PathVariable(value="user_id") long user_id, @RequestBody CreditCard creditCard){
        long wallet_id = walletService.getWallet(user_id).getId(); //get wallet from user_id
        return creditCardService.updateCreditCardInfo(wallet_id, creditCard);
    }
    
    @DeleteMapping("/users/{user_id}/wallet/creditcards")
    public void deleteCreditCard(@PathVariable(value="user_id") long user_id, @RequestBody CreditCard creditCard){
        long wallet_id = walletService.getWallet(user_id).getId();
        creditCardService.deleteCreditCard(wallet_id, creditCard);
    }
    
    @GetMapping("/users/{user_id}/wallet/creditcards/{card_id}")
    //Return a single credit card given an id number
    public CreditCard getCreditCard(@PathVariable(value="card_id") long card_id){
        return creditCardService.getCreditCard(card_id);
    }
    
    @PostMapping("/users/{user_id}/wallet/creditcards/{card_id}")
    //Return a single card after making a payment to raise available limit given an user_id to find a wallet and card_id number to find a card
    public CreditCard makePaymentOnCard(@PathVariable(value="user_id") long user_id, @PathVariable(value="card_id") long card_id, @RequestBody ValueDTO value){
        long wallet_id = walletService.getWallet(user_id).getId();
        return creditCardService.makePayment(wallet_id, card_id, value.getValue());
    }
    
}
