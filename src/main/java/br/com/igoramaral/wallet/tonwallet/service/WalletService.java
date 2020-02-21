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
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author IgorAmaral
 */
@Service
public class WalletService {
    
    private WalletRepository walletRepository;
    private CreditCardRepository creditCardRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository, CreditCardRepository creditCardRepository) {
        this.walletRepository = walletRepository;
        this.creditCardRepository = creditCardRepository;
    }
    
    //Returns all wallets from database
    public List<Wallet> getAllWallets(){
        return walletRepository.findAll();
    }
    
    //get a single wallet based on UserId
    public Wallet getWallet(long id){
        Wallet wallet = walletRepository.findByUserId(id);
        if (wallet != null){
            return wallet;
        } else throw new UserNotFoundException("Wallet not found for user_id=" + id);
    }
    
    //Saves a wallet on the database
    public Wallet saveWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }
    
    //saves a wallet without returning an Wallet object
    public void updateMaxLimit(Wallet wallet){
        walletRepository.save(wallet);
    }
    
    //updates the user defined Limit on the wallet
    public Wallet updateUserLimit(long user_id, String value){
        Wallet oldWalletInfo = walletRepository.findByUserId(user_id);
        if (oldWalletInfo != null){
            Wallet wallet = oldWalletInfo;
            BigDecimal userLimit = new BigDecimal(value);
            //UserLimit cannot be bigger than wallet's maxLimit
            if(userLimit.compareTo(oldWalletInfo.getMaxLimit()) == 1){
                throw new WrongRequestAttributeException("UserLimit cannot be greater than Wallet's MaxLimit");
            } else {
                wallet.setUserLimit(userLimit);
                //get the Sum of all cards available limit. This is the max availableLimit
                List<CreditCard> walletCards = creditCardRepository.findByWalletId(wallet.getId());
                BigDecimal totalCardLimits = new BigDecimal("0.00");
                for (int i = 0; i < walletCards.size(); i++){
                    totalCardLimits = totalCardLimits.add(walletCards.get(i).getAvailableLimit());
                }
                if(totalCardLimits.compareTo(userLimit) <= 0) {
                    //if max availableLimit is smaller or equal to new userLimit, new availableLimit is equal to max availableLimit
                    wallet.setAvailableLimit(totalCardLimits);
                } else {
                    //else, new availableLimit is equal to the userLimit
                    wallet.setAvailableLimit(userLimit);
                }
            }
            return walletRepository.save(wallet);
        }else throw new UserNotFoundException("Wallet not found for user_id=" + user_id);
    }
    
    //delete a wallet from database
    public void deleteWallet(Wallet wallet){
        walletRepository.deleteById(wallet.getId());
    }
    
    //makes a payment on the wallet based on credit cards and available limit
    public Wallet makePayment(long user_id, String value){
        Wallet wallet = walletRepository.findByUserId(user_id);
        if(wallet != null){
            BigDecimal availableLimit = wallet.getAvailableLimit();
            BigDecimal paymentValue = new BigDecimal(value);
            //checks if paymentValue is less or equal to the wallet's availableLimit
            if(paymentValue.compareTo(availableLimit) <= 0){
                wallet.setAvailableLimit(wallet.getAvailableLimit().subtract(paymentValue));
                List<CreditCard> walletCards = creditCardRepository.findByWalletId(wallet.getId());
                //sort the list based on the wallet's payment criteria:
                //1) make payment first on the most distant payment date
                //2) if the dates are equal, make payment first on the card with thelowest available limit
                Collections.sort(walletCards, new Comparator<CreditCard>() {
                    @Override
                    public int compare(CreditCard c1, CreditCard c2) {
                        LocalDate dateToday = LocalDate.now();
                        LocalDate dateCard1;
                        LocalDate dateCard2;
                        int today = dateToday.getDayOfMonth();
                        if (today < c1.getPaymentDay()){
                            dateCard1 = LocalDate.now().withDayOfMonth(c1.getPaymentDay());
                        } else {
                            dateCard1 = LocalDate.now().withDayOfMonth(c1.getPaymentDay()).plusMonths(1);
                        }
                        if (today < c2.getPaymentDay()){
                            dateCard2 = LocalDate.now().withDayOfMonth(c2.getPaymentDay());
                        } else {
                            dateCard2 = LocalDate.now().withDayOfMonth(c2.getPaymentDay()).plusMonths(1);
                        }
                        c1.setPaymentDate(dateCard1);
                        c2.setPaymentDate(dateCard2);
                        if (c2.getPaymentDate().compareTo(c1.getPaymentDate()) != 0){
                            return c2.getPaymentDate().compareTo(c1.getPaymentDate());
                        } else {
                            return c1.getAvailableLimit().compareTo(c2.getAvailableLimit());
                        }
                    }
                });
                //the list is now sorted. we may proceed to make payment on each card
                int i = 0;
                while (paymentValue.compareTo(new BigDecimal("0.00")) != 0){
                    BigDecimal cardLimit = walletCards.get(i).getAvailableLimit();
                    if(cardLimit.compareTo(new BigDecimal("0.00")) == 0){
                        i ++;
                    }
                    else {
                        //if the cardLimit is less than the payment value, split value with the next card
                        if(cardLimit.compareTo(paymentValue) < 0){
                            CreditCard card = walletCards.get(i);
                            card.setAvailableLimit(new BigDecimal("0.00"));
                            paymentValue = paymentValue.subtract(cardLimit);
                            card.setWallet(wallet);
                            creditCardRepository.save(card);
                            i ++;
                        }
                        else {
                            //if the cardLimit is greater than the payment value, make the payment entirely on this card
                            CreditCard card = walletCards.get(i);
                            card.setAvailableLimit(card.getAvailableLimit().subtract(paymentValue));
                            paymentValue = paymentValue.subtract(paymentValue);
                            card.setWallet(wallet);
                            creditCardRepository.save(card);
                            break;
                        }
                    }  
                }
                return walletRepository.save(wallet);
            } else throw new WrongRequestAttributeException("Payment Value cannot be Bigger than Wallet's Available Limit");
        } else throw new UserNotFoundException("Wallet not found for user_id=" + user_id);
    }
    
}
