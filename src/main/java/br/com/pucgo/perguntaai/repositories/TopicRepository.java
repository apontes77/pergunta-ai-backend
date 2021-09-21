package br.com.pucgo.perguntaai.repositories;

import br.com.pucgo.perguntaai.models.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    Page<Topic> findByCourse_Name(String courseName, Pageable pagination);
}
