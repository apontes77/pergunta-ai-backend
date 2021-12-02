package br.com.pucgo.perguntaai.models;

import br.com.pucgo.perguntaai.models.enums.TopicStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "TB_TOPIC")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("message")
    private String message;

    @JsonProperty("creationDate")
    private LocalDateTime creationDate = LocalDateTime.now();

    @ManyToOne
    @JsonProperty("author")
    private User author = new User();

    @JsonProperty("status")
    @Enumerated(EnumType.STRING)
    private TopicStatus status = TopicStatus.NOT_ANSWERED;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "answer_id")
    @JsonProperty("answers")
    private List<Answer> answers = new ArrayList<>();

    @JsonProperty("tags")
    @ElementCollection(targetClass=String.class)
    private List<String> tags = new ArrayList<>();


    public Topic(String title, String message, List<String> tags, Long authorId) {
        this.title = title;
        this.message = message;
        this.tags = tags;
        this.author.setId(authorId);
    }
}
