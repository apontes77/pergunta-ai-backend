package br.com.pucgo.perguntaai.repositories;

import br.com.pucgo.perguntaai.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsRepository extends JpaRepository<Tag, Long> {
}
