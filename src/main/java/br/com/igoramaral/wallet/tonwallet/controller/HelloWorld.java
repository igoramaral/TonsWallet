
package br.com.igoramaral.wallet.tonwallet.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author IgorAmaral
 */
@RestController
public class HelloWorld {
    
    @GetMapping("/")
    String hello() {
        return "Hello World from heroku!";
    }
}
