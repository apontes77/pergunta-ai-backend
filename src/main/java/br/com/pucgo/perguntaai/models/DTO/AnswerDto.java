package br.com.pucgo.perguntaai.models.DTO;

import br.com.pucgo.perguntaai.models.Answer;
import br.com.pucgo.perguntaai.models.Topic;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnswerDto {
    private final Long id;
    private final String message;
    private final LocalDateTime creationDate;
    private final String authorName;

    public AnswerDto (Answer answer) {
        this.id = answer.getId();
        this.message = answer.getMessage();
        this.creationDate = answer.getCreationDate();
        this.authorName = answer.getAuthor().getName();
    }

    public static Page<AnswerDto> convert(Page<Answer> answers) {
        return answers.map(AnswerDto::new);
    }
}
