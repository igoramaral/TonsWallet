/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.igoramaral.wallet.tonwallet.service;

import br.com.igoramaral.wallet.tonwallet.models.CreditCard;
import br.com.igoramaral.wallet.tonwallet.models.Wallet;
import br.com.igoramaral.wallet.tonwallet.repository.CreditCardRepository;
import br.com.igoramaral.wallet.tonwallet.repository.WalletRepository;
import java.math.BigDecimal;
import java.util.List;
import javassist.NotFoundException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author IgorAmaral
 */
@Service
public class CreditCardService {
    
    private WalletRepository walletRepository;
    private CreditCardRepository creditCardRepository;

    @Autowired
    public CreditCardService(CreditCardRepository creditCardRepository, WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
        this.creditCardRepository = creditCardRepository;
    }
    
    public List<CreditCard> getAllCreditCardsFromWallet(long id){
        return creditCardRepository.findByWalletId(id);
    }
    
    public CreditCard getCreditCard(long id){
        return creditCardRepository.findById(id);
    }
    
    public CreditCard saveCreditCard(CreditCard creditCard) {
        return creditCardRepository.save(creditCard);
    }
    
    public CreditCard saveCreditCardUpdatingLimit(long wallet_id, CreditCard creditCard){
        
        Wallet wallet = walletRepository.findById(wallet_id);
        PersistenceUtil util = Persistence.getPersistenceUtil();
        if(util.isLoaded(wallet)){
            creditCard.setWallet(wallet);
            BigDecimal newLimit = creditCard.getMaxLimit();
            wallet.setMaxLimit(wallet.getMaxLimit().add(newLimit));
            return creditCardRepository.save(creditCard);
        } else return null;
    }
    
    public CreditCard updateCreditCardInfo(long wallet_id, CreditCard creditCard){
        Wallet wallet = walletRepository.findById(wallet_id);
        PersistenceUtil util = Persistence.getPersistenceUtil();
        if(util.isLoaded(wallet)){
            CreditCard oldCardInfo = getCreditCard(creditCard.getId());

            int comparison = oldCardInfo.getMaxLimit().compareTo(creditCard.getMaxLimit());
            if(comparison == 0){
                //If the card limit hasn't changed, just save the card info
                creditCard.setWallet(wallet);
                return creditCardRepository.save(creditCard);
            } else if (comparison == -1) {
                //if the old card limit is lesser than the new card limit, add the difference to the wallet limit
                BigDecimal limitDifference = creditCard.getMaxLimit().subtract(oldCardInfo.getMaxLimit());
                wallet.setMaxLimit(wallet.getMaxLimit().add(limitDifference)); //adds credit card limit to wallet limit
                creditCard.setWallet(wallet);
                return creditCardRepository.save(creditCard);
            } else {
                //if the old card limit is more than the new card limit, subtract the difference from the wallet limit
                BigDecimal limitDifference = oldCardInfo.getMaxLimit().subtract(creditCard.getMaxLimit());
                wallet.setMaxLimit(wallet.getMaxLimit().subtract(limitDifference)); //adds credit card limit to wallet limit
                creditCard.setWallet(wallet);
                return creditCardRepository.save(creditCard);
            }
        } else return null;
    }
    
    public void deleteCreditCard(long wallet_id, CreditCard creditCard){
        Wallet wallet = walletRepository.findById(wallet_id);
        PersistenceUtil util = Persistence.getPersistenceUtil();
        if(util.isLoaded(wallet)){
            creditCard.setWallet(wallet);
            BigDecimal newLimit = creditCard.getMaxLimit();
            wallet.setMaxLimit(wallet.getMaxLimit().subtract(newLimit));
            creditCardRepository.delete(creditCard);
        }
    }
    
 
}
