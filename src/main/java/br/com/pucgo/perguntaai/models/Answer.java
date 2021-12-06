package br.com.pucgo.perguntaai.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "TB_ANSWER")
public class Answer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private LocalDateTime creationDate = LocalDateTime.now();
    @ManyToOne
    private User author;
    @ManyToOne(cascade = CascadeType.ALL)
    private Topic topic;
    private boolean solution = false;
    private int answerLike = 0;

    public Answer(String message, Long authorId, Long topicId) {
        this.message = message;
        this.author  = new User();
        this.author.setId(authorId);
        this.topic  = new Topic();
        this.topic.setId(topicId);
    }
    public Answer(String message, Long authorId, Long topicId, boolean solution) {
        this.message = message;
        this.author  = new User();
        this.author.setId(authorId);
        this.topic  = new Topic();
        this.topic.setId(topicId);
        this.solution = solution;
    }
}
