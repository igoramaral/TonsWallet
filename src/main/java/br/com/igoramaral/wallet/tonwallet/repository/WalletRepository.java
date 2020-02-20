/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.igoramaral.wallet.tonwallet.repository;

import br.com.igoramaral.wallet.tonwallet.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author IgorAmaral
 */
@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long>{
    Wallet findById(long id);
    Wallet findByUserId(long id);
}
