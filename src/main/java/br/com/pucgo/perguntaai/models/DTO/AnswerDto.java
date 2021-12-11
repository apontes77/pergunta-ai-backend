package br.com.pucgo.perguntaai.models.DTO;

import br.com.pucgo.perguntaai.models.Answer;
import br.com.pucgo.perguntaai.models.Topic;
import br.com.pucgo.perguntaai.models.enums.AvatarOptions;
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
    private final Long authorId;
    private final String authorName;
    private final AvatarOptions authorAvatar;
    private final int answerLikes;
    private final boolean solution;

    public AnswerDto (Answer answer) {
        this.id = answer.getId();
        this.message = answer.getMessage();
        this.creationDate = answer.getCreationDate();
        this.authorId = answer.getAuthor().getId();
        this.authorName = answer.getAuthor().getName();
        this.authorAvatar = answer.getAuthor().getAvatarOptions();
        this.answerLikes = answer.getAnswerLike();
        this.solution = answer.isSolution();
    }

    public static Page<AnswerDto> convert(Page<Answer> answers) {
        return answers.map(AnswerDto::new);
    }
}
