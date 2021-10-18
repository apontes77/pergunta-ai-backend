package br.com.pucgo.perguntaai.ControllerTest;

import br.com.pucgo.perguntaai.controllers.v1.UserController;
import br.com.pucgo.perguntaai.models.form.UserForm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class RestisterUserTest {
    @Autowired
    private UserController userController;

    @Test
    void mustResgisterUser () {

        UserForm registerUser = UserForm.builder()
                .name("Aluno Teste")
                .email("alunoteste@pucgo.edu.br")
                .course("análise e desenvolvimento de sistemas")
                .password("123456")
                .build();

        ResponseEntity<?> userCreated = userController.register(registerUser);
        Assertions.assertEquals(userCreated.getStatusCodeValue(), 201);
    }

    @Test
    void mustNotResgisterUser () {
        //usuário já existe no banco de testes
        UserForm registerUser = UserForm.builder()
                .name("Aluno Teste")
                .email("aluno@pucgo.edu.br")
                .course("análise e desenvolvimento de sistemas")
                .password("123456")
                .build();

        ResponseEntity<?> userCreated = userController.register(registerUser);
        Assertions.assertEquals(userCreated.getStatusCodeValue(), 400);
    }
}
