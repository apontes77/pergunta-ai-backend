package br.com.pucgo.perguntaai.ControllerTest;

import br.com.pucgo.perguntaai.controllers.v1.LoginController;
import br.com.pucgo.perguntaai.models.form.LoginForm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class AuthenticationUserTest {
    @Autowired
    private LoginController loginController;

    @Test
    void mustAuthenticateUser () {
        LoginForm loginUser = LoginForm.builder()
                .email("aluno@pucgo.edu.br")
                .password("123456")
                .build();

        ResponseEntity<?> userCreated = loginController.authenticate(loginUser);
        Assertions.assertEquals(userCreated.getStatusCodeValue(), 200);
    }

    @Test
    void mustNotAuthenticateUser () {
        //tentativa de autenticação com a senha incorreta
        LoginForm loginUser = LoginForm.builder()
                .email("aluno@pucgo.edu.br")
                .password("123")
                .build();

        ResponseEntity<?> userCreated = loginController.authenticate(loginUser);
        Assertions.assertEquals(userCreated.getStatusCodeValue(), 400);
    }
}
