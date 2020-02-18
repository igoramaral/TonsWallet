/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.igoramaral.wallet.tonwallet.repository;

import br.com.igoramaral.wallet.tonwallet.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author IgorAmaral
 */
public interface UserRepository extends JpaRepository<User, Long>{
    
    
    User findById(long id);
    
}
