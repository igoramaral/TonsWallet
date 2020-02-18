/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.igoramaral.wallet.tonwallet.service;

import br.com.igoramaral.wallet.tonwallet.models.User;
import br.com.igoramaral.wallet.tonwallet.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author IgorAmaral
 */
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    //Return all users from db
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    
    //Return a single user given an id number
    public User getUser(long id){
        return userRepository.findById(id);
    }
    
    public User saveUser(User user){
        return userRepository.save(user);
    }
}
