/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.igoramaral.wallet.tonwallet.repository;

import br.com.igoramaral.wallet.tonwallet.models.CreditCard;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author IgorAmaral
 */
@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long>{
    List<CreditCard> findByWalletId(long id);
    CreditCard findById(long id);
}
