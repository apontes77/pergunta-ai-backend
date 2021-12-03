package br.com.pucgo.perguntaai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class PerguntaaiBackendApplication {

	public static void main(String[] args) {
		System.out.println(LocalDateTime.now());

		SpringApplication.run(PerguntaaiBackendApplication.class, args);
	}

}
