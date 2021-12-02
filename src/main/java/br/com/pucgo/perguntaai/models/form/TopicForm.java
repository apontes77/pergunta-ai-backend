package br.com.pucgo.perguntaai.models.form;

import br.com.pucgo.perguntaai.models.Topic;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TopicForm {
    @NotNull
    @NotEmpty
    @Length(min = 5)
    private String title;
    @NotNull
    @NotEmpty
    @Length(min = 10)
    private String message;
    @NotNull
    @NotEmpty
    private List<String> tags = new ArrayList<>();
    
    private Long authorId;

    public Topic convert() {
        return new Topic(title, message, tags, authorId);
    }
}
