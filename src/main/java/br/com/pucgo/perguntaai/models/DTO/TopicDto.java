package br.com.pucgo.perguntaai.models.DTO;

import br.com.pucgo.perguntaai.models.Topic;
import br.com.pucgo.perguntaai.models.enums.TopicStatus;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Getter
public class TopicDto {
    private final Long id;
    private final String message;
    private final String title;
    private final LocalDateTime creationDate;
    private final Long authorId;
    private final TopicStatus status;


    public TopicDto(Topic topic) {
        this.id = topic.getId();
        this.message = topic.getMessage();
        this.title = topic.getTitle();
        this.creationDate = topic.getCreationDate();
        this.authorId = topic.getAuthor().getId();
        this.status = topic.getStatus();
    }

    public static Page<TopicDto> convert(Page<Topic> topics) {
        return topics.map(TopicDto::new);
    }
}
