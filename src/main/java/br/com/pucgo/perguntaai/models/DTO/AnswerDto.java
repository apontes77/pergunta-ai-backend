package br.com.pucgo.perguntaai.models.DTO;

import br.com.pucgo.perguntaai.models.Answer;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AnswerDto {
    private Long id;
    private String message;
    private LocalDateTime creationDate;
    private String authorName;

    public AnswerDto (Answer answer) {
        this.id = answer.getId();
        this.message = answer.getMessage();
        this.creationDate = answer.getCreationDate();
        this.authorName = answer.getAuthor().getName();
    }
}
