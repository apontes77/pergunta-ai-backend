package br.com.pucgo.perguntaai.models.DTO;

import br.com.pucgo.perguntaai.models.Topic;
import br.com.pucgo.perguntaai.models.enums.TopicStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TopicDtoDetails {
    private Long id;
    private String title;
    private String message;
    private LocalDateTime creationDate;
    private String authorName;
    private TopicStatus status;
    private List<AnswerDto> answers;

    public TopicDtoDetails (Topic topic) {
        this.id = topic.getId();
        this.message = topic.getMessage();
        this.creationDate = topic.getCreationDate();
        this.authorName = topic.getAuthor().getName();
        this.status = topic.getStatus();
        this.answers = new ArrayList<>();
        this.answers.addAll(topic.getAnswers()
                                    .stream()
                                    .map(AnswerDto::new)
                                    .collect(Collectors.toList()));
    }
}
