package br.com.pucgo.perguntaai.models.DTO;

import br.com.pucgo.perguntaai.models.Answer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnswerDto {
    private final Long id;
    private final String message;
    private final LocalDateTime creationDate;

    public AnswerDto (Answer answer) {
        this.id = answer.getId();
        this.message = answer.getMessage();
        this.creationDate = answer.getCreationDate();
    }
}
