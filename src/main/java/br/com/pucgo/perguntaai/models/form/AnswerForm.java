package br.com.pucgo.perguntaai.models.form;

import br.com.pucgo.perguntaai.models.Answer;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AnswerForm {
    @NotNull
    @NotEmpty
    @Length(min = 10)
    private String message;

    @NotNull
    private Long authorId;

    @NotNull
    private Long topicId;

    public Answer convert() {
        return new Answer(message, authorId, topicId);
    }
}
