package br.com.pucgo.perguntaai.models.DTO;

import br.com.pucgo.perguntaai.models.Topic;
import br.com.pucgo.perguntaai.models.enums.AvatarOptions;
import br.com.pucgo.perguntaai.models.enums.TopicStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TopicDtoDetails {
    private final Long id;
    private final String message;
    private final LocalDateTime creationDate;
    private final String authorName;
    private final String title;
    private final AvatarOptions avatar;

    private final TopicStatus status;
    private final List<AnswerDto> answers;
    private final List<String> tags;

    public TopicDtoDetails (Topic topic) {
        this.id = topic.getId();
        this.message = topic.getMessage();
        this.creationDate = topic.getCreationDate();
        this.authorName = topic.getAuthor().getName();
        this.status = topic.getStatus();
        this.title = topic.getTitle();
        this.avatar = topic.getAuthor().getAvatarOptions();
        this.answers = new ArrayList<>();
        this.answers.addAll(topic.getAnswers()
                                    .stream()
                                    .map(AnswerDto::new)
                                    .collect(Collectors.toList()));
        this.tags = topic.getTags();
    }
}
