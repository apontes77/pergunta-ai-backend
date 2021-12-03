package br.com.pucgo.perguntaai.repositories;

import br.com.pucgo.perguntaai.models.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
