/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.igoramaral.wallet.tonwallet.service;

import br.com.igoramaral.wallet.tonwallet.models.User;
import br.com.igoramaral.wallet.tonwallet.repository.UserRepository;
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
    
    //Save a user on the database
    public User saveUser(User user){
        return userRepository.save(user);
    }
    
    //Updates a user
    public User updateUserInfo(long id, User updatedUser){
        User user = userRepository.findById(id);
        PersistenceUtil util = Persistence.getPersistenceUtil();
        if(util.isLoaded(user)){
            user.setName(updatedUser.getName());
            return userRepository.save(user);
        } else return null;
    }
    
    //Deletes a user (and respective data) from the database
    public void deleteUser(long id){
        userRepository.deleteById(id);
    }
}
