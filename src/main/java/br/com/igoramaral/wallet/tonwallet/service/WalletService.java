/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.igoramaral.wallet.tonwallet.service;

import br.com.igoramaral.wallet.tonwallet.models.Wallet;
import br.com.igoramaral.wallet.tonwallet.repository.CreditCardRepository;
import br.com.igoramaral.wallet.tonwallet.repository.WalletRepository;
import java.math.BigDecimal;
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
    
    public List<Wallet> getAllWallets(){
        return walletRepository.findAll();
    }
    
    public Wallet getWallet(long id){
        return walletRepository.findById(id);
    }
    
    public Wallet saveWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }
    
    public void updateMaxLimit(Wallet wallet){
        walletRepository.save(wallet);
    }
    
    //updates User defined Limit
    public Wallet updateUserLimit(Wallet wallet){
        BigDecimal userLimit = wallet.getUserLimit();
        BigDecimal maxLimit = wallet.getMaxLimit();
        if(userLimit.compareTo(maxLimit) == 1){
            return null;
        } else {
            return walletRepository.save(wallet);
        }
    }
    
    public void deleteWallet(Wallet wallet){
        walletRepository.deleteById(wallet.getId());
    }
    
}
