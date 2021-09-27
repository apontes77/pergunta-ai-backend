package br.com.pucgo.perguntaai;

import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PerguntaaiBackendApplicationTests {

	UserRepository userRepository;
	@Test
	void mustResgisterUser() {
		final User registerUser = User.builder()
				.name("Aluno Teste")
				.email("alunoteste@puc.com.br")
				.course("ADS")
				.password("123456")
				.build();

		userRepository.save(registerUser);
		assertNotNull(userRepository);
	}

}
