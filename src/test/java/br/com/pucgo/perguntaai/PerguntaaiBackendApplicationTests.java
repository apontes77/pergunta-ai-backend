package br.com.pucgo.perguntaai;

import br.com.pucgo.perguntaai.controllers.v1.UserController;
import br.com.pucgo.perguntaai.models.DTO.UserDto;
import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.models.form.UserForm;
import br.com.pucgo.perguntaai.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
class PerguntaaiBackendApplicationTests {



}
