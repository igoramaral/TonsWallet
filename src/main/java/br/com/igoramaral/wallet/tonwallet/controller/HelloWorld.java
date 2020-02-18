
package br.com.igoramaral.wallet.tonwallet.controller;

import br.com.igoramaral.wallet.tonwallet.models.User;
import br.com.igoramaral.wallet.tonwallet.repository.UserRepository;
import java.util.List;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author IgorAmaral
 */
@RestController
public class HelloWorld {
    
    private final UserRepository userRepository;

    public HelloWorld(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    
    
    @GetMapping("/")
    String hello() {
        return "Hello World from heroku!";
    }
    
    @GetMapping("/users")
    List<User> users(){
        return userRepository.findAll();
    }
    
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            userRepository.save(new User("Igor Amaral"));
            userRepository.save(new User("Natalia Nascimento"));
        };
            
    }
}


