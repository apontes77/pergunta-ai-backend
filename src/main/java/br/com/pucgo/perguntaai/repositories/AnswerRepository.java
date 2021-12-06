package br.com.pucgo.perguntaai.repositories;

import br.com.pucgo.perguntaai.models.Answer;
import br.com.pucgo.perguntaai.models.Topic;
import br.com.pucgo.perguntaai.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByTopicAndAuthor(Topic topic, User user);
    Page<Answer> findByTopic(Topic topic, Pageable pagination);
}
