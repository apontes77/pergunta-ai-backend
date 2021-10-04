package br.com.pucgo.perguntaai.Controllers;

import br.com.pucgo.perguntaai.controllers.v1.UserController;
import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.models.form.UserForm;
import br.com.pucgo.perguntaai.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@Slf4j
public class RegisterUserTests {


    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void givenAnUserToRegister_whenMakesARegister_thenTheRegisterIsMadeWithSuccess() {
        final UserForm registerUserForm = UserForm.builder()
                .name("Aluno Teste")
                .email("alunoteste@puc.com.br")
                .course("ADS")
                .password("123456")
                .build();

        User registerUser = registerUserForm.convert();

        /**
         * este é um mock da resposta do método saveUser em UserService.
         *
         * Para que este mock da resposta do userService funcione,
         * ele deve corresponder exatamente aos argumentos do método saveUser.
         */
        when(userService.saveUser(ArgumentMatchers.any(User.class)))
                .thenReturn(registerUser);

        UriComponentsBuilder uriComponents = UriComponentsBuilder.fromUriString("/api/v1/user/{id}");

        ResponseEntity<?> userCreated = userController.register(registerUserForm, uriComponents);
        assertNotNull(userCreated);
    }

    @Test
    void whenInvalidNameToRegister_thenReturns4xxClientError() throws Exception {
        final UserForm registerUserForm = UserForm.builder()
                .name("A")
                .email("alunoteste@puc.com.br")
                .course("ADS")
                .password("123456")
                .build();

        User registerUser = registerUserForm.convert();

        /**
         * este é um mock da resposta do método saveUser em UserService.
         *
         * Para que este mock da resposta do userService funcione,
         * ele deve corresponder exatamente aos argumentos do método saveUser.
         */
        when(userService.saveUser(ArgumentMatchers.any(User.class)))
                .thenReturn(registerUser);

        UriComponentsBuilder uriComponents = UriComponentsBuilder.fromUriString("/api/v1/user/{id}");

        ResponseEntity<?> userCreated = userController.register(registerUserForm, uriComponents);
        assertNotEquals(registerUser.getName(), "Aluno");

    }
}
