package br.com.pucgo.perguntaai.models.form;

import br.com.pucgo.perguntaai.models.Answer;
import br.com.pucgo.perguntaai.models.AnswerLike;
import br.com.pucgo.perguntaai.models.Topic;
import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.repositories.AnswerRepository;
import br.com.pucgo.perguntaai.repositories.TopicRepository;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AnswerUpdateForm {
    @NotNull
    @NotEmpty
    @Length(min = 10)
    private String message;

    @NotNull
    private Long authorId;

    @NotNull
    private Long topicId;

    private boolean solution;

    public Answer update(Long id, AnswerRepository answerRepository) {
        Answer answer = answerRepository.getById(id);
        answer.setMessage(this.message);
        answer.setSolution(this.solution);

        return answer;
    }
}
