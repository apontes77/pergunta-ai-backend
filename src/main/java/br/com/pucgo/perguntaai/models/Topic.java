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
    private Long id;
    private String title;
    private String message;
    private LocalDateTime creationDate = LocalDateTime.now();
    @ManyToOne
    private User author;
    @Enumerated(EnumType.STRING)
    private TopicStatus status = TopicStatus.NOT_ANSWERED;
    @OneToMany(mappedBy = "topic")
    private List<Answer> answers = new ArrayList<>();

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name="topic_tag",
    joinColumns = @JoinColumn(name = "topic_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id"))

    @JsonProperty("tags")
    private Set<Tag> tags = new HashSet<>();


    public Topic(String title, String message, Set<Tag> tags) {
        this.title = title;
        this.message = message;
        this.tags = tags;
    }
}
