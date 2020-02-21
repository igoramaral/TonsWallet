/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.igoramaral.wallet.tonwallet.service;

import br.com.igoramaral.wallet.tonwallet.exceptions.UserNotFoundException;
import br.com.igoramaral.wallet.tonwallet.exceptions.WrongRequestAttributeException;
import br.com.igoramaral.wallet.tonwallet.models.CreditCard;
import br.com.igoramaral.wallet.tonwallet.models.Wallet;
import br.com.igoramaral.wallet.tonwallet.repository.CreditCardRepository;
import br.com.igoramaral.wallet.tonwallet.repository.WalletRepository;
import java.math.BigDecimal;
import java.util.List;
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
        CreditCard card = creditCardRepository.findById(id);
        if (card != null){
            return card;
        }else throw new UserNotFoundException("Card not found for card_id=" + id);
    }
    
    public CreditCard saveCreditCard(CreditCard creditCard) {
        return creditCardRepository.save(creditCard);
    }
    
    public CreditCard saveCreditCardUpdatingLimit(long wallet_id, CreditCard creditCard){
        
        Wallet wallet = walletRepository.findById(wallet_id);
        if(wallet != null){
            creditCard.setWallet(wallet);
            BigDecimal newLimit = creditCard.getMaxLimit();
            wallet.setMaxLimit(wallet.getMaxLimit().add(newLimit)); //add credit card limit to the wallet max limit
            //if actual walletAvailableLimit + cardAvailableLimit > walletUserLimit, walletAvailableLimit = walletUserLimit
            //else, walletAvailableLimit += cardAvailableLimit
            if(wallet.getAvailableLimit().add(creditCard.getAvailableLimit()).compareTo(wallet.getUserLimit()) == 1){
                wallet.setAvailableLimit(wallet.getUserLimit());
            }
            else {
                wallet.setAvailableLimit(wallet.getAvailableLimit().add(creditCard.getAvailableLimit())); 
            }
            return creditCardRepository.save(creditCard);
        } else throw new UserNotFoundException("Wallet not found for wallet_id=" + wallet_id);
    }
    
    public CreditCard updateCreditCardInfo(long wallet_id, CreditCard creditCard){
        Wallet wallet = walletRepository.findById(wallet_id);
        if(wallet != null){
            CreditCard oldCardInfo = getCreditCard(creditCard.getId());
            if (oldCardInfo.getAvailableLimit().compareTo(creditCard.getAvailableLimit()) != 0){
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
            } throw new WrongRequestAttributeException("Cannot update credit card with different Available Limit value. Use payment function to update Available Limit.");
        } else throw new UserNotFoundException("Wallet not found for wallet_id=" + wallet_id);
    }
    
    public void deleteCreditCard(long wallet_id, CreditCard creditCard){
        Wallet wallet = walletRepository.findById(wallet_id);
        if(wallet != null){
            creditCard.setWallet(wallet);
            BigDecimal newLimit = creditCard.getMaxLimit();
            wallet.setMaxLimit(wallet.getMaxLimit().subtract(newLimit));
            creditCardRepository.delete(creditCard);
        }else throw new UserNotFoundException("Wallet not found for wallet_id=" + wallet_id);
    }
    
    public CreditCard makePayment(long wallet_id, long card_id, String value){
        CreditCard card = creditCardRepository.findById(card_id);
        Wallet wallet = walletRepository.findById(wallet_id);
        PersistenceUtil util = Persistence.getPersistenceUtil();
        if(card != null){
            if (wallet != null){
                //check if available limit is equal to max card limit
                if (card.getAvailableLimit().compareTo(card.getMaxLimit()) == 0){
                    //if the limits are equal, there's no need to update available limit since its alredy the maximum
                    throw new WrongRequestAttributeException("Credit Card available limit is already the maximum Credit Card limit.");
                } else {
                    BigDecimal maxPaymentValue = card.getMaxLimit().subtract(card.getAvailableLimit()); 
                    BigDecimal paymentValue = new BigDecimal(value);
                    if(paymentValue.compareTo(maxPaymentValue) <= 0){
                        //if the paymentValue is smaller or equal to maxPaymentValue, add it directly to the card and wallet limits
                        card.setAvailableLimit(card.getAvailableLimit().add(paymentValue)); //adds payment value to the card available limit
                        //if walletAvailableLimit + paymentValue > walletUserLimit, walletAvailableLimit = walletUserLimit
                        //else, walletAvailableLimit += paymentValue
                        if(wallet.getAvailableLimit().add(paymentValue).compareTo(wallet.getUserLimit()) == 1){
                            wallet.setAvailableLimit(wallet.getUserLimit());
                        }
                        else {
                            wallet.setAvailableLimit(wallet.getAvailableLimit().add(paymentValue)); //adds payment value to the wallet available limit
                        }
                        card.setWallet(wallet);
                        return creditCardRepository.save(card);
                    } else {
                        //cannot make a payment that will make availableLimit higher than maxLimit
                        throw new WrongRequestAttributeException("Payment Value cannot be greater than Credit Card's Max Limit");
                    }
                }
            }else throw new UserNotFoundException("Wallet not found for wallet_id=" + wallet_id);
        } else throw new UserNotFoundException("Credit Card not found for card_id=" + card_id);
    }
    
}
