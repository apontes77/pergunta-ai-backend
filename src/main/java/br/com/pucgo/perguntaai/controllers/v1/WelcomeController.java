package br.com.pucgo.perguntaai.controllers.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class WelcomeController {

    @GetMapping
    public String welcome(){
        return "BEM-VINDO!!!!";
    }
}
