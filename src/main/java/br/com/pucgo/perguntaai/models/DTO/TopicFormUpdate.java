package br.com.pucgo.perguntaai.models.DTO;

import br.com.pucgo.perguntaai.models.Topic;
import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.models.enums.TopicStatus;
import br.com.pucgo.perguntaai.repositories.TopicRepository;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class TopicFormUpdate {

    @NotEmpty @NotNull @Length(min = 5)
    private String title;
    @NotEmpty @NotNull @Length(min = 10)
    private String message;
    @NotNull
    private TopicStatus status;
    @NotNull
    private Long authorId;

    public Topic update(Long id, TopicRepository topicRepository) {
        Topic topic = topicRepository.getById(id);
        topic.setTitle(this.title);
        topic.setMessage(this.message);
        User auhtor = topic.getAuthor();
        auhtor.setId(this.authorId);
        topic.setAuthor(auhtor);
        topic.setStatus(this.status);

        return topic;
    }
}
