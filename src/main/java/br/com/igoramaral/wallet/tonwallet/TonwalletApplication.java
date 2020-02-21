package br.com.igoramaral.wallet.tonwallet;

import java.util.Collections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@Controller
public class TonwalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(TonwalletApplication.class, args);
	}

        @Bean
        public Docket swaggerConfiguration(){
            
            return new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("br.com.igoramaral.wallet.tonwallet.controller"))
                    .build()
                    .apiInfo(apiDetails());
        }
        

        @RequestMapping ("/")
        public String home() {
            return "redirect:/swagger-ui.html";
        }
        
        private ApiInfo apiDetails(){
            return new ApiInfo(
                    "Ton's Wallet API",
                    "API for managing a virtual multi-creditcard wallet created for Ton's selection process",
                    "1.0",
                    "Free to Use",
                    new springfox.documentation.service.Contact("Igor Amaral", "https://www.github.com/igoramaral", ""),
                    "API Git Repository",
                    "https://www.github.com/igoramaral/TonsWallet",
                    Collections.emptyList());
        }
}
