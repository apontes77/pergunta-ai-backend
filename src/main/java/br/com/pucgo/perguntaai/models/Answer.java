package br.com.pucgo.perguntaai.models;

import br.com.pucgo.perguntaai.models.DTO.UserAdaptedForAnswerDTO;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private LocalDateTime creationDate;
    @OneToOne
    private UserAdaptedForAnswerDTO user;
    @ManyToOne
    private Topic topic;
}
