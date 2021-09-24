package br.com.pucgo.perguntaai.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "TB_TOPIC")
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
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Tag> tags = new ArrayList<>();


    public Topic(String title, String message, List<Tag> tags) {
        this.title = title;
        this.message = message;
        this.tags = tags;
    }
}
