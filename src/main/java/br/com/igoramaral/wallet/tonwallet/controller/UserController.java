/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.igoramaral.wallet.tonwallet.controller;

import br.com.igoramaral.wallet.tonwallet.models.User;
import br.com.igoramaral.wallet.tonwallet.models.Wallet;
import br.com.igoramaral.wallet.tonwallet.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author IgorAmaral
 */
@RestController

public class UserController {
    
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
}
    
    @GetMapping("/users")
    //Return all users from db
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
    
    @GetMapping("/users/{id}")
    //Return a single user given an id number
    public User getUser(@PathVariable(value="id") long id){
        return userService.getUser(id);
    }
    
    @PostMapping("/users")
    public User addUser(@RequestBody User user){
        User userToBeSaved = new User(user.getName(), new Wallet("0.00","0.00","0.00")); 
        return userService.saveUser(userToBeSaved);
    }
    
    
    
}
